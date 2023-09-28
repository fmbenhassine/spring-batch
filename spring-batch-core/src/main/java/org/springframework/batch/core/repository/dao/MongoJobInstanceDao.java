package org.springframework.batch.core.repository.dao;

import java.util.List;

import org.springframework.batch.core.DefaultJobKeyGenerator;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobKeyGenerator;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.persistence.converter.JobInstanceConverter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class MongoJobInstanceDao implements JobInstanceDao {

	private static final String COLLECTION_NAME = "BATCH_JOB_INSTANCE"; // TODO make
																		// configurable

	private final MongoOperations mongoOperations;

	private JobKeyGenerator<JobParameters> jobKeyGenerator = new DefaultJobKeyGenerator();

	private final JobInstanceConverter jobInstanceConverter = new JobInstanceConverter();

	public MongoJobInstanceDao(MongoOperations mongoOperations) {
		Assert.notNull(mongoOperations, "mongoOperations must not be null.");
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
		jobInstance.incrementVersion(); // TODO is this needed?

		org.springframework.batch.core.repository.persistence.JobInstance jobInstanceToSave = this.jobInstanceConverter
			.fromJobInstance(jobInstance);
		String key = this.jobKeyGenerator.generateKey(jobParameters);
		jobInstanceToSave.setJobKey(key);
		this.mongoOperations.insert(jobInstanceToSave, COLLECTION_NAME);

		return jobInstance;
	}

	@Override
	public JobInstance getJobInstance(String jobName, JobParameters jobParameters) {
		String key = this.jobKeyGenerator.generateKey(jobParameters);
		Query query = query(where("jobName").is(jobName).and("jobKey").is(key));
		org.springframework.batch.core.repository.persistence.JobInstance jobInstance = this.mongoOperations
			.findOne(query, org.springframework.batch.core.repository.persistence.JobInstance.class, COLLECTION_NAME);
		return jobInstance != null ? this.jobInstanceConverter.toJobInstance(jobInstance) : null;
	}

	@Override
	public JobInstance getJobInstance(Long instanceId) {
		org.springframework.batch.core.repository.persistence.JobInstance jobInstance = this.mongoOperations.findById(
				instanceId, org.springframework.batch.core.repository.persistence.JobInstance.class, COLLECTION_NAME);
		return jobInstance != null ? this.jobInstanceConverter.toJobInstance(jobInstance) : null;
	}

	@Override
	public JobInstance getJobInstance(JobExecution jobExecution) {
		return getJobInstance(jobExecution.getJobId());
	}

	@Override
	public List<JobInstance> getJobInstances(String jobName, int start, int count) {
		Query query = query(where("jobName").is(jobName));
		Sort.Order sortOrder = Sort.Order.desc("_id");
		List<org.springframework.batch.core.repository.persistence.JobInstance> jobInstances = this.mongoOperations
			.find(query.with(Sort.by(sortOrder)),
					org.springframework.batch.core.repository.persistence.JobInstance.class, COLLECTION_NAME)
			.stream()
			.toList();
		return jobInstances.subList(start, jobInstances.size())
			.stream()
			.map(this.jobInstanceConverter::toJobInstance)
			.limit(count)
			.toList();
	}

	@Override
	public JobInstance getLastJobInstance(String jobName) {
		Query query = query(where("jobName").is(jobName));
		Sort.Order sortOrder = Sort.Order.desc("_id");
		org.springframework.batch.core.repository.persistence.JobInstance jobInstance = this.mongoOperations.findOne(
				query.with(Sort.by(sortOrder)), org.springframework.batch.core.repository.persistence.JobInstance.class,
				COLLECTION_NAME);
		return jobInstance != null ? this.jobInstanceConverter.toJobInstance(jobInstance) : null;
	}

	@Override
	public List<String> getJobNames() {
		return this.mongoOperations
			.findAll(org.springframework.batch.core.repository.persistence.JobInstance.class, COLLECTION_NAME)
			.stream()
			.map(org.springframework.batch.core.repository.persistence.JobInstance::getJobName)
			.toList();
	}

	@Override
	public List<JobInstance> findJobInstancesByName(String jobName, int start, int count) {
		Query query = query(where("jobName").alike(Example.of(jobName)));
		Sort.Order sortOrder = Sort.Order.desc("_id");
		List<org.springframework.batch.core.repository.persistence.JobInstance> jobInstances = this.mongoOperations
			.find(query.with(Sort.by(sortOrder)),
					org.springframework.batch.core.repository.persistence.JobInstance.class, COLLECTION_NAME)
			.stream()
			.toList();
		return jobInstances.subList(start, jobInstances.size())
			.stream()
			.map(this.jobInstanceConverter::toJobInstance)
			.limit(count)
			.toList();
	}

	@Override
	public long getJobInstanceCount(String jobName) throws NoSuchJobException {
		if (!getJobNames().contains(jobName)) {
			throw new NoSuchJobException("Job not found " + jobName);
		}
		Query query = query(where("jobName").is(jobName));
		return this.mongoOperations.count(query, COLLECTION_NAME);
	}

	public void setJobKeyGenerator(JobKeyGenerator<JobParameters> jobKeyGenerator) {
		this.jobKeyGenerator = jobKeyGenerator;
	}

}
