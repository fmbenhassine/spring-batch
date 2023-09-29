package org.springframework.batch.core.repository.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.repository.persistence.converter.JobExecutionConverter;
import org.springframework.batch.core.repository.persistence.converter.JobInstanceConverter;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class MongoJobExecutionDao implements JobExecutionDao {

	private static final String JOB_EXECUTIONS_COLLECTION_NAME = "BATCH_JOB_EXECUTION"; // TODO
																						// make
																						// configurable

	private static final String JOB_INSTANCES_COLLECTION_NAME = "BATCH_JOB_INSTANCE"; // TODO
																						// make
																						// configurable

	private final MongoOperations mongoOperations;

	private final JobExecutionConverter jobExecutionConverter = new JobExecutionConverter();

	private final JobInstanceConverter jobInstanceConverter = new JobInstanceConverter();

	public MongoJobExecutionDao(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	@Override
	public void saveJobExecution(JobExecution jobExecution) {
		org.springframework.batch.core.repository.persistence.JobExecution jobExecutionToSave = this.jobExecutionConverter
			.fromJobExecution(jobExecution);
		org.springframework.batch.core.repository.persistence.JobExecution saved = this.mongoOperations.save(jobExecutionToSave, JOB_EXECUTIONS_COLLECTION_NAME);
		jobExecution.setId(saved.getId());
	}

	@Override
	public void updateJobExecution(JobExecution jobExecution) {
		saveJobExecution(jobExecution);
	}

	@Override
	public List<JobExecution> findJobExecutions(JobInstance jobInstance) {
		Query query = query(where("jobInstanceId").is(jobInstance.getId()));
		List<org.springframework.batch.core.repository.persistence.JobExecution> jobExecutions = this.mongoOperations
			.find(query, org.springframework.batch.core.repository.persistence.JobExecution.class,
					JOB_EXECUTIONS_COLLECTION_NAME);
		return jobExecutions.stream()
			.map(jobExecution -> this.jobExecutionConverter.toJobExecution(jobExecution, jobInstance))
			.toList();
	}

	@Override
	public JobExecution getLastJobExecution(JobInstance jobInstance) {
		Query query = query(where("jobInstanceId").is(jobInstance.getId()));
		Sort.Order sortOrder = Sort.Order.desc("_id");
		org.springframework.batch.core.repository.persistence.JobExecution jobExecution = this.mongoOperations.findOne(
				query.with(Sort.by(sortOrder)),
				org.springframework.batch.core.repository.persistence.JobExecution.class,
				JOB_EXECUTIONS_COLLECTION_NAME);
		return jobExecution != null ? this.jobExecutionConverter.toJobExecution(jobExecution, jobInstance) : null;
	}

	@Override
	public Set<JobExecution> findRunningJobExecutions(String jobName) {
		Query query = query(where("jobName").is(jobName));
		List<JobInstance> jobInstances = this.mongoOperations
			.find(query, org.springframework.batch.core.repository.persistence.JobInstance.class,
					JOB_INSTANCES_COLLECTION_NAME)
			.stream()
			.map(this.jobInstanceConverter::toJobInstance)
			.toList();
		Set<JobExecution> runningJobExecutions = new HashSet<>();
		for (JobInstance jobInstance : jobInstances) {
			query = query(
					where("jobInstanceId").is(jobInstance.getId()).and("status").in("STARTING", "STARTED", "STOPPING"));
			this.mongoOperations
				.find(query, org.springframework.batch.core.repository.persistence.JobExecution.class,
						JOB_EXECUTIONS_COLLECTION_NAME)
				.stream()
				.map(jobExecution -> this.jobExecutionConverter.toJobExecution(jobExecution, jobInstance))
				.forEach(runningJobExecutions::add);
		}
		return runningJobExecutions;
	}

	@Override
	public JobExecution getJobExecution(Long executionId) {
		org.springframework.batch.core.repository.persistence.JobExecution jobExecution = this.mongoOperations.findById(
				executionId, org.springframework.batch.core.repository.persistence.JobExecution.class,
				JOB_EXECUTIONS_COLLECTION_NAME);
		if (jobExecution == null) {
			return null;
		}
		org.springframework.batch.core.repository.persistence.JobInstance jobInstance = this.mongoOperations.findById(
				jobExecution.getJobInstanceId(),
				org.springframework.batch.core.repository.persistence.JobInstance.class, JOB_INSTANCES_COLLECTION_NAME);
		return this.jobExecutionConverter.toJobExecution(jobExecution,
				this.jobInstanceConverter.toJobInstance(jobInstance));
	}

	@Override
	public void synchronizeStatus(JobExecution jobExecution) {
		Query query = query(where("_id").is(jobExecution.getId()));
		Update update = Update.update("status", jobExecution.getStatus());
		// TODO the contract mentions to update the version as well. Double check if this
		// is needed as the version is not used in the tests following the call sites of
		// synchronizeStatus
		this.mongoOperations.updateFirst(query, update,
				org.springframework.batch.core.repository.persistence.JobExecution.class,
				JOB_EXECUTIONS_COLLECTION_NAME);
	}

}
