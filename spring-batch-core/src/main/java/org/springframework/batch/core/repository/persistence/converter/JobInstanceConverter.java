package org.springframework.batch.core.repository.persistence.converter;

import org.springframework.batch.core.repository.persistence.JobInstance;

public class JobInstanceConverter {

	public org.springframework.batch.core.JobInstance toJobInstance(JobInstance source) {
		return new org.springframework.batch.core.JobInstance(source.getId(), source.getJobName());
	}

	public JobInstance fromJobInstance(org.springframework.batch.core.JobInstance source) {
		JobInstance jobInstance = new JobInstance();
		jobInstance.setId(source.getId());
		jobInstance.setJobName(source.getJobName());
		return jobInstance;
	}

}
