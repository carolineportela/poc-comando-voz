package br.project.financial.rest.specs.commons.response.error;

import br.project.financial.errors.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
        responseCode = "422",
        description = "Invalid period – start date must be before or equal to end date",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                        summary = "Invalid date period",
                        value = "{\n" +
                                "  \"error\": \"API_INVALID_PERIOD\",\n" +
                                "  \"details\": {}\n" +
                                "}"
                )
        )
)
public @interface ApiResponseInvalidPeriod {}
