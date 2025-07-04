package br.project.financial.usecases.transaction;

import br.project.financial.dtos.transaction.output.TransactionRevenueOutputDTO;
import br.project.financial.enums.TransactionType;
import br.project.financial.errors.ExceptionCode;
import br.project.financial.errors.exceptions.BusinessRuleException;
import br.project.financial.errors.exceptions.NoTransactionsFoundException;
import br.project.financial.repositories.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class CalculateRevenueByTypeAndPeriodUseCase {

    private final TransactionRepository repository;

    public CalculateRevenueByTypeAndPeriodUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public TransactionRevenueOutputDTO execute(
            TransactionType transactionType,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (transactionType == null) {
            throw new BusinessRuleException(
                    ExceptionCode.INVALID_TRANSACTION_TYPE,
                    Map.of("transactionType", "must be provided")
            );
        }

        if (startDate == null) {
            throw new BusinessRuleException(
                    ExceptionCode.INVALID_PERIOD,
                    Map.of("startDate", "must be provided")
            );
        }
        if (endDate == null) {
            throw new BusinessRuleException(
                    ExceptionCode.INVALID_PERIOD,
                    Map.of("endDate", "must be provided")
            );
        }
        if (startDate.isAfter(endDate)) {
            throw new BusinessRuleException(
                    ExceptionCode.INVALID_PERIOD,
                    Map.of("startDate", "must be before or equal to endDate")
            );
        }

        TransactionRevenueOutputDTO result =
                repository.sumByTypeAndPeriod(transactionType, startDate, endDate);

        if (result == null || result.getTotal() == null) {
            throw new NoTransactionsFoundException(Map.of(
                    "transactionType", transactionType.name(),
                    "startDate", startDate.toString(),
                    "endDate", endDate.toString()
            ));
        }

        return result;
    }
}
