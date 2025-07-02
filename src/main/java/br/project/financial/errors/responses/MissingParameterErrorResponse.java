package br.project.financial.errors.responses;

import br.project.financial.errors.ExceptionCode;
import br.project.financial.errors.ErrorResponse;
import br.project.financial.errors.details.MissingParameterErrorDetails;

public class MissingParameterErrorResponse extends ErrorResponse<MissingParameterErrorDetails> {
    public MissingParameterErrorResponse(MissingParameterErrorDetails details) {
        super(ExceptionCode.API_FIELDS_INVALID, details);
    }
}
