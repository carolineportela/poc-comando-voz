//package br.project.financial.entities;
//
//import br.project.financial.enums.TransactionType;
//import br.project.financial.util.TransactionTypeConverter;
//import jakarta.persistence.Column;
//import jakarta.persistence.Convert;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//@Entity
//@Table(name = "transactions")
//@Getter
//@Setter
//public class Transaction {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Convert(converter = TransactionTypeConverter.class)
//    @Column(name = "transaction_type", nullable = false)
//    private TransactionType transactionType;
//
//    @Column(name = "customer")
//    private String customer;
//
//    @Column(name = "category")
//    private String category;
//
//    @Column(name = "amount", precision = 19, scale = 2)
//    private BigDecimal amount;
//
//
//    @Column(name = "branch")
//    private String branch;
//
//    @Column(name = "date")
//    private LocalDate date;
//}

package br.project.financial.entities;

import br.project.financial.enums.TransactionType;
import br.project.financial.util.TransactionTypeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "transactions")
@Getter @Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = TransactionTypeConverter.class)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "customer")
    private String customer;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cost_center_id")
    private CostCenter costCenter;

    @ManyToOne(optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "issue_status", length = 15)
    private String issueStatus;

    @Column(name = "payment_status", length = 15)
    private String paymentStatus;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

//    @ManyToOne(optional = false)
//    @JoinColumn(name = "expense_purposes_id")
//    private ExpensePurposes expensePurpose;

    @ManyToOne
    @JoinColumn(name = "expense_purpose_id",  // EXATAMENTE esse nome, sem "s"
            referencedColumnName = "id",
            nullable = true)
    private ExpensePurpose expensePurpose;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionItem> items = new ArrayList<>();
}
