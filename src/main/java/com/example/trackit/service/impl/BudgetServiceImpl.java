package com.example.trackit.service.impl;

import com.example.trackit.model.dto.AddBudgetDto;
import com.example.trackit.model.dto.ViewAllBudgetDto;
import com.example.trackit.model.entity.Budget;
import com.example.trackit.model.entity.Category;
import com.example.trackit.model.entity.User;
import com.example.trackit.repository.BudgetRepository;
import com.example.trackit.repository.CategoryRepository;
import com.example.trackit.service.BudgetService;
import com.example.trackit.service.session.UserHelperService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserHelperService userHelperService;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    @Override
    @Transactional
    public boolean addBudget(AddBudgetDto addBudgetDto) {
        Budget budget = new Budget();
        budget.setAmount(addBudgetDto.getAmount());
        Optional<Category> category = categoryRepository.findAll().stream().filter(c -> c.getName().equals(addBudgetDto.getCategoryName()) && (c.getUser().getId() == 1 || c.getUser().getId() == userHelperService.getUser().getId())).findFirst();
        budget.setCategory(category.get());
        List<String> categories = userHelperService.getUser().getBudgets().stream().map(b -> b.getCategory().getName()).toList();
        if (categories.contains(budget.getCategory().getName())){
            return false;
        }
        budget.setUser(userHelperService.getUser());
        budgetRepository.save(budget);
        return true;
    }

    @Override
    @Transactional
    public List<ViewAllBudgetDto> viewAll() {
        User user = userHelperService.getUser();
        return user.getBudgets().stream().map(this::map).toList();

    }

    @Override
    @Transactional
    public boolean editBudget(String categoryName, BigDecimal amount) {
        Budget budget = userHelperService.getUser().getBudgets().stream()
                .filter(b -> b.getCategory().getName().equals(categoryName))
                .findFirst().orElseThrow();
        if (amount.compareTo(budget.getSpentAmount()) < 0) {
            return false;
        }
        budget.setAmount(amount);
        budgetRepository.save(budget);
        return true;
    }

    @Override
    public void deleteAll() {
        budgetRepository.deleteAll();
    }

    private ViewAllBudgetDto map(Budget budget){
        ViewAllBudgetDto budgetDto = modelMapper.map(budget, ViewAllBudgetDto.class);
        budgetDto.setSpentAmount(budget.getSpentAmount());
        return budgetDto;
    }
}
