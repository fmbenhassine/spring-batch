package org.springframework.batch.core.repository.persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;

public class JobExecution {

	private long id;

	private long jobInstanceId;

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

	public JobExecution(long id, long jobInstanceId, Map<String, JobParameter<?>> jobParameters,
			List<StepExecution> stepExecutions, BatchStatus status, LocalDateTime startTime, LocalDateTime createTime,
			LocalDateTime endTime, LocalDateTime lastUpdated, ExitStatus exitStatus,
			ExecutionContext executionContext) {
		this.id = id;
		this.jobInstanceId = jobInstanceId;
		this.jobParameters = jobParameters;
		this.stepExecutions = stepExecutions;
		this.status = status;
		this.startTime = startTime;
		this.createTime = createTime;
		this.endTime = endTime;
		this.lastUpdated = lastUpdated;
		this.exitStatus = exitStatus;
		this.executionContext = executionContext;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getJobInstanceId() {
		return jobInstanceId;
	}

	public void setJobInstanceId(long jobInstanceId) {
		this.jobInstanceId = jobInstanceId;
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

}
