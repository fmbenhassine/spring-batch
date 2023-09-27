package org.springframework.batch.core.repository.dao;

import java.util.Collection;

import com.mongodb.client.MongoClient;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoStepExecutionDao implements StepExecutionDao {

    private MongoOperations mongoOperations;

    public MongoStepExecutionDao(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void saveStepExecution(StepExecution stepExecution) {

    }

    @Override
    public void saveStepExecutions(Collection<StepExecution> stepExecutions) {

    }

    @Override
    public void updateStepExecution(StepExecution stepExecution) {

    }

    @Override
    public StepExecution getStepExecution(JobExecution jobExecution, Long stepExecutionId) {
        return null;
    }

    @Override
    public StepExecution getLastStepExecution(JobInstance jobInstance, String stepName) {
        return null;
    }

    @Override
    public void addStepExecutions(JobExecution jobExecution) {

    }

    @Override
    public long countStepExecutions(JobInstance jobInstance, String stepName) {
        return 0;
    }
}
