package br.project.financial.usecases.transaction;

import br.project.financial.dtos.transaction.output.TotalAmountOutputDTO;
import br.project.financial.errors.exceptions.BusinessRuleException;
import br.project.financial.errors.exceptions.NoTransactionsFoundException;
import br.project.financial.errors.ExceptionCode;
import br.project.financial.enums.TransactionType;
import br.project.financial.repositories.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CalculateExpensesByCenterUseCase {

    private final TransactionRepository repository;

    public TotalAmountOutputDTO execute(
            LocalDate start,
            LocalDate end,
            String expensePurposeName,
            String costCenterName
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
        if (costCenterName == null || costCenterName.isBlank()) {
            throw new BusinessRuleException(ExceptionCode.INVALID_FILTER,
                    Map.of("costCenter", "must be provided"));
        }


        TotalAmountOutputDTO result = repository.sumExpensesByPurposeAndCenterAndPeriod(
                TransactionType.DESPESA,
                expensePurposeName,
                costCenterName,
                start,
                end
        );


        if (result == null
                || result.getTotal() == null
                || new BigDecimal(result.getTotal()).compareTo(BigDecimal.ZERO) == 0) {
            throw new NoTransactionsFoundException(Map.of(
                    "type", TransactionType.DESPESA.name(),
                    "purpose", expensePurposeName,
                    "costCenter", costCenterName,
                    "startDate", start.toString(),
                    "endDate", end.toString()
            ));
        }

        return result;
    }
}
