package com.example.trackit.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Month;

@Entity
@Table(name = "users_progres")
@Getter
@Setter
@NoArgsConstructor
public class UserProgress extends BaseEntity{
    @ManyToOne
    private User user;
    @Column(name = "spent_amount")
    private BigDecimal spentAmount = BigDecimal.ZERO;
    @Column(name = "saved_amount")
    private BigDecimal savedAmount = BigDecimal.ZERO;
    private Month month;

}
