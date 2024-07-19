package com.example.trackit.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AddFromBudget {

    @NotNull
    private String categoryName;
    @Positive
    @NotNull
    private BigDecimal newAmount;


}
