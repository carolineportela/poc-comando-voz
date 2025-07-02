//package br.project.financial.repositories.transaction;
//
//import br.project.financial.dtos.transaction.output.TransactionRevenueOutputDTO;
//import br.project.financial.dtos.transaction.output.BranchTransactionRevenueOutputDTO;
//import br.project.financial.dtos.transaction.output.BranchAmountOutputDTO;
//import br.project.financial.entities.Transaction;
//import br.project.financial.enums.TransactionType;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Repository
//public interface TransactionRepository extends JpaRepository<Transaction, Long> {
//
//    @Query("""
//                SELECT new br.project.financial.dtos.transaction.output.TransactionRevenueOutputDTO(
//                    t.transactionType,
//                    :startDate,
//                    :endDate,
//                    SUM(t.amount)
//                )
//                FROM Transaction t
//                WHERE t.transactionType = :transactionType
//                  AND t.date BETWEEN :startDate AND :endDate
//            """)
//    TransactionRevenueOutputDTO sumByTypeAndPeriod(
//            @Param("transactionType") TransactionType transactionType,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate
//    );
//
//    @Query("""
//                SELECT new br.project.financial.dtos.transaction.output.BranchTransactionRevenueOutputDTO(
//                    t.transactionType,
//                    t.branch,
//                    :startDate,
//                    :endDate,
//                    SUM(t.amount)
//                )
//                FROM Transaction t
//                WHERE t.transactionType = :transactionType
//                  AND t.branch = :branch
//                  AND t.date BETWEEN :startDate AND :endDate
//            """)
//    BranchTransactionRevenueOutputDTO sumByTypeBranchAndPeriod(
//            @Param("transactionType") TransactionType transactionType,
//            @Param("branch") String branch,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate
//    );
//
//    @Query("""
//               SELECT new br.project.financial.dtos.transaction.output.BranchAmountOutputDTO(
//                    t.branch,
//                    SUM(t.amount)
//                )
//                FROM Transaction t
//                WHERE t.transactionType = :transactionType
//                GROUP BY t.branch
//                ORDER BY SUM(t.amount) DESC
//            """)
//    List<BranchAmountOutputDTO> sumByTypeGroupedByBranchOrderedDesc(
//            @Param("transactionType") TransactionType transactionType
//    );
//
//    @Query("""
//                SELECT new br.project.financial.dtos.transaction.output.BranchAmountOutputDTO(
//                    t.branch,
//                    SUM(t.amount)
//                )
//                FROM Transaction t
//                WHERE t.transactionType = :transactionType
//                  AND t.date BETWEEN :startDate AND :endDate
//                GROUP BY t.branch
//                ORDER BY SUM(t.amount) DESC
//            """)
//    List<BranchAmountOutputDTO> findTop2BranchesByTransactionTypeAndDateBetween(
//            @Param("transactionType") TransactionType transactionType,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate
//    );
//
//    List<Transaction> findByBranchAndDateBetweenOrderByDateAsc(
//            @Param("branch") String branch,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate
//    );
//}
package br.project.financial.repositories.transaction;

import br.project.financial.dtos.category.output.CategoryAmountOutputDTO;
import br.project.financial.dtos.costcenter.output.CostCenterAmountOutputDTO;
import br.project.financial.dtos.transaction.output.TotalAmountOutputDTO;
import br.project.financial.dtos.transaction.output.TransactionRevenueOutputDTO;
import br.project.financial.dtos.transaction.output.BranchTransactionRevenueOutputDTO;
import br.project.financial.dtos.transaction.output.BranchAmountOutputDTO;
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

    // 1) Soma de valor por tipo de transação e período
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

    // 2) Soma de valor por tipo, filial e período
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

    // 3) Total pago (filtrado por status e período) por centro de custo
    @Query("""
                SELECT new br.project.financial.dtos.transaction.output.BranchAmountOutputDTO(
                    t.costCenter.name,
                    SUM(item.totalPrice)
                )
                FROM Transaction t
                JOIN t.items item
                WHERE t.transactionType = 'ENTRADA'
                  AND t.paymentStatus = 'PAGA'
                  AND t.paymentDate BETWEEN :startDate AND :endDate
                GROUP BY t.costCenter.name
            """)
    List<BranchAmountOutputDTO> sumPaidPurchasesByCostCenter(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // 4) Vendas: valor por categoria no período
    @Query("""
                SELECT new br.project.financial.dtos.transaction.output.BranchAmountOutputDTO(
                    c.name,
                    SUM(item.totalPrice)
                )
                FROM Transaction t
                JOIN t.items item
                JOIN item.product p
                JOIN p.category c
                WHERE t.transactionType = 'VENDA'
                  AND t.date BETWEEN :startDate AND :endDate
                GROUP BY c.name
            """)
    List<BranchAmountOutputDTO> sumSalesValueByCategory(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // 5) Vendas: quantidade por categoria no período
    @Query("""
                SELECT new br.project.financial.dtos.transaction.output.BranchAmountOutputDTO(
                    c.name,
                    SUM(item.quantity)
                )
                FROM Transaction t
                JOIN t.items item
                JOIN item.product p
                JOIN p.category c
                WHERE t.transactionType = 'VENDA'
                  AND t.date BETWEEN :startDate AND :endDate
                GROUP BY c.name
            """)
    List<BranchAmountOutputDTO> sumSalesQuantityByCategory(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // 6) Top 2 filiais por tipo e período
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

    // 7) Filial com maior total por tipo (top 1)
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

    // 8) Lista transações detalhadas de uma filial por período
    List<Transaction> findByBranch_NameAndDateBetweenOrderByDateAsc(
            String branchName,
            LocalDate startDate,
            LocalDate endDate
    );

    // Soma de compras (DESPESA) por categoria no período
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

    // Soma de todas as compras (DESPESA) por centro de custo no período
//    @Query("""
//                SELECT new br.project.financial.dtos.costcenter.output.CostCenterAmountOutputDTO(
//                    t.costCenter.name,
//                    SUM(item.totalPrice)
//                )
//                FROM Transaction t
//                  JOIN t.items item
//                WHERE t.transactionType = 'DESPESA'
//                  AND t.date BETWEEN :start AND :end
//                GROUP BY t.costCenter.name
//            """)
//    List<CostCenterAmountOutputDTO> sumPurchasesByCostCenter(
//            @Param("start") LocalDate start,
//            @Param("end")   LocalDate end
//    );


    // NOVAS CONSULTAS PRO CONTINUACAO POC
    // --- NOVO: compras filtradas por tipo, categoria e período ---
    @Query("""
                SELECT new br.project.financial.dtos.category.output.CategoryAmountOutputDTO(
                    p.category.name,
                    SUM(item.quantity * item.unitPrice)
                )
                FROM Transaction t
                JOIN t.items item
                JOIN item.product p
                JOIN p.category c
                JOIN t.branch b
                WHERE t.transactionType = :transactionType
                  AND c.name = :category
                  AND b.name = :branch
                  AND t.date BETWEEN :start AND :end
            """)
    CategoryAmountOutputDTO sumPurchasesByTypeCategoryAndBranch(
            @Param("transactionType") TransactionType transactionType,
            @Param("category") String category,
            @Param("branch") String branch,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

//    @Query("""
//                SELECT new br.project.financial.dtos.transaction.output.TotalAmountOutputDTO(
//                    'Despesas pagas',
//                    SUM(ti.quantity * ti.unitPrice)
//                )
//                FROM Transaction t
//                JOIN t.items ti
//                WHERE t.transactionType = 'DESPESA'
//                  AND t.paymentStatus = 'PAGA'
//                  AND t.paymentDate BETWEEN :start AND :end
//            """)
//    TotalAmountOutputDTO sumPaidExpensesBetweenDates(
//            @Param("start") LocalDate start,
//            @Param("end") LocalDate end
//    );

    // Sem filtro por branch
    @Query("""
    SELECT new br.project.financial.dtos.transaction.output.TotalAmountOutputDTO(
        'Despesas pagas',
        SUM(ti.quantity * ti.unitPrice)
    )
    FROM Transaction t
    JOIN t.items ti
    WHERE t.transactionType = 'DESPESA'
      AND t.paymentStatus = 'PAGA'
      AND t.paymentDate BETWEEN :start AND :end
""")
    TotalAmountOutputDTO sumPaidExpensesBetweenDates(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    // Com filtro por branch
    @Query("""
                SELECT new br.project.financial.dtos.transaction.output.TotalAmountOutputDTO(
                    'Despesas pagas',
                    SUM(ti.quantity * ti.unitPrice)
                )
                FROM Transaction t
                JOIN t.items ti
                WHERE t.transactionType = 'DESPESA'
                  AND t.paymentStatus = 'PAGA'
                  AND t.paymentDate BETWEEN :start AND :end
                  AND t.branch.name = :branch
            """)
    TotalAmountOutputDTO sumPaidExpensesBetweenDatesAndBranch(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("branch") String branch
    );



}
