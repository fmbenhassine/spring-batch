package org.springframework.batch.core.repository.dao;

import java.util.Collection;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.persistence.converter.JobExecutionConverter;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class MongoExecutionContextDao implements ExecutionContextDao {

	private static final String STEP_EXECUTIONS_COLLECTION_NAME = "BATCH_STEP_EXECUTION"; // TODO
																							// make
																							// configurable

	private static final String JOB_EXECUTIONS_COLLECTION_NAME = "BATCH_JOB_EXECUTION"; // TODO
																						// make
																						// configurable

	private final JobExecutionConverter jobExecutionConverter = new JobExecutionConverter();

	private final MongoOperations mongoOperations;

	public MongoExecutionContextDao(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	@Override
	public ExecutionContext getExecutionContext(JobExecution jobExecution) {
		org.springframework.batch.core.repository.persistence.JobExecution execution = this.mongoOperations.findById(
				jobExecution.getId(), org.springframework.batch.core.repository.persistence.JobExecution.class,
				JOB_EXECUTIONS_COLLECTION_NAME);
		if (execution == null) {
			return new ExecutionContext();
		}
		return new ExecutionContext(execution.getExecutionContext().map());
	}

	@Override
	public ExecutionContext getExecutionContext(StepExecution stepExecution) {
		org.springframework.batch.core.repository.persistence.StepExecution execution = this.mongoOperations.findById(
				stepExecution.getId(), org.springframework.batch.core.repository.persistence.StepExecution.class,
				STEP_EXECUTIONS_COLLECTION_NAME);
		if (execution == null) {
			return new ExecutionContext();
		}
		return new ExecutionContext(execution.getExecutionContext().map());
	}

	@Override
	public void saveExecutionContext(JobExecution jobExecution) {
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		Query query = query(where("_id").is(jobExecution.getId()));
		Update update = Update.update("executionContext",
				new org.springframework.batch.core.repository.persistence.ExecutionContext(executionContext.toMap(),
						executionContext.isDirty()));
		this.mongoOperations.updateFirst(query, update,
				org.springframework.batch.core.repository.persistence.JobExecution.class,
				JOB_EXECUTIONS_COLLECTION_NAME);
	}

	@Override
	public void saveExecutionContext(StepExecution stepExecution) {
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		Query query = query(where("_id").is(stepExecution.getId()));
		Update update = Update.update("executionContext",
				new org.springframework.batch.core.repository.persistence.ExecutionContext(executionContext.toMap(),
						executionContext.isDirty()));
		this.mongoOperations.updateFirst(query, update,
				org.springframework.batch.core.repository.persistence.StepExecution.class,
				STEP_EXECUTIONS_COLLECTION_NAME);

	}

	@Override
	public void saveExecutionContexts(Collection<StepExecution> stepExecutions) {
		for (StepExecution stepExecution : stepExecutions) {
			saveExecutionContext(stepExecution);
		}
	}

	@Override
	public void updateExecutionContext(JobExecution jobExecution) {
		saveExecutionContext(jobExecution);
	}

	@Override
	public void updateExecutionContext(StepExecution stepExecution) {
		saveExecutionContext(stepExecution);
	}

}
