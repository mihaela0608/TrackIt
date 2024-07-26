package com.example.trackit.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
@Getter
@Setter
public class Expense extends BaseEntity{

    @ManyToOne(optional = false)
    private User user;
    @ManyToOne(optional = false)
    private Category category;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private LocalDate date;
    private String description;

    public Expense() {
        this.date = LocalDate.now();
    }

    public Expense(Category category, BigDecimal amount) {
        this.date = LocalDate.now();
        this.category = category;
        this.amount = amount;
    }
}
