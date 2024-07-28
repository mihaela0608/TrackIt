package com.example.trackit.service;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.example.trackit.model.dto.ViewExpensesDetails;
import com.example.trackit.model.entity.Category;
import com.example.trackit.model.entity.Expense;
import com.example.trackit.model.entity.User;
import com.example.trackit.repository.BudgetRepository;
import com.example.trackit.repository.CategoryRepository;
import com.example.trackit.repository.ExpenseRepository;
import com.example.trackit.service.impl.ExpenseServiceImpl;
import com.example.trackit.service.session.UserHelperService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class TestExpenseServiceImpl {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserHelperService userHelperService;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private BudgetRepository budgetRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ExpenseServiceImpl expenseService;



    @Test
    void testAllDetails() {
         User  mockUser = new User();
        mockUser.setId(1L);
        Expense mockExpense1 = new Expense();
        mockExpense1.setId(1L);
        mockExpense1.setUser(mockUser);
        Category category1 = new Category();
        category1.setName("Food");
        mockExpense1.setCategory(category1);

        Expense mockExpense2 = new Expense();
        mockExpense2.setId(2L);
        mockExpense2.setUser(mockUser);
        Category category2 = new Category();
        category2.setName("Transport");
        mockExpense2.setCategory(category2);
        ViewExpensesDetails mockViewExpensesDetails1;
        mockViewExpensesDetails1 = new ViewExpensesDetails();
        mockViewExpensesDetails1.setCategoryName("Food");

        ViewExpensesDetails mockViewExpensesDetails2 = new ViewExpensesDetails();
        mockViewExpensesDetails2.setCategoryName("Transport");
        when(expenseRepository.findAll()).thenReturn(Arrays.asList(mockExpense1, mockExpense2));
        when(userHelperService.getUser()).thenReturn(mockUser);
        when(modelMapper.map(mockExpense1, ViewExpensesDetails.class)).thenReturn(mockViewExpensesDetails1);
        when(modelMapper.map(mockExpense2, ViewExpensesDetails.class)).thenReturn(mockViewExpensesDetails2);

        List<ViewExpensesDetails> result = expenseService.allDetails();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Food", result.get(0).getCategoryName());
        Assertions.assertEquals("Transport", result.get(1).getCategoryName());
    }


}
