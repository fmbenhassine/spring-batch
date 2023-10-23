package org.springframework.batch.core.repository.persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;

public class JobExecution {

	private String id;

	private Long jobExecutionId;

	private Long jobInstanceId;

	private Map<String, JobParameter<?>> jobParameters = new HashMap<>();

	private List<StepExecution> stepExecutions = new ArrayList<>();

	private BatchStatus status;

	private LocalDateTime startTime;

	private LocalDateTime createTime;

	private LocalDateTime endTime;

	private LocalDateTime lastUpdated;

	private ExitStatus exitStatus;

	private ExecutionContext executionContext;

	public JobExecution() {
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

	public Long getJobExecutionId() {
		return jobExecutionId;
	}

	public void setJobExecutionId(Long jobExecutionId) {
		this.jobExecutionId = jobExecutionId;
	}

	public Map<String, JobParameter<?>> getJobParameters() {
		return jobParameters;
	}

	public void setJobParameters(Map<String, JobParameter<?>> jobParameters) {
		this.jobParameters = jobParameters;
	}

	public List<StepExecution> getStepExecutions() {
		return stepExecutions;
	}

	public void setStepExecutions(List<StepExecution> stepExecutions) {
		this.stepExecutions = stepExecutions;
	}

	public BatchStatus getStatus() {
		return status;
	}

	public void setStatus(BatchStatus status) {
		this.status = status;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public ExitStatus getExitStatus() {
		return exitStatus;
	}

	public void setExitStatus(ExitStatus exitStatus) {
		this.exitStatus = exitStatus;
	}

	public ExecutionContext getExecutionContext() {
		return executionContext;
	}

	public void setExecutionContext(ExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

	@Override
	public String toString() {
		return "JobExecution{" +
				"id='" + id + '\'' +
				", jobExecutionId=" + jobExecutionId +
				", jobInstanceId=" + jobInstanceId +
				", jobParameters=" + jobParameters +
				", stepExecutions=" + stepExecutions +
				", status=" + status +
				", startTime=" + startTime +
				", createTime=" + createTime +
				", endTime=" + endTime +
				", lastUpdated=" + lastUpdated +
				", exitStatus=" + exitStatus +
				", executionContext=" + executionContext +
				'}';
	}
}
