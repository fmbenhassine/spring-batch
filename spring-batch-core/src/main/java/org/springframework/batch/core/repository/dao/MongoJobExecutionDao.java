package org.springframework.batch.core.repository.dao;

import java.util.List;
import java.util.Set;

import com.mongodb.client.MongoClient;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoJobExecutionDao implements JobExecutionDao {

    private MongoOperations mongoOperations;

    public MongoJobExecutionDao(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void saveJobExecution(JobExecution jobExecution) {

    }

    @Override
    public void updateJobExecution(JobExecution jobExecution) {

    }

    @Override
    public List<JobExecution> findJobExecutions(JobInstance jobInstance) {
        return null;
    }

    @Override
    public JobExecution getLastJobExecution(JobInstance jobInstance) {
        return null;
    }

    @Override
    public Set<JobExecution> findRunningJobExecutions(String jobName) {
        return null;
    }

    @Override
    public JobExecution getJobExecution(Long executionId) {
        return null;
    }

    @Override
    public void synchronizeStatus(JobExecution jobExecution) {

    }
}
