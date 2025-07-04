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


        List<BranchAmountOutputDTO> top2 = repository
                .findTop2BranchesByTransactionTypeAndDateBetween(
                        transactionType, startDate, endDate
                );


        if (top2.size() < 2) {
            throw new BusinessRuleException(
                    ExceptionCode.INSUFFICIENT_BRANCHES_FOR_COMPARISON,
                    Map.of(
                            "transactionType", transactionType.name(),
                            "startDate", startDate.toString(),
                            "endDate", endDate.toString(),
                            "branchesFound", String.valueOf(top2.size())
                    )
            );
        }


        BigDecimal firstAmount = top2.get(0).getAmount();
        BigDecimal secondAmount = top2.get(1).getAmount();
        BigDecimal difference = firstAmount
                .subtract(secondAmount)
                .setScale(2, RoundingMode.HALF_UP);


        return new Top2BranchesComparisonDTO(top2, difference);
    }
}
