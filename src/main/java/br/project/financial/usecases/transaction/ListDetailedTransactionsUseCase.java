package br.project.financial.usecases.transaction;

import br.project.financial.dtos.transaction.output.TransactionDetailedDTO;
import br.project.financial.entities.Transaction;
import br.project.financial.errors.ExceptionCode;
import br.project.financial.errors.exceptions.BusinessRuleException;
import br.project.financial.errors.exceptions.NoTransactionsFoundException;
import br.project.financial.mappers.transaction.TransactionDetailedMapper;
import br.project.financial.repositories.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ListDetailedTransactionsUseCase {

    private final TransactionRepository repository;
    private final TransactionDetailedMapper mapper;

    public ListDetailedTransactionsUseCase(
            TransactionRepository repository,
            TransactionDetailedMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<TransactionDetailedDTO> execute(
            String branch,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (branch == null || branch.isBlank()) {
            throw new BusinessRuleException(
                    ExceptionCode.ENTITY_NOT_FOUND,
                    Map.of("branch", "must be provided")
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

        List<Transaction> entities =
                repository.findByBranch_NameAndDateBetweenOrderByDateAsc(
                        branch, startDate, endDate
                );

        if (entities.isEmpty()) {
            throw new NoTransactionsFoundException(
                    Map.of(
                            "branch",    branch,
                            "startDate", startDate.toString(),
                            "endDate",   endDate.toString()
                    )
            );
        }

        return mapper.toDtos(entities);
    }
}
