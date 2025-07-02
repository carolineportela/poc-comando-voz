package br.project.financial.errors;

import br.project.financial.enums.TransactionType;
import br.project.financial.errors.details.EntityNotFoundErrorDetails;
import br.project.financial.errors.details.MissingParameterErrorDetails;
import br.project.financial.errors.exceptions.BusinessRuleException;
import br.project.financial.errors.exceptions.EntityNotFoundException;
import br.project.financial.errors.exceptions.NoTransactionsFoundException;
import br.project.financial.errors.responses.EntityNotFoundErrorResponse;
import br.project.financial.errors.responses.MissingParameterErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> entityNotFoundExceptionHandler(EntityNotFoundException ex, WebRequest request) {
        final var body = new EntityNotFoundErrorResponse(
                new EntityNotFoundErrorDetails(
                        ex.getEntityClass().getSimpleName(),
                        ex.getParameters()
                )
        );

        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Object> handleBusinessRuleException(BusinessRuleException ex, WebRequest request) {
        var body = new ErrorResponse<>(ex.getCode(), ex.getDetails());
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }


    @ExceptionHandler(NoTransactionsFoundException.class)
    public ResponseEntity<Object> handleNoTransactionsFound(NoTransactionsFoundException ex, WebRequest request) {
        var body = new ErrorResponse<>(ex.getCode(), ex.getParameters());
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        var body = new MissingParameterErrorResponse(
                new MissingParameterErrorDetails(ex.getParameterName())
        );

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            WebRequest request
    ) {
        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()
                && ex.getRequiredType().equals(TransactionType.class)) {

            var body = new ErrorResponse<>(
                    ExceptionCode.INVALID_TRANSACTION_TYPE,
                    Map.of("invalidValue", ex.getValue())
            );

            return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
        }

        var fallback = new ErrorResponse<>(
                ExceptionCode.API_FIELDS_INVALID,
                Map.of(
                        "parameter", ex.getName(),
                        "invalidValue", ex.getValue(),
                        "expectedType", ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"
                )
        );

        return handleExceptionInternal(ex, fallback, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
