package br.project.financial.errors.details;

import java.util.Map;

public record EntityNotFoundErrorDetails(
        String entity,
        Map<String, Object> parameters
) {
}
