//package br.project.financial.usecases.transaction;
//
//import br.project.financial.dtos.transaction.output.TransactionRevenueOutputDTO;
//import br.project.financial.enums.TransactionType;
//import br.project.financial.errors.ExceptionCode;
//import br.project.financial.errors.exceptions.BusinessRuleException;
//import br.project.financial.errors.exceptions.NoTransactionsFoundException;
//import br.project.financial.repositories.transaction.TransactionRepository;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.Map;
//
//@Service
//public class CalculateRevenueByTypeAndDateUseCase {
//
//    private final TransactionRepository repository;
//
//    public CalculateRevenueByTypeAndDateUseCase(TransactionRepository repository) {
//        this.repository = repository;
//    }
//
//    public TransactionRevenueOutputDTO execute(TransactionType transactionType, LocalDate date) {
//        if (transactionType == null) {
//            throw new BusinessRuleException(
//                    ExceptionCode.INVALID_TRANSACTION_TYPE,
//                    Map.of("message", "Transaction type must be provided")
//            );
//        }
//
//        if (date == null) {
//            throw new BusinessRuleException(
//                    ExceptionCode.INVALID_PERIOD,
//                    Map.of("message", "Date must be provided")
//            );
//        }
//
//        TransactionRevenueOutputDTO result = repository.sumByTypeAndPeriod(transactionType, date, date);
//
//        if (result == null || result.getTotal().compareTo(BigDecimal.ZERO) == 0
//        ) {
//            throw new NoTransactionsFoundException(Map.of(
//                    "transactionType", transactionType.name(),
//                    "date", date
//            ));
//        }
//
//        return result;
//    }
//}
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
        // 1) validação de tipo de transação
        if (transactionType == null) {
            throw new BusinessRuleException(
                    ExceptionCode.INVALID_TRANSACTION_TYPE,
                    Map.of("transactionType", "must be provided")
            );
        }

        // 2) validação de data
        if (date == null) {
            throw new BusinessRuleException(
                    ExceptionCode.INVALID_PERIOD,
                    Map.of("date", "must be provided")
            );
        }

        // 3) consulta ao repositório (usa a mesma data como início e fim)
        TransactionRevenueOutputDTO result =
                repository.sumByTypeAndPeriod(transactionType, date, date);

        // 4) se não houver resultado ou total nulo/zero, lança exceção
        if (result == null
                || result.getTotal() == null
                || result.getTotal().compareTo(BigDecimal.ZERO) == 0
        ) {
            throw new NoTransactionsFoundException(Map.of(
                    "transactionType", transactionType.name(),
                    "date",           date.toString()
            ));
        }

        return result;
    }
}
