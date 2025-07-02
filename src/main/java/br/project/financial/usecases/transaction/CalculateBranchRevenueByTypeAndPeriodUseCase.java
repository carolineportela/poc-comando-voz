//package br.project.financial.usecases.transaction;
//
//import br.project.financial.dtos.transaction.output.BranchTransactionRevenueOutputDTO;
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
//public class CalculateBranchRevenueByTypeAndPeriodUseCase {
//
//    private final TransactionRepository repository;
//
//    public CalculateBranchRevenueByTypeAndPeriodUseCase(TransactionRepository repository) {
//        this.repository = repository;
//    }
//
//    public BranchTransactionRevenueOutputDTO execute(
//            TransactionType transactionType,
//            String branch,
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
//        BranchTransactionRevenueOutputDTO result =
//                repository.sumByTypeBranchAndPeriod(transactionType, branch, startDate, endDate);
//
//        if (result == null
//                || result.getTotal() == null
//                || result.getTotal().compareTo(BigDecimal.ZERO) == 0
//        ) {
//            throw new NoTransactionsFoundException(Map.of(
//                    "transactionType", transactionType.name(),
//                    "branch", branch,
//                    "startDate", startDate,
//                    "endDate", endDate
//            ));
//        }
//
//        return result;
//    }
//}
package br.project.financial.usecases.transaction;

import br.project.financial.dtos.transaction.output.BranchTransactionRevenueOutputDTO;
import br.project.financial.enums.TransactionType;
import br.project.financial.errors.ExceptionCode;
import br.project.financial.errors.exceptions.BusinessRuleException;
import br.project.financial.errors.exceptions.NoTransactionsFoundException;
import br.project.financial.repositories.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class CalculateBranchRevenueByTypeAndPeriodUseCase {

    private final TransactionRepository repository;

    public CalculateBranchRevenueByTypeAndPeriodUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public BranchTransactionRevenueOutputDTO execute(
            TransactionType transactionType,
            String branch,
            LocalDate startDate,
            LocalDate endDate
    ) {
        // 1) validação de tipo de transação
        if (transactionType == null) {
            throw new BusinessRuleException(
                    ExceptionCode.INVALID_TRANSACTION_TYPE,
                    Map.of("transactionType", "must be provided")
            );
        }
        // 2) validação do período
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

        // 3) consulta ao repositório
        BranchTransactionRevenueOutputDTO result =
                repository.sumByTypeBranchAndPeriod(transactionType, branch, startDate, endDate);

        // 4) se não houver resultado, lança exceção
        if (result == null || result.getTotal() == null) {
            throw new NoTransactionsFoundException(Map.of(
                    "transactionType", transactionType.name(),
                    "branch",           branch,
                    "startDate",        startDate.toString(),
                    "endDate",          endDate.toString()
            ));
        }

        return result;
    }
}
