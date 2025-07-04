package br.project.financial.usecases.transaction;

import br.project.financial.dtos.transaction.output.TransactionDetailedDTO;
import br.project.financial.repositories.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListDetailedAllTransactionsUseCase {

    private final TransactionRepository repository;

    public List<TransactionDetailedDTO> execute() {
        return repository.findAllTransactionSummaries();
    }
}
