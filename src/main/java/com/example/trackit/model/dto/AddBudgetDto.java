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
    @NotNull(message = "Category is required")
    private String categoryName;
    @Positive(message = "Amount should be positive number")
    @NotNull
    private BigDecimal amount;

}
