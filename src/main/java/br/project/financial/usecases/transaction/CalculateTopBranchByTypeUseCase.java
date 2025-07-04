package br.project.financial.usecases.transaction;

import br.project.financial.dtos.transaction.output.BranchAmountOutputDTO;
import br.project.financial.enums.TransactionType;
import br.project.financial.errors.ExceptionCode;
import br.project.financial.errors.exceptions.BusinessRuleException;
import br.project.financial.errors.exceptions.NoTransactionsFoundException;
import br.project.financial.repositories.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class CalculateTopBranchByTypeUseCase {

    private final TransactionRepository repository;

    public CalculateTopBranchByTypeUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public BranchAmountOutputDTO execute(TransactionType transactionType) {
        if (transactionType == null) {
            throw new BusinessRuleException(
                    ExceptionCode.INVALID_TRANSACTION_TYPE,
                    Map.of("transactionType", "must be provided")
            );
        }

        List<BranchAmountOutputDTO> list =
                repository.sumByTypeGroupedByBranchOrderedDesc(transactionType);

        if (list.isEmpty()) {
            throw new NoTransactionsFoundException(
                    Map.of("transactionType", transactionType.name())
            );
        }

        BranchAmountOutputDTO result = list.get(0);

        BigDecimal amount = result.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new NoTransactionsFoundException(
                    Map.of("transactionType", transactionType.name())
            );
        }
        return result;
    }
}
