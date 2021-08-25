package org.springframework.batch.core.repository.dao;

import java.util.Collection;

import com.mongodb.client.MongoClient;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoExecutionContextDao implements ExecutionContextDao {

    private MongoOperations mongoOperations;

    public MongoExecutionContextDao(MongoClient mongoClient, String databaseName) {
        this.mongoOperations = new MongoTemplate(mongoClient, databaseName);
    }

    @Override
    public ExecutionContext getExecutionContext(JobExecution jobExecution) {
        return null;
    }

    @Override
    public ExecutionContext getExecutionContext(StepExecution stepExecution) {
        return null;
    }

    @Override
    public void saveExecutionContext(JobExecution jobExecution) {

    }

    @Override
    public void saveExecutionContext(StepExecution stepExecution) {

    }

    @Override
    public void saveExecutionContexts(Collection<StepExecution> stepExecutions) {

    }

    @Override
    public void updateExecutionContext(JobExecution jobExecution) {

    }

    @Override
    public void updateExecutionContext(StepExecution stepExecution) {

    }
}
