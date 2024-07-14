package com.example.trackit.service;

import com.example.trackit.model.dto.AddBudgetDto;
import com.example.trackit.model.dto.ViewAllBudgetDto;

import java.util.List;

public interface BudgetService {
    boolean addBudget(AddBudgetDto addBudgetDto);
    List<ViewAllBudgetDto> viewAll();
}
