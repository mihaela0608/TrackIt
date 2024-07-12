package com.example.trackit.service.impl;

import com.example.trackit.model.dto.AddBudgetDto;
import com.example.trackit.model.entity.Budget;
import com.example.trackit.repository.BudgetRepository;
import com.example.trackit.repository.CategoryRepository;
import com.example.trackit.service.BudgetService;
import com.example.trackit.service.session.UserHelperService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserHelperService userHelperService;
    private final CategoryRepository categoryRepository;
    @Override
    @Transactional
    public boolean addBudget(AddBudgetDto addBudgetDto) {
        Budget budget = new Budget();
        budget.setAmount(addBudgetDto.getAmount());
        budget.setCategory(categoryRepository.findByName(addBudgetDto.getCategoryName()).get());
        List<String> categories = userHelperService.getUser().getBudgets().stream().map(b -> b.getCategory().getName()).toList();
        if (categories.contains(budget.getCategory().getName())){
            return false;
        }
        budget.setUser(userHelperService.getUser());
        budgetRepository.save(budget);
        return true;
    }
}
