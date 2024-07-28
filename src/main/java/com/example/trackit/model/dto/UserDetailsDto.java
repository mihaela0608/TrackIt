package com.example.trackit.model.dto;

import com.example.trackit.model.entity.Expense;
import com.example.trackit.model.entity.Saving;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailsDto {
    private long id;
    private String username;
    private String email;
    private String registrationDate;
    private long hereFrom;
    private BigDecimal expensesSum;
    private BigDecimal savingsSum;
    private BigDecimal lastMonthExpenses;
    private BigDecimal lastMonthSavings;

}
