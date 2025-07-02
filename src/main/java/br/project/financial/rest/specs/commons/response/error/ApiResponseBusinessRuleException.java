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
        description = "Business rule violation",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                        summary = "Business rule violation",
                        value = "{\n" +
                                "  \"error\": \"API_BUSINESS_RULE_EXCEPTION\",\n" +
                                "  \"details\": {}\n" +
                                "}"
                )
        )
)
public @interface ApiResponseBusinessRuleException {}
