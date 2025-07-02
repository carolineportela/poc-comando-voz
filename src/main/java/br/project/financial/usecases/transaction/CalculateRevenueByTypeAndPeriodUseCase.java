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
//public class CalculateRevenueByTypeAndPeriodUseCase {
//
//    private final TransactionRepository repository;
//
//    public CalculateRevenueByTypeAndPeriodUseCase(TransactionRepository repository) {
//        this.repository = repository;
//    }
//
//    public TransactionRevenueOutputDTO execute(
//            TransactionType transactionType,
//            LocalDate startDate,
//            LocalDate endDate
//    ) {
//        if (transactionType == null) {
//            throw new BusinessRuleException(
//                    ExceptionCode.INVALID_TRANSACTION_TYPE,
//                    Map.of("message", "Transaction type must be provided")
//            );
//        }
//
//        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
//            throw new BusinessRuleException(
//                    ExceptionCode.INVALID_PERIOD,
//                    Map.of("message", "Start date must be before or equal to end date")
//            );
//        }
//
//        TransactionRevenueOutputDTO result = repository.sumByTypeAndPeriod(transactionType, startDate, endDate);
//
//        if (result == null || result.getTotal() == null || result.getTotal().compareTo(BigDecimal.ZERO) == 0) {
//            throw new NoTransactionsFoundException(Map.of(
//                    "transactionType", transactionType.name(),
//                    "startDate", startDate,
//                    "endDate", endDate
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
        // 1. Tipo de transação não pode ser nulo
        if (transactionType == null) {
            throw new BusinessRuleException(
                    ExceptionCode.INVALID_TRANSACTION_TYPE,
                    Map.of("transactionType", "must be provided")
            );
        }

        // 2. Período deve ser válido
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

        // 3. Consulta ao repositório
        TransactionRevenueOutputDTO result =
                repository.sumByTypeAndPeriod(transactionType, startDate, endDate);

        // 4. Se não vier resultado (null ou total null), lança exceção
        if (result == null || result.getTotal() == null) {
            throw new NoTransactionsFoundException(Map.of(
                    "transactionType", transactionType.name(),
                    "startDate", startDate.toString(),
                    "endDate",   endDate.toString()
            ));
        }

        return result;
    }
}
