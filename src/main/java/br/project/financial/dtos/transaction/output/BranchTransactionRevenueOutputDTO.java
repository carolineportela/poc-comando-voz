package br.project.financial.dtos.transaction.output;

import br.project.financial.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BranchTransactionRevenueOutputDTO {
    private TransactionType transactionType;
    private String branch;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal total;
}
