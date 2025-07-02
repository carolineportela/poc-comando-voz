package br.project.financial.dtos.transaction.output;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class TotalAmountOutputDTO {
    private final String label;
    private final String total;

    public TotalAmountOutputDTO(String label, BigDecimal total) {
        this.label = label;
        this.total = total != null
                ? total.setScale(2, RoundingMode.HALF_UP).toPlainString()
                : null;
    }
}
