package br.project.financial.usecases.transaction;

import br.project.financial.dtos.transaction.output.TransactionRevenueOutputDTO;
import br.project.financial.enums.TransactionType;
import br.project.financial.errors.ExceptionCode;
import br.project.financial.errors.exceptions.BusinessRuleException;
import br.project.financial.errors.exceptions.NoTransactionsFoundException;
import br.project.financial.repositories.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
public class CalculateRevenueByTypeAndDateUseCase {

    private final TransactionRepository repository;

    public CalculateRevenueByTypeAndDateUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public TransactionRevenueOutputDTO execute(
            TransactionType transactionType,
            LocalDate date
    ) {

        if (transactionType == null) {
            throw new BusinessRuleException(
                    ExceptionCode.INVALID_TRANSACTION_TYPE,
                    Map.of("transactionType", "must be provided")
            );
        }


        if (date == null) {
            throw new BusinessRuleException(
                    ExceptionCode.INVALID_PERIOD,
                    Map.of("date", "must be provided")
            );
        }
        TransactionRevenueOutputDTO result =
                repository.sumByTypeAndPeriod(transactionType, date, date);

        if (result == null
                || result.getTotal() == null
                || result.getTotal().compareTo(BigDecimal.ZERO) == 0
        ) {
            throw new NoTransactionsFoundException(Map.of(
                    "transactionType", transactionType.name(),
                    "date", date.toString()
            ));
        }

        return result;
    }
}
