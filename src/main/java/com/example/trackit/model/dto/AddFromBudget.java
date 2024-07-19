package com.example.trackit.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class AddFromBudget {

    @NotNull
    private String categoryName;
    @Positive
    @NotNull
    private BigDecimal newAmount;


}
