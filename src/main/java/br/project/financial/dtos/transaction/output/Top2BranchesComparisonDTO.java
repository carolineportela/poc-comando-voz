package br.project.financial.dtos.transaction.output;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class Top2BranchesComparisonDTO {
    private List<BranchAmountOutputDTO> topBranches;
    private BigDecimal difference;
}
