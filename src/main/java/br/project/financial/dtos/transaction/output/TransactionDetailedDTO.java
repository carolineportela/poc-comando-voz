
package br.project.financial.dtos.transaction.output;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import br.project.financial.enums.TransactionType;

@Getter
@Setter
public class TransactionDetailedDTO {
    private Long        id;
    private String      transactionType;
    private String      customer;
    private String      category;
    private String      costCenter;
    private BigDecimal  amount;
    private String      branch;
    private LocalDate   date;

    public TransactionDetailedDTO(
            Long id,
            TransactionType transactionType,
            String customer,
            String category,
            String costCenter,
            BigDecimal amount,
            String branch,
            LocalDate date
    ) {
        this.id               = id;
        this.transactionType  = transactionType != null
                ? transactionType.name()
                : null;
        this.customer         = customer;
        this.category         = category;
        this.costCenter       = costCenter;
        this.amount           = amount;
        this.branch           = branch;
        this.date             = date;
    }
}
