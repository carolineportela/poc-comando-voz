package br.project.financial.entities;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "transaction_items")
@Getter @Setter
public class TransactionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false, precision = 19, scale = 3)
    private BigDecimal quantity;

    @Column(name = "quantity_unit", nullable = false, length = 5)
    private String quantityUnit;

    @Column(name = "unit_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal unitPrice;

    @Column(name = "total_price", precision = 19, scale = 4,
            columnDefinition = "DECIMAL(19,4) GENERATED ALWAYS AS (quantity * unit_price) STORED")
    private BigDecimal totalPrice;
}
