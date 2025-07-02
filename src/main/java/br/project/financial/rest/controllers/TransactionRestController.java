package br.project.financial.rest.controllers;

import br.project.financial.dtos.category.output.CategoryAmountOutputDTO;
import br.project.financial.dtos.transaction.input.*;
import br.project.financial.dtos.transaction.output.*;
import br.project.financial.enums.TransactionType;
import br.project.financial.rest.specs.TransactionControllerSpecs;
import br.project.financial.usecases.transaction.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.project.financial.usecases.transaction.CalculatePurchasesByCategoryUseCase;

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
    private final CalculatePurchasesByCategoryUseCase purchasesByCategoryUseCase;
    private final CalculatePaidExpensesTotalUseCase paidExpensesTotalUseCase;



    public TransactionRestController(
            CalculateRevenueByTypeAndPeriodUseCase byPeriodUseCase,
            CalculateRevenueByTypeAndDateUseCase byDateUseCase,
            CalculateBranchRevenueByTypeAndPeriodUseCase byBranchUseCase,
            CalculateTopBranchByTypeUseCase topBranchUseCase,
            CalculateTop2BranchesComparisonUseCase comparisonUseCase,
            ListDetailedTransactionsUseCase detailedTransactionsUseCase,
            CalculatePurchasesByCategoryUseCase purchasesByCategoryUseCase,
            CalculatePaidExpensesTotalUseCase paidExpensesTotalUseCase
    ) {
        this.byPeriodUseCase         = byPeriodUseCase;
        this.byDateUseCase           = byDateUseCase;
        this.byBranchUseCase         = byBranchUseCase;
        this.topBranchUseCase        = topBranchUseCase;
        this.comparisonUseCase       = comparisonUseCase;
        this.detailedTransactionsUseCase = detailedTransactionsUseCase;
        this.purchasesByCategoryUseCase = purchasesByCategoryUseCase;
        this.paidExpensesTotalUseCase   = paidExpensesTotalUseCase;
    }


    @GetMapping
    public ResponseEntity<TransactionRevenueOutputDTO> getRevenueByTypeAndDate(
            @RequestParam("type") TransactionType type,
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(
                byDateUseCase.execute(type, date)
        );
    }

    @GetMapping("/period")
    public ResponseEntity<TransactionRevenueOutputDTO> getRevenueByTypeAndPeriod(
            @RequestParam("type") TransactionType type,
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(
                byPeriodUseCase.execute(type, startDate, endDate)
        );
    }

    @GetMapping("/branch")
    public ResponseEntity<BranchTransactionRevenueOutputDTO> getRevenueByBranchTypeAndPeriod(
            @RequestParam("type")   TransactionType type,
            @RequestParam("branch") String branch,
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(
                byBranchUseCase.execute(type, branch, startDate, endDate)
        );
    }

    @GetMapping("/branch/top")
    public ResponseEntity<BranchAmountOutputDTO> getTopBranchByType(
            @RequestParam("type") TransactionType type
    ) {
        return ResponseEntity.ok(
                topBranchUseCase.execute(type)
        );
    }

    @GetMapping("/comparison")
    public ResponseEntity<Top2BranchesComparisonDTO> compareTop2Branches(
            @RequestParam("type") TransactionType type,
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(
                comparisonUseCase.execute(type, startDate, endDate)
        );
    }

    @GetMapping("/detailed")
    public ResponseEntity<List<TransactionDetailedDTO>> getDetailedTransactions(
            @RequestParam("branch") String branch,
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(
                detailedTransactionsUseCase.execute(branch, startDate, endDate)
        );
    }

    /**
     * Quanto foi comprado (DESPESA) de uma categoria em um período.
     * Exemplo: GET /v1/transactions/purchases/category?category=Algodão&startDate=2024-01-01&endDate=2024-01-31
     */
//    @GetMapping("/purchases/category")
//    public ResponseEntity<CategoryAmountOutputDTO> getPurchasesByCategory(
//            @RequestParam("category")  String category,
//            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam("endDate")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
//    ) {
//        return ResponseEntity.ok(purchasesByCategoryUseCase.execute(category, startDate, endDate));
//    }

    // purchases = compras
    // vendas = sales
    @GetMapping("/purchases/category")
    public ResponseEntity<CategoryAmountOutputDTO> getByTypeCategoryAndBranch(
            @RequestParam("type")       TransactionType type,
            @RequestParam("category")   String category,
            @RequestParam("branch")     String branch,
            @RequestParam("startDate")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate")    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(
                purchasesByCategoryUseCase.execute(type, category, branch, startDate, endDate)
        );
    }

    // todas as despesas
    @GetMapping("/expenses/paid")
    public ResponseEntity<TotalAmountOutputDTO> getPaidExpensesTotal(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "branch", required = false) String branch
    ) {
        return ResponseEntity.ok(
                paidExpensesTotalUseCase.execute(startDate, endDate, branch)
        );
    }





}
