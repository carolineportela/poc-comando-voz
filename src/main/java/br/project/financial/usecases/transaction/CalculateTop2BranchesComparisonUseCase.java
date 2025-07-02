//package br.project.financial.usecases.transaction;
//
//import br.project.financial.dtos.transaction.output.Top2BranchesComparisonDTO;
//import br.project.financial.dtos.transaction.output.BranchAmountOutputDTO;
//import br.project.financial.enums.TransactionType;
//import br.project.financial.errors.ExceptionCode;
//import br.project.financial.errors.exceptions.BusinessRuleException;
//import br.project.financial.repositories.transaction.TransactionRepository;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class CalculateTop2BranchesComparisonUseCase {
//
//    private final TransactionRepository repository;
//
//    public CalculateTop2BranchesComparisonUseCase(TransactionRepository repository) {
//        this.repository = repository;
//    }
//
//    public Top2BranchesComparisonDTO execute(
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
//        if (startDate.isAfter(endDate)) {
//            throw new BusinessRuleException(
//                    ExceptionCode.INVALID_PERIOD,
//                    Map.of("message", "Start date must be before end date")
//            );
//        }
//
//        List<BranchAmountOutputDTO> top2 = repository
//                .findTop2BranchesByTransactionTypeAndDateBetween(
//                        transactionType, startDate, endDate
//                );
//
//        if (top2.size() < 2) {
//            throw new BusinessRuleException(
//                    ExceptionCode.INSUFFICIENT_BRANCHES_FOR_COMPARISON,
//                    Map.of(
//                            "transactionType", transactionType.name(),
//                            "startDate", startDate,
//                            "endDate", endDate,
//                            "branchesFound", top2.size()
//                    )
//            );
//        }
//
//        BigDecimal bd1 = top2.get(0).getAmount();
//        BigDecimal bd2 = top2.get(1).getAmount();
//        BigDecimal diff = bd1
//                .subtract(bd2)
//                .setScale(2, RoundingMode.HALF_UP);
//
//        return new Top2BranchesComparisonDTO(top2, diff);
//
//    }
//}
package br.project.financial.usecases.transaction;

import br.project.financial.dtos.transaction.output.Top2BranchesComparisonDTO;
import br.project.financial.dtos.transaction.output.BranchAmountOutputDTO;
import br.project.financial.enums.TransactionType;
import br.project.financial.errors.ExceptionCode;
import br.project.financial.errors.exceptions.BusinessRuleException;
import br.project.financial.repositories.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class CalculateTop2BranchesComparisonUseCase {

    private final TransactionRepository repository;

    public CalculateTop2BranchesComparisonUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public Top2BranchesComparisonDTO execute(
            TransactionType transactionType,
            LocalDate startDate,
            LocalDate endDate
    ) {
        // 1) validação de transactionType
        if (transactionType == null) {
            throw new BusinessRuleException(
                    ExceptionCode.INVALID_TRANSACTION_TYPE,
                    Map.of("transactionType", "must be provided")
            );
        }

        // 2) validação de datas
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

        // 3) busca os top 2
        List<BranchAmountOutputDTO> top2 = repository
                .findTop2BranchesByTransactionTypeAndDateBetween(
                        transactionType, startDate, endDate
                );

        // 4) verifica se encontrou pelo menos 2 filiais
        if (top2.size() < 2) {
            throw new BusinessRuleException(
                    ExceptionCode.INSUFFICIENT_BRANCHES_FOR_COMPARISON,
                    Map.of(
                            "transactionType", transactionType.name(),
                            "startDate",        startDate.toString(),
                            "endDate",          endDate.toString(),
                            "branchesFound",    String.valueOf(top2.size())
                    )
            );
        }

        // 5) calcula diferença entre o 1º e o 2º
        BigDecimal firstAmount  = top2.get(0).getAmount();
        BigDecimal secondAmount = top2.get(1).getAmount();
        BigDecimal difference   = firstAmount
                .subtract(secondAmount)
                .setScale(2, RoundingMode.HALF_UP);

        // 6) retorna DTO com lista e diferença
        return new Top2BranchesComparisonDTO(top2, difference);
    }
}
