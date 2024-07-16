package com.example.trackit.model.dto;

import com.example.trackit.model.entity.Category;
import com.example.trackit.model.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AddExpenseDto {

    @NotBlank(message = "Category is required")
    private String categoryName;
    @NotNull
    @Positive(message = "Amount should be a positive number")
    private BigDecimal amount;
    private String description;
}
