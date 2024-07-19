package com.example.trackit.model.dto;

import com.example.trackit.model.entity.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AddBudgetDto {
    @NotNull(message = "{addBudgetDto.categoryName.NotNull}")
    private String categoryName;

    @NotNull(message = "{addBudgetDto.amount.NotNull}")
    @Positive(message = "{addBudgetDto.amount.Positive}")
    private BigDecimal amount;
}
