package br.project.financial.dtos.transaction.input;

import br.project.financial.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BranchAmountInputDTO {
    private TransactionType type;
}
