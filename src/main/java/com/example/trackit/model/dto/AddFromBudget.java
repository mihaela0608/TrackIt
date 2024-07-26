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

    @NotNull(message = "{addFromBudget.categoryName.NotNull}")
    private String categoryName;

    @Positive(message = "{addFromBudget.newAmount.Positive}")
    @NotNull(message = "{addFromBudget.newAmount.NotNull}")
    private BigDecimal newAmount;

    public AddFromBudget(String categoryName, BigDecimal newAmount) {
        this.categoryName = categoryName;
        this.newAmount = newAmount;
    }
}
