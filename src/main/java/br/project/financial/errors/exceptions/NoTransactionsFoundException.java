package br.project.financial.errors.exceptions;

import br.project.financial.errors.ExceptionCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Getter
@Slf4j
public class NoTransactionsFoundException extends RuntimeException {
    private final ExceptionCode code = ExceptionCode.NO_TRANSACTIONS_FOUND;
    private final Map<String, Object> parameters;

    public NoTransactionsFoundException(Map<String, Object> parameters) {
        super(ExceptionCode.NO_TRANSACTIONS_FOUND.toString());
        this.parameters = parameters;
        log.error(this.getMessage(), this);
    }
}
