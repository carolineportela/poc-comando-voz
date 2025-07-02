//package br.project.financial.rest.specs;
//
//import br.project.financial.dtos.transaction.input.BranchAmountInputDTO;
//import br.project.financial.dtos.transaction.input.BranchTransactionRevenueInputDTO;
//import br.project.financial.dtos.transaction.input.Top2BranchesComparisonInputDTO;
//import br.project.financial.dtos.transaction.input.TransactionDetailedInputDTO;
//import br.project.financial.dtos.transaction.input.TransactionRevenueByDateInputDTO;
//import br.project.financial.dtos.transaction.input.TransactionRevenueByPeriodInputDTO;
//import br.project.financial.dtos.transaction.output.*;
//import br.project.financial.rest.specs.commons.response.error.*;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.List;
//
//@Tag(name = "Transactions", description = "Endpoints for financial transactions")
//@RequestMapping("/v1/transactions")
//@ApiResponseInternalServerError
//public interface TransactionControllerSpecs {
//
//    @Operation(summary = "Get transaction by type and date")
//    @ApiResponseBadRequest
//    @ApiResponseNotFound
//    @GetMapping
//    ResponseEntity<TransactionRevenueOutputDTO> getRevenueByTypeAndDate(TransactionRevenueByDateInputDTO inputDTO);
//
//    @Operation(summary = "Get transaction by type and period")
//    @ApiResponseBadRequest
//    @ApiResponseNotFound
//    @ApiResponseInvalidPeriod
//    @ApiResponseNoTransactionsFound
//    @GetMapping("/period")
//    ResponseEntity<TransactionRevenueOutputDTO> getRevenueByTypeAndPeriod(TransactionRevenueByPeriodInputDTO inputDTO);
//
//    @Operation(summary = "Get transaction by branch, type and period")
//    @ApiResponseBadRequest
//    @ApiResponseInvalidPeriod
//    @ApiResponseNotFound
//    @ApiResponseNoTransactionsFound
//    @GetMapping("/branch")
//    ResponseEntity<BranchTransactionRevenueOutputDTO> getRevenueByBranchTypeAndPeriod(BranchTransactionRevenueInputDTO inputDTO);
//
//    @Operation(summary = "get the branch that had the highest value by transaction type")
//    @ApiResponseBadRequest
//    @ApiResponseNotFound
//    @ApiResponseNoTransactionsFound
//    @GetMapping("/branch/top")
//    ResponseEntity<BranchAmountOutputDTO> getTopBranchByType(BranchAmountInputDTO inputDTO);
//
//    @Operation(summary = "Compare the turnover of the top 2 transaction branches for a given type and period")
//    @ApiResponseBadRequest
//    @ApiResponseNotFound
//    @ApiResponseInvalidPeriod
//    @ApiResponseNoTransactionsFound
//    @GetMapping("/comparison")
//    ResponseEntity<Top2BranchesComparisonDTO> compareTop2Branches(Top2BranchesComparisonInputDTO inputDTO);
//
//    @Operation(summary = "List detailed transactions for a branch and date period")
//    @ApiResponseBadRequest
//    @ApiResponseNotFound
//    @ApiResponseInvalidPeriod
//    @ApiResponseNoTransactionsFound
//    @GetMapping("/detailed")
//    ResponseEntity<List<TransactionDetailedDTO>> getDetailedTransactions(TransactionDetailedInputDTO inputDTO);
//}
package br.project.financial.rest.specs;

import br.project.financial.dtos.transaction.output.*;
import br.project.financial.enums.TransactionType;
import br.project.financial.rest.specs.commons.response.error.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Transactions", description = "Endpoints for financial transactions")
@RequestMapping("/v1/transactions")
@ApiResponseInternalServerError
public interface TransactionControllerSpecs {

    @Operation(summary = "Get transaction total by type and exact date")
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping
    ResponseEntity<TransactionRevenueOutputDTO> getRevenueByTypeAndDate(
            @Parameter(description = "Transaction type", required = true)
            @RequestParam("type") TransactionType type,

            @Parameter(description = "Date in format yyyy-MM-dd", required = true)
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    );

    @Operation(summary = "Get transaction total by type over a period")
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseInvalidPeriod
    @ApiResponseNoTransactionsFound
    @GetMapping("/period")
    ResponseEntity<TransactionRevenueOutputDTO> getRevenueByTypeAndPeriod(
            @Parameter(description = "Transaction type", required = true)
            @RequestParam("type") TransactionType type,

            @Parameter(description = "Start date in format yyyy-MM-dd", required = true)
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "End date in format yyyy-MM-dd", required = true)
            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    );

    @Operation(summary = "Get transaction total for a branch by type and period")
    @ApiResponseBadRequest
    @ApiResponseInvalidPeriod
    @ApiResponseNotFound
    @ApiResponseNoTransactionsFound
    @GetMapping("/branch")
    ResponseEntity<BranchTransactionRevenueOutputDTO> getRevenueByBranchTypeAndPeriod(
            @Parameter(description = "Transaction type", required = true)
            @RequestParam("type") TransactionType type,

            @Parameter(description = "Branch name", required = true)
            @RequestParam("branch") String branch,

            @Parameter(description = "Start date in format yyyy-MM-dd", required = true)
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "End date in format yyyy-MM-dd", required = true)
            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    );

    @Operation(summary = "Get the branch with the highest total by transaction type")
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseNoTransactionsFound
    @GetMapping("/branch/top")
    ResponseEntity<BranchAmountOutputDTO> getTopBranchByType(
            @Parameter(description = "Transaction type", required = true)
            @RequestParam("type") TransactionType type
    );

    @Operation(summary = "Compare turnover of the top 2 branches for a given type and period")
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseInvalidPeriod
    @ApiResponseNoTransactionsFound
    @GetMapping("/comparison")
    ResponseEntity<Top2BranchesComparisonDTO> compareTop2Branches(
            @Parameter(description = "Transaction type", required = true)
            @RequestParam("type") TransactionType type,

            @Parameter(description = "Start date in format yyyy-MM-dd", required = true)
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "End date in format yyyy-MM-dd", required = true)
            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    );

    @Operation(summary = "List detailed transactions for a branch over a period")
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseInvalidPeriod
    @ApiResponseNoTransactionsFound
    @GetMapping("/detailed")
    ResponseEntity<List<TransactionDetailedDTO>> getDetailedTransactions(
            @Parameter(description = "Branch name", required = true)
            @RequestParam("branch") String branch,

            @Parameter(description = "Start date in format yyyy-MM-dd", required = true)
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "End date in format yyyy-MM-dd", required = true)
            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    );
}
