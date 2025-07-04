package br.project.financial.rest.controllers;

import br.project.financial.dtos.transaction.output.*;
import br.project.financial.enums.TransactionType;
import br.project.financial.rest.specs.TransactionControllerSpecs;
import br.project.financial.usecases.transaction.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/transactions")
public class TransactionRestController implements TransactionControllerSpecs {

    private final CalculateRevenueByTypeAndDateUseCase byDateUseCase;
    private final CalculateRevenueByTypeAndPeriodUseCase byPeriodUseCase;
    private final CalculateBranchRevenueByTypeAndPeriodUseCase byBranchUseCase;
    private final CalculateTopBranchByTypeUseCase topBranchUseCase;
    private final CalculateTop2BranchesComparisonUseCase comparisonUseCase;
    private final ListDetailedTransactionsUseCase detailedTransactionsUseCase;
    private final CalculatePaidExpensesTotalUseCase paidExpensesTotalUseCase;
    private final CalculateFilteredTotalUseCase filteredTotalUseCase;
    private final CalculateExpensesByCenterUseCase expensesByCenterUseCase;
    private final CalculateSalesByCategoryAndCenterUseCase salesByCategoryUseCase;
    private final CalculateQuantitySoldUseCase quantitySoldUseCase;
    private final ListDetailedAllTransactionsUseCase   listDetailedAllTransactionsUseCase;

    public TransactionRestController(
            CalculateRevenueByTypeAndPeriodUseCase byPeriodUseCase,
            CalculateRevenueByTypeAndDateUseCase byDateUseCase,
            CalculateBranchRevenueByTypeAndPeriodUseCase byBranchUseCase,
            CalculateTopBranchByTypeUseCase topBranchUseCase,
            CalculateTop2BranchesComparisonUseCase comparisonUseCase,
            ListDetailedTransactionsUseCase detailedTransactionsUseCase,
            CalculatePaidExpensesTotalUseCase paidExpensesTotalUseCase,
            CalculateFilteredTotalUseCase filteredTotalUseCase,
            CalculateExpensesByCenterUseCase expensesByCenterUseCase,
            CalculateSalesByCategoryAndCenterUseCase salesByCategoryUseCase,
            CalculateQuantitySoldUseCase quantitySoldUseCase,
            ListDetailedAllTransactionsUseCase   listDetailedAllTransactionsUseCase
    ) {
        this.byPeriodUseCase = byPeriodUseCase;
        this.byDateUseCase = byDateUseCase;
        this.byBranchUseCase = byBranchUseCase;
        this.topBranchUseCase = topBranchUseCase;
        this.comparisonUseCase = comparisonUseCase;
        this.detailedTransactionsUseCase = detailedTransactionsUseCase;
        this.paidExpensesTotalUseCase = paidExpensesTotalUseCase;
        this.filteredTotalUseCase = filteredTotalUseCase;
        this.expensesByCenterUseCase = expensesByCenterUseCase;
        this.salesByCategoryUseCase = salesByCategoryUseCase;
        this.quantitySoldUseCase = quantitySoldUseCase;
        this.listDetailedAllTransactionsUseCase = listDetailedAllTransactionsUseCase;
    }

    @GetMapping
    public ResponseEntity<TransactionRevenueOutputDTO> getRevenueByTypeAndDate(
            @RequestParam("type") TransactionType type,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(byDateUseCase.execute(type, date));
    }

    @GetMapping("/period")
    public ResponseEntity<TransactionRevenueOutputDTO> getRevenueByTypeAndPeriod(
            @RequestParam("type") TransactionType type,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(byPeriodUseCase.execute(type, startDate, endDate));
    }

    @GetMapping("/branch")
    public ResponseEntity<BranchTransactionRevenueOutputDTO> getRevenueByBranchTypeAndPeriod(
            @RequestParam("type") TransactionType type,
            @RequestParam("branch") String branch,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(byBranchUseCase.execute(type, branch, startDate, endDate));
    }

    @GetMapping("/branch/top")
    public ResponseEntity<BranchAmountOutputDTO> getTopBranchByType(
            @RequestParam("type") TransactionType type
    ) {
        return ResponseEntity.ok(topBranchUseCase.execute(type));
    }

    @GetMapping("/comparison")
    public ResponseEntity<Top2BranchesComparisonDTO> compareTop2Branches(
            @RequestParam("type") TransactionType type,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(comparisonUseCase.execute(type, startDate, endDate));
    }

    @GetMapping("/detailed")
    public ResponseEntity<List<TransactionDetailedDTO>> getDetailedTransactions(
            @RequestParam("branch") String branch,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(detailedTransactionsUseCase.execute(branch, startDate, endDate));
    }

    /// //////////////////////////////////////////////////////////////////////////////////
    // ## Total de compras por propósito e centro de custo
    //## Quanto foi comprado (todas a contas) - valor total de compras de um centro de custo
    @GetMapping("/purchases")
    public ResponseEntity<TotalAmountOutputDTO> getTotalComprasByCostCenter(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("purpose") String expensePurposeName,
            @RequestParam("costCenter") String costCenterName
    ) {
        TotalAmountOutputDTO dto = paidExpensesTotalUseCase.execute(
                startDate, endDate, expensePurposeName, costCenterName
        );
        return ResponseEntity.ok(dto);
    }

    /// //////////////////////////////////////////////////////////////////////////////////
    // ## Total filtrado por tipo, propósito, categoria e centro de custo
    //# Quanto foi comprado de algodao (essa query é pra qualquer tipo de produto)
    @GetMapping("/total")
    public ResponseEntity<FilteredTotalOutputDTO> getFilteredTotal(
            @RequestParam("type") TransactionType type,
            @RequestParam("purpose") String purpose,
            @RequestParam("category") String category,
            @RequestParam("costCenter") String costCenter,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        FilteredTotalOutputDTO dto = filteredTotalUseCase.execute(
                type, purpose, category, costCenter, startDate, endDate
        );
        return ResponseEntity.ok(dto);
    }

    /// //////////////////////////////////////////////////////////////////////////////////
    //## TRAZ VALOR TOTAL de todas despesas DE UM CENTRO DE CUSTO
    //## Quanto foi pago  (todas a contas)
    @GetMapping("/total/expenses")
    public ResponseEntity<TotalAmountOutputDTO> getExpensesByCenter(
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @RequestParam("purpose") String expensePurposeName,

            @RequestParam("costCenter") String costCenterName
    ) {
        TotalAmountOutputDTO dto = expensesByCenterUseCase.execute(
                startDate,
                endDate,
                expensePurposeName,
                costCenterName
        );
        return ResponseEntity.ok(dto);
    }
    /// //////////////////////////////////////////////////////////////////////////////////
    //#Quanto foi vendido em valor
    //#valor total de vendas de uma categoria de produto de um centro de custo
    @GetMapping("/total/sales")
    public ResponseEntity<TotalAmountOutputDTO> getSalesByCategoryAndCenter(
            @RequestParam("type")       TransactionType type,
            @RequestParam("category")   String category,
            @RequestParam("costCenter") String costCenter,
            @RequestParam("startDate")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate")    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        TotalAmountOutputDTO dto = salesByCategoryUseCase.execute(
                type, category, costCenter, startDate, endDate
        );
        return ResponseEntity.ok(dto);
    }

    /// //////////////////////////////////////////////////////////////////////////////////
    //#Quanto foi vendido em Kilos
    //#valor total de vendas de uma categoria de produto de um centro de custo por kg
    @GetMapping("/total/sales/kg")
    public ResponseEntity<TotalAmountOutputDTO> getQuantitySold(
            @RequestParam("type")       TransactionType type,
            @RequestParam("category")   String category,
            @RequestParam("costCenter") String costCenter,
            @RequestParam("startDate")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate")    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        TotalAmountOutputDTO dto = quantitySoldUseCase.execute(
                type, category, costCenter, startDate, endDate
        );
        return ResponseEntity.ok(dto);
    }

    /// //////////////////////////////////////////////////////////////////////////////////
    //# Todas transactions
    @GetMapping("/detailed/all")
    public ResponseEntity<List<TransactionDetailedDTO>> getAllDetailedTransactions() {
        List<TransactionDetailedDTO> list =
                listDetailedAllTransactionsUseCase.execute();
        return ResponseEntity.ok(list);
    }

}
