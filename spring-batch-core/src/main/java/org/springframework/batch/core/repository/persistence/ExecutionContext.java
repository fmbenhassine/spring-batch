package org.springframework.batch.core.repository.persistence;

import java.util.Map;

public record ExecutionContext(Map<String, Object> map, boolean dirty) {
}
