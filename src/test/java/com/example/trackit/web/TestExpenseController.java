package com.example.trackit.web;

import com.example.trackit.model.dto.AddExpenseDto;
import com.example.trackit.model.entity.Budget;
import com.example.trackit.model.entity.Category;
import com.example.trackit.model.entity.User;
import com.example.trackit.repository.BudgetRepository;
import com.example.trackit.service.ExpenseService;
import com.example.trackit.service.UserDataService;
import com.example.trackit.service.session.UserHelperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
class TestExpenseController {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private ExpenseService expenseService;

    @Mock
    private UserHelperService userHelperService;

    @InjectMocks
    private ExpenseController expenseController;

    @MockBean
    private UserDataService userDataService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(expenseController).build();
    }

    @Test
    void testViewExpenseAdding() throws Exception {
        User user = new User();
        user.setId(1L);
        when(userHelperService.getUser()).thenReturn(user);

        Budget budget = new Budget();
        Category category = new Category("Food", "Eating");
        budget.setCategory(category);
        budget.setUser(user);
        when(budgetRepository.findAll()).thenReturn(Arrays.asList(budget));

        mockMvc.perform(get("/expenses/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-expense"));

    }

    @Test
    void testCreateExpense_withErrors() throws Exception {
        AddExpenseDto expenseData = new AddExpenseDto();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String result = expenseController.createExpense(expenseData, bindingResult, redirectAttributes);

        assertEquals("redirect:/expenses/create", result);
    }



    @Test
    void testCreateExpense_invalidExpense() throws Exception {
        AddExpenseDto expenseData = new AddExpenseDto();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(expenseService.addExpense(any(AddExpenseDto.class))).thenReturn(false);

        String result = expenseController.createExpense(expenseData, bindingResult, redirectAttributes);

        assertEquals("redirect:/expenses/create", result);
    }

    @Test
    void testViewAll() throws Exception {
        when(expenseService.allDetails()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/expenses/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("all-expenses"));

    }
}
