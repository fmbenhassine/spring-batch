package org.springframework.batch.core.repository.persistence.converter;

import org.springframework.batch.core.repository.persistence.JobParameter;

public class JobParameterConverter {

	public <T> org.springframework.batch.core.JobParameter<T> toJobParameter(JobParameter<T> source) {
		try {
			return new org.springframework.batch.core.JobParameter<>(source.value(),
					(Class<T>) Class.forName(source.type()), source.identifying());
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> JobParameter<T> fromJobParameter(org.springframework.batch.core.JobParameter<T> source) {
		return new JobParameter<>(source.getValue(), source.getType().getName(), source.isIdentifying());
	}

}
