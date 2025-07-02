package br.project.financial.errors.exceptions;

import br.project.financial.errors.ExceptionCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Getter
@Setter
@Slf4j
public class BusinessRuleException extends RuntimeException {
    private final ExceptionCode code;
    private Map<String, Object> details;

    public BusinessRuleException(ExceptionCode code, Map<String, Object> details) {
        super(code.toString());
        this.code = code;
        this.details = details;
        log.error(this.getMessage(), this);
    }

    public BusinessRuleException(ExceptionCode code) {
        super(code.toString());
        this.code = code;
        this.details = null;
        log.error(this.getMessage(), this);
    }
}
