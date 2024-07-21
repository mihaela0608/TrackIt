package com.example.trackit.service;

import com.example.trackit.model.dto.AddExpenseDto;
import com.example.trackit.model.dto.ViewExpenseDto;
import com.example.trackit.model.dto.ViewExpensesDetails;

import java.util.List;

public interface ExpenseService {
    boolean addExpense(AddExpenseDto addExpenseDto);
    List<ViewExpenseDto> allExpenses();
    List<ViewExpensesDetails> allDetails();
    void deleteAll();
}
