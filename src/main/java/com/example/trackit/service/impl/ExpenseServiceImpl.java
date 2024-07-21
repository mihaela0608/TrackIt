package com.example.trackit.service.impl;

import com.example.trackit.model.dto.AddExpenseDto;
import com.example.trackit.model.dto.ViewExpenseDto;
import com.example.trackit.model.dto.ViewExpensesDetails;
import com.example.trackit.model.entity.Budget;
import com.example.trackit.model.entity.Expense;
import com.example.trackit.model.entity.User;
import com.example.trackit.repository.BudgetRepository;
import com.example.trackit.repository.CategoryRepository;
import com.example.trackit.repository.ExpenseRepository;
import com.example.trackit.service.ExpenseService;
import com.example.trackit.service.session.UserHelperService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserHelperService userHelperService;
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserHelperService userHelperService, BudgetRepository budgetRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.expenseRepository = expenseRepository;
        this.userHelperService = userHelperService;
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public boolean addExpense(AddExpenseDto addExpenseDto) {
        User user = userHelperService.getUser();
        BigDecimal amount = addExpenseDto.getAmount();
        Optional<Budget> optionalBudget = isBudgetValid(addExpenseDto, user);
        if (optionalBudget.isEmpty()){
            return false;
        }
        boolean hadEnoughMoney = haveEnoughMoney(optionalBudget, amount);
        if (!hadEnoughMoney){
            return false;
        }
        optionalBudget.get().setSpentAmount(optionalBudget.get().getSpentAmount().add(addExpenseDto.getAmount()));
        budgetRepository.save(optionalBudget.get());
        Expense expense = modelMapper.map(addExpenseDto, Expense.class);
        expense.setCategory(categoryRepository.findByName(addExpenseDto.getCategoryName()).get());
        expense.setUser(user);
        expenseRepository.save(expense);
        return true;
    }

    @Override
    @Transactional
    public List<ViewExpenseDto> allExpenses() {
        return expenseRepository.findAll().stream()
                .filter(e -> e.getUser().getId() == userHelperService.getUser().getId())
                .map(e -> {
                    ViewExpenseDto viewExpenseDto = new ViewExpenseDto();
                    viewExpenseDto.setAmount(e.getAmount());
                    viewExpenseDto.setCategoryName(e.getCategory().getName());
                    return viewExpenseDto;
                }).toList();
    }

    @Override
    @Transactional
    public List<ViewExpensesDetails> allDetails() {
        return expenseRepository.findAll().stream().filter(e -> e.getUser().getId() == userHelperService.getUser().getId())
                .map(e -> {
                    ViewExpensesDetails expensesDetails = modelMapper.map(e, ViewExpensesDetails.class);
                    expensesDetails.setCategoryName(e.getCategory().getName());
                    return expensesDetails;
                }).toList();
    }

    @Override
    public void deleteAll() {
        expenseRepository.deleteAll();
    }

    private static boolean haveEnoughMoney(Optional<Budget> optionalBudget, BigDecimal amountForSpending) {
        Budget budget = optionalBudget.get();
        BigDecimal budgetAmount =budget.getAmount();
        BigDecimal spentAmount = budget.getSpentAmount();
        return amountForSpending.compareTo(budgetAmount.subtract(spentAmount)) <= 0;
    }

    private static Optional<Budget> isBudgetValid(AddExpenseDto addExpenseDto, User user) {
        return user.getBudgets().stream()
                .filter(b -> b.getCategory().getName().equals(addExpenseDto.getCategoryName()))
                .findFirst();
    }
}
