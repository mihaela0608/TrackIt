package com.example.trackit.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "budgets")
@Getter
@Setter
@NoArgsConstructor
public class Budget extends BaseEntity{

    @ManyToOne(optional = false)
    private User user;



    @ManyToOne(optional = false)
    private Category category;

    @Column(nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;
    @Column(name = "spent-amount")
    private BigDecimal spentAmount = BigDecimal.ZERO;

    public Budget(BigDecimal amount, BigDecimal spentAmount, Category category) {
        this.amount = amount;
        this.spentAmount = spentAmount;
        this.category = category;
    }
}
