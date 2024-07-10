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
@Table(name = "savings")
@Getter
@Setter
@NoArgsConstructor
public class Saving extends BaseEntity{
    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private String goal;//Цел на спестяването (напр. "заем за кола", "почивка")
    @Column(name = "saved_amount")
    private BigDecimal savedAmount;
    @Column(name = "target_amount", nullable = false)
    private BigDecimal targetAmount;
    private String description;
}
