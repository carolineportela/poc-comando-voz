package br.project.financial.errors.exceptions;

import br.project.financial.errors.ExceptionCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Getter
@Slf4j
public class EntityNotFoundException extends RuntimeException {
    private final ExceptionCode code = ExceptionCode.ENTITY_NOT_FOUND;
    private final Class<?> entityClass;
    private final Map<String, Object> parameters;

    public EntityNotFoundException(Class<?> entityClass) {
        super(ExceptionCode.ENTITY_NOT_FOUND.toString());
        this.entityClass = entityClass;
        this.parameters = null;
        log.error(this.getMessage(), this);
    }

    public EntityNotFoundException(Class<?> entityClass, Map<String, Object> parameters) {
        super(ExceptionCode.ENTITY_NOT_FOUND.toString());
        this.entityClass = entityClass;
        this.parameters = parameters;
        log.error(this.getMessage(), this);
    }
}
