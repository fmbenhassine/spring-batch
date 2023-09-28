package org.springframework.batch.core.repository.persistence;

public class JobInstance {

	private long id;

	private String jobName;

	private String jobKey;

	public JobInstance() {
	}

	public JobInstance(long id, String jobName, String jobKey) {
		this.id = id;
		this.jobName = jobName;
		this.jobKey = jobKey;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobKey() {
		return jobKey;
	}

	public void setJobKey(String jobKey) {
		this.jobKey = jobKey;
	}

}
