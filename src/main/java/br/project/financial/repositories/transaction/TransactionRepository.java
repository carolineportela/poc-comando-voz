package br.project.financial.repositories.transaction;

import br.project.financial.dtos.category.output.CategoryAmountOutputDTO;
import br.project.financial.dtos.transaction.output.*;
import br.project.financial.entities.Transaction;
import br.project.financial.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
                SELECT new br.project.financial.dtos.transaction.output.TransactionRevenueOutputDTO(
                    t.transactionType,
                    :startDate,
                    :endDate,
                    SUM(item.totalPrice)
                )
                FROM Transaction t
                JOIN t.items item
                WHERE t.transactionType = :transactionType
                  AND t.date BETWEEN :startDate AND :endDate
            """)
    TransactionRevenueOutputDTO sumByTypeAndPeriod(
            @Param("transactionType") TransactionType transactionType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    @Query("""
                SELECT new br.project.financial.dtos.transaction.output.BranchTransactionRevenueOutputDTO(
                    t.transactionType,
                    t.branch.name,
                    :startDate,
                    :endDate,
                    SUM(item.totalPrice)
                )
                FROM Transaction t
                JOIN t.items item
                WHERE t.transactionType = :transactionType
                  AND t.branch.name = :branchName
                  AND t.date BETWEEN :startDate AND :endDate
            """)
    BranchTransactionRevenueOutputDTO sumByTypeBranchAndPeriod(
            @Param("transactionType") TransactionType transactionType,
            @Param("branchName") String branchName,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
                SELECT new br.project.financial.dtos.transaction.output.BranchAmountOutputDTO(
                    t.branch.name,
                    SUM(item.totalPrice)
                )
                FROM Transaction t
                JOIN t.items item
                WHERE t.transactionType   = :transactionType
                  AND t.date BETWEEN      :startDate AND :endDate
                GROUP BY t.branch.name
                ORDER BY SUM(item.totalPrice) DESC
            """)
    List<BranchAmountOutputDTO> findTop2BranchesByTransactionTypeAndDateBetween(
            @Param("transactionType") TransactionType transactionType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
                SELECT new br.project.financial.dtos.transaction.output.BranchAmountOutputDTO(
                    t.branch.name,
                    SUM(item.totalPrice)
                )
                FROM Transaction t
                JOIN t.items item
                WHERE t.transactionType = :transactionType
                GROUP BY t.branch.name
                ORDER BY SUM(item.totalPrice) DESC
            """)
    List<BranchAmountOutputDTO> sumByTypeGroupedByBranchOrderedDesc(
            @Param("transactionType") TransactionType transactionType
    );

    List<Transaction> findByBranch_NameAndDateBetweenOrderByDateAsc(
            String branchName,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("""
                SELECT new br.project.financial.dtos.category.output.CategoryAmountOutputDTO(
                    p.category.name,
                    SUM(item.totalPrice)
                )
                FROM Transaction t
                  JOIN t.items item
                  JOIN item.product p
                WHERE t.transactionType = 'DESPESA'
                  AND p.category.name = :category
                  AND t.date BETWEEN :start AND :end
            """)
    CategoryAmountOutputDTO sumPurchasesByCategory(
            @Param("category") String category,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );


    /// ///////////////////////////////////////////////////////////////////////////////////


    //Quanto foi comprado (todas a contas) - valor total de compras de um centro de custo - OK
    @Query("""
                SELECT new br.project.financial.dtos.transaction.output.TotalAmountOutputDTO(
                    :costCenterName,                          
                    SUM(item.quantity * item.unitPrice)       
                )
                FROM Transaction t
                JOIN t.items item
                JOIN t.costCenter cc
                JOIN t.expensePurpose ep
                WHERE
                  t.transactionType       = :transactionType
                  AND ep.name             = :expensePurposeName   
                  AND cc.name             = :costCenterName       
                  AND t.date BETWEEN :start AND :end
            """)
    TotalAmountOutputDTO sumPurchasesByPurposeAndCenterAndPeriod(
            @Param("transactionType") TransactionType transactionType,
            @Param("expensePurposeName") String expensePurposeName,
            @Param("costCenterName") String costCenterName,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    /// ///////////////////////////////////////////////////////////////////////////////////
    //## TRAZ VALOR TOTAL DE UM TIPO DE TRANSICAO, CATEGORIA DO PRODUTO, EXPENSE_PORPUSE e POR QUAL CENTRO DE CUSTO E PERIODO
    //# Quanto foi comprado de algodao (essa query é pra qualquer tipo de produto)
    @Query("""
                SELECT new br.project.financial.dtos.transaction.output.FilteredTotalOutputDTO(
                    t.transactionType,           
                    ep.name,                     
                    c.name,                      
                    cc.name,                    
                    SUM(item.quantity * item.unitPrice)
                )
                FROM Transaction t
                JOIN t.items item
                JOIN item.product p
                JOIN p.category c
                JOIN t.costCenter cc
                LEFT JOIN t.expensePurpose ep
                WHERE
                  t.transactionType      = :transactionType
                  AND ep.name            = :expensePurposeName
                  AND c.name             = :categoryName
                  AND cc.name            = :costCenterName
                  AND t.date BETWEEN :start AND :end
            """)
    FilteredTotalOutputDTO sumByTypePurposeCategoryCenterAndPeriod(
            @Param("transactionType") TransactionType transactionType,
            @Param("expensePurposeName") String expensePurposeName,
            @Param("categoryName") String categoryName,
            @Param("costCenterName") String costCenterName,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    /// ///////////////////////////////////////////////////////////////////////////////////
    //## TRAZ VALOR TOTAL de todas despesas DE UM CENTRO DE CUSTO
    //## Quanto foi pago  (todas a contas)
    @Query("""
                SELECT new br.project.financial.dtos.transaction.output.TotalAmountOutputDTO(
                    :costCenterName,
                    SUM(item.quantity * item.unitPrice)
                )
                FROM Transaction t
                JOIN t.items item
                JOIN t.costCenter cc
                JOIN t.expensePurpose ep
                WHERE
                  t.transactionType = :transactionType
                  AND ep.name        = :expensePurposeName
                  AND cc.name        = :costCenterName
                  AND t.date BETWEEN :start AND :end
            """)
    TotalAmountOutputDTO sumExpensesByPurposeAndCenterAndPeriod(
            @Param("transactionType") TransactionType transactionType,
            @Param("expensePurposeName") String expensePurposeName,
            @Param("costCenterName") String costCenterName,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    /// //////////////////////////////////////////////////////////////////////////////////

    // Quanto foi vendido em valor
    // valor total de vendas de uma categoria de produto de um centro de custo
    @Query("""
                SELECT new br.project.financial.dtos.transaction.output.TotalAmountOutputDTO(
                    :costCenterName,                  
                    SUM(item.quantity * item.unitPrice)
                )
                FROM Transaction t
                JOIN t.items item
                JOIN item.product p
                JOIN p.category c
                JOIN t.costCenter cc
                WHERE
                  t.transactionType = :transactionType
                  AND c.name         = :categoryName
                  AND cc.name        = :costCenterName
                  AND t.date BETWEEN :start AND :end
            """)
    TotalAmountOutputDTO sumSalesByCategoryAndCenterAndPeriod(
            @Param("transactionType") TransactionType transactionType,
            @Param("categoryName")    String categoryName,
            @Param("costCenterName")  String costCenterName,
            @Param("start")           LocalDate start,
            @Param("end")             LocalDate end
    );


    /// //////////////////////////////////////////////////////////////////////////////////
    //#Quanto foi vendido em KG
    //#valor total de vendas de uma categoria de produto de um centro de custo por kg
    @Query("""
                SELECT new br.project.financial.dtos.transaction.output.TotalAmountOutputDTO(
                    :costCenterName,                  
                    SUM(item.quantity)
                )
                FROM Transaction t
                JOIN t.items item
                JOIN item.product p
                JOIN p.category c
                JOIN t.costCenter cc
                WHERE
                  t.transactionType     = :transactionType
                  AND c.name            = :categoryName
                  AND cc.name           = :costCenterName
                  AND item.quantityUnit = 'KG'
                  AND t.date BETWEEN :start AND :end
            """)
    TotalAmountOutputDTO sumQuantitySoldByCategoryAndCenterAndPeriod(
            @Param("transactionType") TransactionType transactionType,
            @Param("categoryName")    String categoryName,
            @Param("costCenterName")  String costCenterName,
            @Param("start")           LocalDate start,
            @Param("end")             LocalDate end
    );





    /// //////////////////////////////////////////////////////////////////////////////////
    // RETORNA TODAS TRANSACTIONS
    @Query("""
                SELECT new br.project.financial.dtos.transaction.output.TransactionDetailedDTO(
                    t.id,
                    t.transactionType,           
                    t.customer,                  
                    c.name,                      
                    cc.name,                    
                    SUM(item.quantity * item.unitPrice),  
                    b.name,                      
                    t.date                     
                )
                FROM Transaction t
                JOIN t.items item
                JOIN item.product p
                JOIN p.category c
                JOIN t.costCenter cc
                JOIN t.branch b
                GROUP BY
                    t.id,
                    t.transactionType,
                    t.customer,
                    c.name,
                    cc.name,
                    b.name,
                    t.date
                ORDER BY t.id
            """)
    List<TransactionDetailedDTO> findAllTransactionSummaries();



}




