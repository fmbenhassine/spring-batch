package org.springframework.batch.core.repository.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.persistence.converter.JobExecutionConverter;
import org.springframework.batch.core.repository.persistence.converter.StepExecutionConverter;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class MongoStepExecutionDao implements StepExecutionDao {

	private static final String STEP_EXECUTIONS_COLLECTION_NAME = "BATCH_STEP_EXECUTION"; // TODO
																							// make
																							// configurable

	private static final String JOB_EXECUTIONS_COLLECTION_NAME = "BATCH_JOB_EXECUTION"; // TODO
																						// make
																						// configurable

	private final StepExecutionConverter stepExecutionConverter = new StepExecutionConverter();

	private final JobExecutionConverter jobExecutionConverter = new JobExecutionConverter();

	private final MongoOperations mongoOperations;

	public MongoStepExecutionDao(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	@Override
	public void saveStepExecution(StepExecution stepExecution) {
		org.springframework.batch.core.repository.persistence.StepExecution stepExecutionToSave = this.stepExecutionConverter
			.fromStepExecution(stepExecution);
		long stepExecutionId = new Random().nextLong();
		stepExecutionToSave.setStepExecutionId(stepExecutionId);
		this.mongoOperations.insert(stepExecutionToSave, STEP_EXECUTIONS_COLLECTION_NAME);
		stepExecution.setId(stepExecutionId);
	}

	@Override
	public void saveStepExecutions(Collection<StepExecution> stepExecutions) {
		for (StepExecution stepExecution : stepExecutions) {
			saveStepExecution(stepExecution);
		}
	}

	@Override
	public void updateStepExecution(StepExecution stepExecution) {
		Query query = query(where("stepExecutionId").is(stepExecution.getId()));
		org.springframework.batch.core.repository.persistence.StepExecution stepExecutionToUpdate = this.stepExecutionConverter
				.fromStepExecution(stepExecution);
		this.mongoOperations.findAndReplace(query, stepExecutionToUpdate, STEP_EXECUTIONS_COLLECTION_NAME);
	}

	@Override
	public StepExecution getStepExecution(JobExecution jobExecution, Long stepExecutionId) {
		org.springframework.batch.core.repository.persistence.StepExecution stepExecution = this.mongoOperations
			.findById(stepExecutionId, org.springframework.batch.core.repository.persistence.StepExecution.class,
					STEP_EXECUTIONS_COLLECTION_NAME);
		return stepExecution != null ? this.stepExecutionConverter.toStepExecution(stepExecution, jobExecution) : null;
	}

	@Override
	public StepExecution getLastStepExecution(JobInstance jobInstance, String stepName) {
		// TODO optimize the query
		// get all step executions
		List<org.springframework.batch.core.repository.persistence.StepExecution> stepExecutions = new ArrayList<>();
		Query query = query(where("jobInstanceId").is(jobInstance.getId()));
		List<org.springframework.batch.core.repository.persistence.JobExecution> jobExecutions = this.mongoOperations
			.find(query, org.springframework.batch.core.repository.persistence.JobExecution.class,
					JOB_EXECUTIONS_COLLECTION_NAME);
		for (org.springframework.batch.core.repository.persistence.JobExecution jobExecution : jobExecutions) {
			stepExecutions.addAll(jobExecution.getStepExecutions());
		}
		// sort step executions by creation date then id (see contract) and return the
		// first one
		Optional<org.springframework.batch.core.repository.persistence.StepExecution> lastStepExecution = stepExecutions
			.stream()
			.min(Comparator
				.comparing(org.springframework.batch.core.repository.persistence.StepExecution::getCreateTime)
				.thenComparing(org.springframework.batch.core.repository.persistence.StepExecution::getId));
		if (lastStepExecution.isPresent()) {
			org.springframework.batch.core.repository.persistence.StepExecution stepExecution = lastStepExecution.get();
			JobExecution jobExecution = this.jobExecutionConverter.toJobExecution(jobExecutions.stream()
				.filter(execution -> execution.getJobExecutionId().equals(stepExecution.getJobExecutionId()))
				.findFirst()
				.get(), jobInstance);
			return this.stepExecutionConverter.toStepExecution(stepExecution, jobExecution);
		}
		else {
			return null;
		}
	}

	@Override
	public void addStepExecutions(JobExecution jobExecution) {
		Query query = query(where("jobExecutionId").is(jobExecution.getId()));
		List<StepExecution> stepExecutions = this.mongoOperations
			.find(query, org.springframework.batch.core.repository.persistence.StepExecution.class,
					STEP_EXECUTIONS_COLLECTION_NAME)
			.stream()
			.map(stepExecution -> this.stepExecutionConverter.toStepExecution(stepExecution, jobExecution))
			.toList();
		jobExecution.addStepExecutions(stepExecutions);
	}

	@Override
	public long countStepExecutions(JobInstance jobInstance, String stepName) {
		long count = 0;
		// TODO optimize the count query
		Query query = query(where("jobInstanceId").is(jobInstance.getId()));
		List<org.springframework.batch.core.repository.persistence.JobExecution> jobExecutions = this.mongoOperations
			.find(query, org.springframework.batch.core.repository.persistence.JobExecution.class,
					JOB_EXECUTIONS_COLLECTION_NAME);
		for (org.springframework.batch.core.repository.persistence.JobExecution jobExecution : jobExecutions) {
			List<org.springframework.batch.core.repository.persistence.StepExecution> stepExecutions = jobExecution
				.getStepExecutions();
			for (org.springframework.batch.core.repository.persistence.StepExecution stepExecution : stepExecutions) {
				if (stepExecution.getName().equals(stepName)) {
					count++;
				}
			}
		}
		return count;
	}

}
