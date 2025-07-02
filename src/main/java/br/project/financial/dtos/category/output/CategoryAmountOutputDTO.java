package br.project.financial.dtos.category.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

//@Getter
//@Setter
//@AllArgsConstructor
//public class CategoryAmountOutputDTO {
//    private String category;
//    private BigDecimal total;
//
//
//}

@Getter
public class CategoryAmountOutputDTO {
    private final String category;
    private final String total;

    public CategoryAmountOutputDTO(String category, BigDecimal total) {
        this.category = category;
        this.total = total != null
                ? total.setScale(2, RoundingMode.HALF_UP).toPlainString()
                : null;
    }
}
