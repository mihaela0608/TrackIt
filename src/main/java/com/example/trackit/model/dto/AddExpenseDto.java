package com.example.trackit.model.dto;

import com.example.trackit.model.entity.Category;
import com.example.trackit.model.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AddExpenseDto {

    @NotBlank(message = "{addExpenseDto.categoryName.NotBlank}")
    private String categoryName;

    @NotNull(message = "{addExpenseDto.amount.NotNull}")
    @Positive(message = "{addExpenseDto.amount.Positive}")
    private BigDecimal amount;

    @Size(max = 120, message = "{addExpenseDto.description.Size}")
    private String description;
}
