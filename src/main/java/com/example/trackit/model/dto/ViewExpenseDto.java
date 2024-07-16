package com.example.trackit.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ViewExpenseDto {
    private String categoryName;
    private BigDecimal amount;

}
