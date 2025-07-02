//package br.project.financial.usecases.transaction;
//
//import br.project.financial.dtos.category.output.CategoryAmountOutputDTO;
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
//public class CalculatePurchasesByCategoryUseCase {
//
//    private final TransactionRepository repository;
//
//    public CalculatePurchasesByCategoryUseCase(TransactionRepository repository) {
//        this.repository = repository;
//    }
//
//    /**
//     * Retorna quanto foi comprado (DESPESA) de uma dada categoria num período.
//     */
//    public CategoryAmountOutputDTO execute(
//            String category,
//            LocalDate startDate,
//            LocalDate endDate
//    ) {
//        // validações básicas
//        if (category == null || category.isBlank()) {
//            throw new BusinessRuleException(
//                    ExceptionCode.API_FIELDS_INVALID,
//                    Map.of("category", "must be provided")
//            );
//        }
//        if (startDate == null || endDate == null) {
//            throw new BusinessRuleException(
//                    ExceptionCode.INVALID_PERIOD,
//                    Map.of("startDate/endDate", "must be provided")
//            );
//        }
//        if (startDate.isAfter(endDate)) {
//            throw new BusinessRuleException(
//                    ExceptionCode.INVALID_PERIOD,
//                    Map.of("startDate", "must be before or equal to endDate")
//            );
//        }
//
//        // consulta ao repositório
//        CategoryAmountOutputDTO result =
//                repository.sumPurchasesByCategory(category, startDate, endDate);
//
//        // verifica se veio algum valor
//        if (result == null
//                || result.getTotal() == null
//                || result.getTotal().compareTo(BigDecimal.ZERO) == 0
//        ) {
//            throw new NoTransactionsFoundException(Map.of(
//                    "category",   category,
//                    "startDate",  startDate.toString(),
//                    "endDate",    endDate.toString()
//            ));
//        }
//
//        return result;
//    }
//}
package br.project.financial.usecases.transaction;

import br.project.financial.dtos.category.output.CategoryAmountOutputDTO;
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
public class CalculatePurchasesByCategoryUseCase {

    private final TransactionRepository repository;

    public CalculatePurchasesByCategoryUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public CategoryAmountOutputDTO execute(
            TransactionType transactionType,
            String category,
            String branch,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (transactionType == null) {
            throw new BusinessRuleException(ExceptionCode.INVALID_TRANSACTION_TYPE, Map.of("type", "must be provided"));
        }
        if (category == null || category.isBlank()) {
            throw new BusinessRuleException(ExceptionCode.API_FIELDS_INVALID, Map.of("category", "must be provided"));
        }
        if (branch == null || branch.isBlank()) {
            throw new BusinessRuleException(ExceptionCode.API_FIELDS_INVALID, Map.of("branch", "must be provided"));
        }
        if (startDate == null || endDate == null) {
            throw new BusinessRuleException(ExceptionCode.INVALID_PERIOD, Map.of("startDate/endDate", "must be provided"));
        }
        if (startDate.isAfter(endDate)) {
            throw new BusinessRuleException(ExceptionCode.INVALID_PERIOD, Map.of("startDate", "must be before or equal to endDate"));
        }

        CategoryAmountOutputDTO result = repository
                .sumPurchasesByTypeCategoryAndBranch(transactionType, category, branch, startDate, endDate);

        if (result == null
                || result.getTotal() == null
                || new BigDecimal(result.getTotal()).compareTo(BigDecimal.ZERO) == 0) {

            throw new NoTransactionsFoundException(Map.of(
                    "type", transactionType.name(),
                    "category", category,
                    "branch", branch,
                    "startDate", startDate.toString(),
                    "endDate", endDate.toString()
            ));
        }

        return result;
    }

}
