package org.springframework.batch.core.repository.persistence;

public class JobInstance {

	private String id;

	private Long jobInstanceId;

	private String jobName;

	private String jobKey;

	public JobInstance() {
	}

	public String getId() {
		return id;
	}

	public Long getJobInstanceId() {
		return jobInstanceId;
	}

	public void setJobInstanceId(Long jobInstanceId) {
		this.jobInstanceId = jobInstanceId;
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

	@Override
	public String toString() {
		return "JobInstance{" +
				"id='" + id + '\'' +
				", jobInstanceId=" + jobInstanceId +
				", jobName='" + jobName + '\'' +
				", jobKey='" + jobKey + '\'' +
				'}';
	}
}
