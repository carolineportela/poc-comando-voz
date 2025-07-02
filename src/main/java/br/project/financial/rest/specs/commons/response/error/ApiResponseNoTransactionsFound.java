package br.project.financial.rest.specs.commons.response.error;

import br.project.financial.errors.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
        responseCode = "404",
        description = "No transactions found for the given parameters",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                        summary = "No transactions found",
                        value = "{\n" +
                                "  \"error\": \"NO_TRANSACTIONS_FOUND\",\n" +
                                "  \"details\": {}\n" +
                                "}"
                )
        )
)
public @interface ApiResponseNoTransactionsFound {}
