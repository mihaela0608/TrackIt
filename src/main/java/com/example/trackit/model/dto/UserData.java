package com.example.trackit.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserData {
    private long id;
    private BigDecimal lastMonthExpenses;
    private BigDecimal lastMonthSavings;

    public UserData(long id) {
        this.id = id;
    }
}
