package org.springframework.batch.core.repository.dao;

import java.util.Arrays;
import java.util.List;

import com.mongodb.client.MongoClient;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class MongoJobInstanceDao implements JobInstanceDao {

    private final MongoOperations mongoOperations;

    public MongoJobInstanceDao(MongoClient mongoClient, String databaseName) {
        this.mongoOperations = new MongoTemplate(mongoClient, databaseName);
    }

    @Override
    public JobInstance createJobInstance(String jobName, JobParameters jobParameters) {
        return null;
    }

    @Override
    public JobInstance getJobInstance(String jobName, JobParameters jobParameters) {
        return null;
    }

    @Override
    public JobInstance getJobInstance(Long instanceId) {
        return this.mongoOperations.findById(instanceId, JobInstance.class);
    }

    @Override
    public JobInstance getJobInstance(JobExecution jobExecution) {
        return getJobInstance(jobExecution.getJobId());
    }

    @Override
    public List<JobInstance> getJobInstances(String jobName, int start, int count) {
        return null;
    }

    @Override
    public JobInstance getLastJobInstance(String jobName) {
        return null;
    }

    @Override
    public List<String> getJobNames() {
        return null;
    }

    @Override
    public List<JobInstance> findJobInstancesByName(String jobName, int start, int count) {
        return null;
    }

    @Override
    public int getJobInstanceCount(String jobName) throws NoSuchJobException {
        Query query = Query.query(Criteria.where(""));
        // FIXME change org.springframework.batch.core.repository.dao.JobInstanceDao.getJobInstanceCount to return long
        return (int) this.mongoOperations.count(query, "BATCH_JOB_INSTANCE");
    }
}
