package br.project.financial.usecases.transaction;

import br.project.financial.dtos.transaction.output.FilteredTotalOutputDTO;
import br.project.financial.enums.TransactionType;
import br.project.financial.errors.ExceptionCode;
import br.project.financial.errors.exceptions.BusinessRuleException;
import br.project.financial.errors.exceptions.NoTransactionsFoundException;
import br.project.financial.repositories.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CalculateFilteredTotalUseCase {

    private final TransactionRepository repository;

    public FilteredTotalOutputDTO execute(
            TransactionType type,
            String expensePurposeName,
            String categoryName,
            String costCenterName,
            LocalDate start,
            LocalDate end
    ) {
        if (start == null || end == null) {
            throw new BusinessRuleException(ExceptionCode.INVALID_PERIOD,
                    Map.of("startDate/endDate", "must be provided"));
        }
        if (start.isAfter(end)) {
            throw new BusinessRuleException(ExceptionCode.INVALID_PERIOD,
                    Map.of("startDate", "must be before or equal to endDate"));
        }
        if (expensePurposeName == null || expensePurposeName.isBlank()) {
            throw new BusinessRuleException(ExceptionCode.INVALID_FILTER,
                    Map.of("purpose", "must be provided"));
        }
        if (categoryName == null || categoryName.isBlank()) {
            throw new BusinessRuleException(ExceptionCode.INVALID_FILTER,
                    Map.of("category", "must be provided"));
        }
        if (costCenterName == null || costCenterName.isBlank()) {
            throw new BusinessRuleException(ExceptionCode.INVALID_FILTER,
                    Map.of("costCenter", "must be provided"));
        }

        FilteredTotalOutputDTO result = repository.sumByTypePurposeCategoryCenterAndPeriod(
                type,
                expensePurposeName,
                categoryName,
                costCenterName,
                start,
                end
        );

        if (result == null
                || result.getTotal() == null
                || new BigDecimal(result.getTotal()).compareTo(BigDecimal.ZERO) == 0) {
            throw new NoTransactionsFoundException(Map.of(
                    "type", type.name(),
                    "purpose", expensePurposeName,
                    "category", categoryName,
                    "costCenter", costCenterName,
                    "startDate", start.toString(),
                    "endDate", end.toString()
            ));
        }
        return result;
    }
}
