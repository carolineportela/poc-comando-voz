package br.project.financial.errors.responses;

import br.project.financial.errors.ExceptionCode;
import br.project.financial.errors.ErrorResponse;
import br.project.financial.errors.details.EntityNotFoundErrorDetails;

public class EntityNotFoundErrorResponse extends ErrorResponse<EntityNotFoundErrorDetails> {
    public EntityNotFoundErrorResponse(EntityNotFoundErrorDetails details) {
        super(ExceptionCode.ENTITY_NOT_FOUND, details);
    }
}
