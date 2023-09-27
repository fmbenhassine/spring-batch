package org.springframework.batch.core.repository.dao;

import java.util.List;

import org.springframework.batch.core.DefaultJobKeyGenerator;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobKeyGenerator;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class MongoJobInstanceDao implements JobInstanceDao {

    private static final String COLLECTION_NAME = "BATCH_JOB_INSTANCE"; // TODO make configurable

    private final MongoOperations mongoOperations;

    private JobKeyGenerator<JobParameters> jobKeyGenerator = new DefaultJobKeyGenerator();

    public MongoJobInstanceDao(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public JobInstance createJobInstance(String jobName, JobParameters jobParameters) {
        Assert.notNull(jobName, "Job name must not be null.");
        Assert.notNull(jobParameters, "JobParameters must not be null.");

        Assert.state(getJobInstance(jobName, jobParameters) == null, "JobInstance must not already exist");

        JobInstance lastJobInstance = getLastJobInstance(jobName);
        Long jobInstanceId = lastJobInstance == null ? 1L : lastJobInstance.getId() + 1;

        JobInstance jobInstance = new JobInstance(jobInstanceId, jobName);
        jobInstance.incrementVersion(); // TODO version is volatile => not persisted. Is this needed here?

        mongoOperations.insert(jobInstance, COLLECTION_NAME);

        String key = jobKeyGenerator.generateKey(jobParameters);
        Query query = query(where("_id").is(jobInstanceId));
        Update update = Update.update("jobKey", key);
        mongoOperations.updateFirst(query, update, JobInstance.class, COLLECTION_NAME);

        return jobInstance;
    }

    @Override
    public JobInstance getJobInstance(String jobName, JobParameters jobParameters) {
        String key = jobKeyGenerator.generateKey(jobParameters);
        Query query = query(where("jobName").is(jobName).and("jobKey").is(key));
        return mongoOperations.findOne(query, JobInstance.class, COLLECTION_NAME);
    }

    @Override
    public JobInstance getJobInstance(Long instanceId) {
        return this.mongoOperations.findById(instanceId, JobInstance.class, COLLECTION_NAME);
    }

    @Override
    public JobInstance getJobInstance(JobExecution jobExecution) {
        return getJobInstance(jobExecution.getJobId());
    }

    @Override
    public List<JobInstance> getJobInstances(String jobName, int start, int count) {
        Query query = query(where("jobName").is(jobName));
        Sort.Order sortOrder = Sort.Order.desc("_id");
        List<JobInstance> jobInstances = mongoOperations.find(query.with(Sort.by(sortOrder)), JobInstance.class, COLLECTION_NAME).stream().toList();
        return jobInstances.subList(start, jobInstances.size()).stream().limit(count).toList();
    }

    @Override
    public JobInstance getLastJobInstance(String jobName) {
        Query query = query(where("jobName").is(jobName));
        Sort.Order sortOrder = Sort.Order.desc("_id");
        return mongoOperations.findOne(query.with(Sort.by(sortOrder)), JobInstance.class, COLLECTION_NAME);
    }

    @Override
    public List<String> getJobNames() {
        return mongoOperations.findAll(JobInstance.class, COLLECTION_NAME)
                .stream()
                .map(JobInstance::getJobName)
                .toList();
    }

    @Override
    public List<JobInstance> findJobInstancesByName(String jobName, int start, int count) {
        Query query = query(where("jobName").alike(Example.of(jobName)));
        Sort.Order sortOrder = Sort.Order.desc("_id");
        List<JobInstance> jobInstances = mongoOperations.find(query.with(Sort.by(sortOrder)), JobInstance.class, COLLECTION_NAME).stream().toList();
        return jobInstances.subList(start, jobInstances.size()).stream().limit(count).toList();
    }

    @Override
    public long getJobInstanceCount(String jobName) throws NoSuchJobException {
        if (getJobNames().contains(jobName)) {
            throw new NoSuchJobException("Job not found " + jobName);
        }
        Query query = query(where("jobName").is(jobName));
        return this.mongoOperations.count(query, COLLECTION_NAME);
    }

    public void setJobKeyGenerator(JobKeyGenerator<JobParameters> jobKeyGenerator) {
        this.jobKeyGenerator = jobKeyGenerator;
    }
}
