package org.springframework.batch.core.repository.persistence;

import java.time.LocalDateTime;

import org.springframework.batch.core.BatchStatus;

public class StepExecution {

	private long id;

	private long jobExecutionId;

	private String name;

	private BatchStatus status;

	private long readCount;

	private long writeCount;

	private long commitCount;

	private long rollbackCount;

	private long readSkipCount;

	private long processSkipCount;

	private long writeSkipCount;

	private long filterCount;

	private LocalDateTime startTime;

	private LocalDateTime createTime;

	private LocalDateTime endTime;

	private LocalDateTime lastUpdated;

	private ExecutionContext executionContext;

	private ExitStatus exitStatus;

	private boolean terminateOnly;

	public StepExecution() {
	}

	public StepExecution(long id, long jobExecutionId, String name, BatchStatus status, long readCount, long writeCount,
			long commitCount, long rollbackCount, long readSkipCount, long processSkipCount, long writeSkipCount,
			long filterCount, LocalDateTime startTime, LocalDateTime createTime, LocalDateTime endTime,
			LocalDateTime lastUpdated, ExecutionContext executionContext, ExitStatus exitStatus,
			boolean terminateOnly) {
		this.id = id;
		this.jobExecutionId = jobExecutionId;
		this.name = name;
		this.status = status;
		this.readCount = readCount;
		this.writeCount = writeCount;
		this.commitCount = commitCount;
		this.rollbackCount = rollbackCount;
		this.readSkipCount = readSkipCount;
		this.processSkipCount = processSkipCount;
		this.writeSkipCount = writeSkipCount;
		this.filterCount = filterCount;
		this.startTime = startTime;
		this.createTime = createTime;
		this.endTime = endTime;
		this.lastUpdated = lastUpdated;
		this.executionContext = executionContext;
		this.exitStatus = exitStatus;
		this.terminateOnly = terminateOnly;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getJobExecutionId() {
		return jobExecutionId;
	}

	public void setJobExecutionId(long jobExecutionId) {
		this.jobExecutionId = jobExecutionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BatchStatus getStatus() {
		return status;
	}

	public void setStatus(BatchStatus status) {
		this.status = status;
	}

	public long getReadCount() {
		return readCount;
	}

	public void setReadCount(long readCount) {
		this.readCount = readCount;
	}

	public long getWriteCount() {
		return writeCount;
	}

	public void setWriteCount(long writeCount) {
		this.writeCount = writeCount;
	}

	public long getCommitCount() {
		return commitCount;
	}

	public void setCommitCount(long commitCount) {
		this.commitCount = commitCount;
	}

	public long getRollbackCount() {
		return rollbackCount;
	}

	public void setRollbackCount(long rollbackCount) {
		this.rollbackCount = rollbackCount;
	}

	public long getReadSkipCount() {
		return readSkipCount;
	}

	public void setReadSkipCount(long readSkipCount) {
		this.readSkipCount = readSkipCount;
	}

	public long getProcessSkipCount() {
		return processSkipCount;
	}

	public void setProcessSkipCount(long processSkipCount) {
		this.processSkipCount = processSkipCount;
	}

	public long getWriteSkipCount() {
		return writeSkipCount;
	}

	public void setWriteSkipCount(long writeSkipCount) {
		this.writeSkipCount = writeSkipCount;
	}

	public long getFilterCount() {
		return filterCount;
	}

	public void setFilterCount(long filterCount) {
		this.filterCount = filterCount;
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

	public ExecutionContext getExecutionContext() {
		return executionContext;
	}

	public void setExecutionContext(ExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

	public ExitStatus getExitStatus() {
		return exitStatus;
	}

	public void setExitStatus(ExitStatus exitStatus) {
		this.exitStatus = exitStatus;
	}

	public boolean isTerminateOnly() {
		return terminateOnly;
	}

	public void setTerminateOnly(boolean terminateOnly) {
		this.terminateOnly = terminateOnly;
	}

}
