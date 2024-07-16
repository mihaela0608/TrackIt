package com.example.trackit.model.dto;

import com.example.trackit.model.entity.Category;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ViewExpensesDetails {
    private String categoryName;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
}
