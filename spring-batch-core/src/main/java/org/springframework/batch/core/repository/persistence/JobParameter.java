package org.springframework.batch.core.repository.persistence;

public record JobParameter<T>(T value, String type, boolean identifying) {
}
