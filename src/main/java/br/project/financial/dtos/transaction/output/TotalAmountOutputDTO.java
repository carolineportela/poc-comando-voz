package br.project.financial.dtos.transaction.output;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class TotalAmountOutputDTO {
    private final String costCenterName;
    private final String total;

    public TotalAmountOutputDTO(String costCenterName, BigDecimal total) {
        this.costCenterName = costCenterName;
        this.total = total != null
                ? total.setScale(2, RoundingMode.HALF_UP).toPlainString()
                : null;
    }
}
