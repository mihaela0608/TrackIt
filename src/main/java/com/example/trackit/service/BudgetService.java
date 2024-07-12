package com.example.trackit.service;

import com.example.trackit.model.dto.AddBudgetDto;

public interface BudgetService {
    boolean addBudget(AddBudgetDto addBudgetDto);
}
