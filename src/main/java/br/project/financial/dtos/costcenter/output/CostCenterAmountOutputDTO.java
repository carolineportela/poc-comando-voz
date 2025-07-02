package br.project.financial.dtos.costcenter.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class CostCenterAmountOutputDTO {
    private String costCenter;
    private BigDecimal total;
}