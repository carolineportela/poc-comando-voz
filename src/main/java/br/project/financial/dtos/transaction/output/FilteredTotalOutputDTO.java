package br.project.financial.dtos.transaction.output;

import br.project.financial.enums.TransactionType;
import lombok.Getter;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class FilteredTotalOutputDTO {
    private final String type;
    private final String purpose;
    private final String categoryName;
    private final String costCenterName;
    private final String total;

    public FilteredTotalOutputDTO(
            TransactionType type,
            String purpose,
            String categoryName,
            String costCenterName,
            BigDecimal total
    ) {
        this.type           = type.name();
        this.purpose        = purpose;
        this.categoryName   = categoryName;
        this.costCenterName = costCenterName;
        this.total          = total != null
                ? total.setScale(2, RoundingMode.HALF_UP).toPlainString()
                : null;
    }
}
