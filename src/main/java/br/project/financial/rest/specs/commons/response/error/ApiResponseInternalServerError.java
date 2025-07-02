package br.project.financial.rest.specs.commons.response.error;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

@ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content = @Content
)
public @interface ApiResponseInternalServerError {}
