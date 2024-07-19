package com.example.trackit.model.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SavingDetails {
    private long id;
    private String goal;
    private BigDecimal savedAmount;
    private BigDecimal targetAmount;
    private String description;
}
