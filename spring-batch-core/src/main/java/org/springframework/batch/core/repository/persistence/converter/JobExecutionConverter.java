package org.springframework.batch.core.repository.persistence.converter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.repository.persistence.ExecutionContext;
import org.springframework.batch.core.repository.persistence.ExitStatus;
import org.springframework.batch.core.repository.persistence.JobExecution;
import org.springframework.batch.core.repository.persistence.JobParameter;

public class JobExecutionConverter {

	private final JobParameterConverter jobParameterConverter = new JobParameterConverter();

	private final StepExecutionConverter stepExecutionConverter = new StepExecutionConverter();

	public org.springframework.batch.core.JobExecution toJobExecution(JobExecution source, JobInstance jobInstance) {
		Map<String, org.springframework.batch.core.JobParameter<?>> parameterMap = new HashMap<>();
		source.getJobParameters()
			.forEach((key, value) -> parameterMap.put(key, this.jobParameterConverter.toJobParameter(value)));
		org.springframework.batch.core.JobExecution jobExecution = new org.springframework.batch.core.JobExecution(
				jobInstance, source.getId(), new JobParameters(parameterMap));
		jobExecution.addStepExecutions(source.getStepExecutions()
			.stream()
			.map(stepExecution -> this.stepExecutionConverter.toStepExecution(stepExecution, jobExecution))
			.toList());
		jobExecution.setStatus(source.getStatus());
		jobExecution.setStartTime(source.getStartTime());
		jobExecution.setCreateTime(source.getCreateTime());
		jobExecution.setEndTime(source.getEndTime());
		jobExecution.setLastUpdated(source.getLastUpdated());
		jobExecution.setExitStatus(new org.springframework.batch.core.ExitStatus(source.getExitStatus().exitCode(),
				source.getExitStatus().exitDescription()));
		jobExecution.setExecutionContext(
				new org.springframework.batch.item.ExecutionContext(source.getExecutionContext().map()));
		return jobExecution;
	}

	public JobExecution fromJobExecution(org.springframework.batch.core.JobExecution source) {
		JobExecution jobExecution = new JobExecution();
		jobExecution.setId(source.getId());
		jobExecution.setJobInstanceId(source.getJobInstance().getInstanceId());
		Map<String, JobParameter<?>> parameterMap = new HashMap<>();
		source.getJobParameters()
			.getParameters()
			.forEach((key, value) -> parameterMap.put(key, this.jobParameterConverter.fromJobParameter(value)));
		jobExecution.setJobParameters(parameterMap);
		jobExecution.setStepExecutions(
				source.getStepExecutions().stream().map(this.stepExecutionConverter::fromStepExecution).toList());
		jobExecution.setStatus(source.getStatus());
		jobExecution.setStartTime(source.getStartTime());
		jobExecution.setCreateTime(source.getCreateTime());
		jobExecution.setEndTime(source.getEndTime());
		jobExecution.setLastUpdated(source.getLastUpdated());
		jobExecution.setExitStatus(
				new ExitStatus(source.getExitStatus().getExitCode(), source.getExitStatus().getExitDescription()));
		jobExecution.setExecutionContext(
				new ExecutionContext(source.getExecutionContext().toMap(), source.getExecutionContext().isDirty()));
		return jobExecution;
	}

}
