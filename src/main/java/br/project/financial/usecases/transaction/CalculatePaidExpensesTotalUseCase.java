package br.project.financial.usecases.transaction;

import br.project.financial.dtos.transaction.output.TotalAmountOutputDTO;
import br.project.financial.errors.exceptions.BusinessRuleException;
import br.project.financial.errors.exceptions.NoTransactionsFoundException;
import br.project.financial.errors.ExceptionCode;
import br.project.financial.repositories.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CalculatePaidExpensesTotalUseCase {

    private final TransactionRepository repository;

    public TotalAmountOutputDTO execute(LocalDate start, LocalDate end, String branch) {
        if (start == null || end == null) {
            throw new BusinessRuleException(ExceptionCode.INVALID_PERIOD,
                    Map.of("startDate/endDate", "must be provided"));
        }

        if (start.isAfter(end)) {
            throw new BusinessRuleException(ExceptionCode.INVALID_PERIOD,
                    Map.of("startDate", "must be before or equal to endDate"));
        }

        TotalAmountOutputDTO result;

        if (branch != null && !branch.isBlank()) {
            result = repository.sumPaidExpensesBetweenDatesAndBranch(start, end, branch);
        } else {
            result = repository.sumPaidExpensesBetweenDates(start, end);
        }

        if (result == null || result.getTotal() == null ||
                new BigDecimal(result.getTotal()).compareTo(BigDecimal.ZERO) == 0) {
            throw new NoTransactionsFoundException(Map.of(
                    "type", "DESPESA",
                    "paymentStatus", "PAGA",
                    "startDate", start.toString(),
                    "endDate", end.toString(),
                    "branch", branch != null ? branch : "ALL"
            ));
        }

        return result;
    }
}
