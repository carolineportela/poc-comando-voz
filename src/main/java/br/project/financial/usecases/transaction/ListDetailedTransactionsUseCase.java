//package br.project.financial.usecases.transaction;
//
//import br.project.financial.dtos.transaction.output.TransactionDetailedDTO;
//import br.project.financial.errors.ExceptionCode;
//import br.project.financial.errors.exceptions.BusinessRuleException;
//import br.project.financial.errors.exceptions.NoTransactionsFoundException;
//import br.project.financial.mappers.transaction.TransactionDetailedMapper;
//import br.project.financial.repositories.transaction.TransactionRepository;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class ListDetailedTransactionsUseCase {
//
//    private final TransactionRepository repository;
//    private final TransactionDetailedMapper mapper;
//
//    public ListDetailedTransactionsUseCase(
//            TransactionRepository repository,
//            TransactionDetailedMapper mapper
//    ) {
//        this.repository = repository;
//        this.mapper = mapper;
//    }
//
//    public List<TransactionDetailedDTO> execute(
//            String branch,
//            LocalDate startDate,
//            LocalDate endDate
//    ) {
//        if (startDate.isAfter(endDate)) {
//            throw new BusinessRuleException(
//                    ExceptionCode.INVALID_PERIOD,
//                    Map.of("message", "Start date must be before end date")
//            );
//        }
//
//        var entities = repository.findByBranchAndDateBetweenOrderByDateAsc(branch, startDate, endDate);
//
//        if (entities.isEmpty()) {
//            throw new NoTransactionsFoundException(Map.of(
//                    "branch", branch,
//                    "startDate", startDate,
//                    "endDate", endDate
//            ));
//        }
//
//        return mapper.toDtos(entities);
//
//    }
//}
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
        // 1) validação de branch
        if (branch == null || branch.isBlank()) {
            throw new BusinessRuleException(
                    ExceptionCode.ENTITY_NOT_FOUND,
                    Map.of("branch", "must be provided")
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

        // 3) busca as transações detalhadas da filial no período
        List<Transaction> entities =
                repository.findByBranch_NameAndDateBetweenOrderByDateAsc(
                        branch, startDate, endDate
                );

        // 4) se não encontrar nenhuma, lança NoTransactionsFoundException
        if (entities.isEmpty()) {
            throw new NoTransactionsFoundException(
                    Map.of(
                            "branch",    branch,
                            "startDate", startDate.toString(),
                            "endDate",   endDate.toString()
                    )
            );
        }

        // 5) mapeia para DTOs e retorna
        return mapper.toDtos(entities);
    }
}
