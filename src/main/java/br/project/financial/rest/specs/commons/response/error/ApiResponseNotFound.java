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
        responseCode = "404",
        description = "Resource not found",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                        summary = "Not found",
                        value = "{\n" +
                                "  \"error\": \"API_NOT_FOUND\",\n" +
                                "  \"details\": {}\n" +
                                "}"
                )
        )
)
public @interface ApiResponseNotFound {}
