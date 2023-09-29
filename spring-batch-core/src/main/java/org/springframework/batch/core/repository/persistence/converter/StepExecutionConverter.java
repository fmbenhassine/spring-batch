package org.springframework.batch.core.repository.persistence.converter;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.repository.persistence.ExecutionContext;
import org.springframework.batch.core.repository.persistence.ExitStatus;
import org.springframework.batch.core.repository.persistence.StepExecution;

public class StepExecutionConverter {

	public org.springframework.batch.core.StepExecution toStepExecution(StepExecution source,
			JobExecution jobExecution) {
		org.springframework.batch.core.StepExecution stepExecution = new org.springframework.batch.core.StepExecution(
				source.getName(), jobExecution, source.getId());
		stepExecution.setStatus(source.getStatus());
		stepExecution.setReadCount(source.getReadCount());
		stepExecution.setWriteCount(source.getWriteCount());
		stepExecution.setCommitCount(source.getCommitCount());
		stepExecution.setRollbackCount(source.getRollbackCount());
		stepExecution.setReadSkipCount(source.getReadSkipCount());
		stepExecution.setProcessSkipCount(source.getProcessSkipCount());
		stepExecution.setWriteSkipCount(source.getWriteSkipCount());
		stepExecution.setFilterCount(source.getFilterCount());
		stepExecution.setStartTime(source.getStartTime());
		stepExecution.setCreateTime(source.getCreateTime());
		stepExecution.setEndTime(source.getEndTime());
		stepExecution.setLastUpdated(source.getLastUpdated());
		stepExecution.setExitStatus(new org.springframework.batch.core.ExitStatus(source.getExitStatus().exitCode(),
				source.getExitStatus().exitDescription()));
		stepExecution.setExecutionContext(
				new org.springframework.batch.item.ExecutionContext(source.getExecutionContext().map()));
		if (source.isTerminateOnly()) {
			stepExecution.setTerminateOnly();
		}
		return stepExecution;
	}

	public StepExecution fromStepExecution(org.springframework.batch.core.StepExecution source) {
		StepExecution stepExecution = new StepExecution();
		if (source.getId() != null) {
			stepExecution.setId(source.getId());
		}
		stepExecution.setJobExecutionId(source.getJobExecutionId());
		stepExecution.setStatus(source.getStatus());
		stepExecution.setReadCount(source.getReadCount());
		stepExecution.setWriteCount(source.getWriteCount());
		stepExecution.setCommitCount(source.getCommitCount());
		stepExecution.setRollbackCount(source.getRollbackCount());
		stepExecution.setReadSkipCount(source.getReadSkipCount());
		stepExecution.setProcessSkipCount(source.getProcessSkipCount());
		stepExecution.setWriteSkipCount(source.getWriteSkipCount());
		stepExecution.setFilterCount(source.getFilterCount());
		stepExecution.setStartTime(source.getStartTime());
		stepExecution.setCreateTime(source.getCreateTime());
		stepExecution.setEndTime(source.getEndTime());
		stepExecution.setLastUpdated(source.getLastUpdated());
		stepExecution.setExitStatus(
				new ExitStatus(source.getExitStatus().getExitCode(), source.getExitStatus().getExitDescription()));
		stepExecution.setExecutionContext(
				new ExecutionContext(source.getExecutionContext().toMap(), source.getExecutionContext().isDirty()));
		stepExecution.setTerminateOnly(source.isTerminateOnly());
		return stepExecution;
	}

}
