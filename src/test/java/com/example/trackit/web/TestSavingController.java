package com.example.trackit.web;

import com.example.trackit.model.dto.AddFromBudget;
import com.example.trackit.model.dto.AddSavingDto;
import com.example.trackit.service.BudgetService;
import com.example.trackit.service.SavingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TestSavingController {

    @Mock
    private SavingService savingService;

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private SavingsController savingsController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(savingsController).build();
    }

    @Test
    void testViewCreateSaving() throws Exception {
        mockMvc.perform(get("/savings/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-saving"))
                .andExpect(model().attributeExists("savingData"));
    }

    @Test
    void testCreateSaving_withErrors() throws Exception {
        AddSavingDto savingData = new AddSavingDto();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/savings/create")
                        .flashAttr("savingData", savingData)
                        .flashAttr("org.springframework.validation.BindingResult.savingData", bindingResult))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/savings/create"));
    }



    @Test
    void testViewAllSavings() throws Exception {
        when(savingService.allDetails()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/savings/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("all-savings"))
                .andExpect(model().attributeExists("savings"));
    }

    @Test
    void testChooseType_validId() throws Exception {
        when(savingService.isSavingIdValidForUser(anyLong())).thenReturn(true);

        mockMvc.perform(get("/savings/choose/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("choose-saving"))
                .andExpect(model().attribute("id", 1L));
    }



    @Test
    void testSelectType_budget() throws Exception {
        mockMvc.perform(post("/savings/choose/1")
                        .param("budget-source", "budget"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/savings/add-amount/budget/1"));
    }

    @Test
    void testSelectType_separate() throws Exception {
        mockMvc.perform(post("/savings/choose/1")
                        .param("budget-source", "separate"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/savings/add-amount/separate/1"));
    }

    @Test
    void testViewFromBudget_validId() throws Exception {
        when(savingService.isSavingIdValidForUser(anyLong())).thenReturn(true);
        when(budgetService.viewAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/savings/add-amount/budget/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-from-budget"))
                .andExpect(model().attributeExists("fromBudget"))
                .andExpect(model().attributeExists("budgets"))
                .andExpect(model().attribute("id", 1L));
    }

    @Test
    void testAddFromBudget_withErrors() throws Exception {
        AddFromBudget fromBudget = new AddFromBudget();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(savingService.isSavingIdValidForUser(anyLong())).thenReturn(true);

        mockMvc.perform(post("/savings/add-amount/budget/1")
                        .flashAttr("fromBudget", fromBudget)
                        .flashAttr("org.springframework.validation.BindingResult.fromBudget", bindingResult))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/savings/add-amount/budget/1"));
    }





    @Test
    void testViewSeparate_validId() throws Exception {
        when(savingService.isSavingIdValidForUser(anyLong())).thenReturn(true);

        mockMvc.perform(get("/savings/add-amount/separate/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-separate"))
                .andExpect(model().attribute("id", 1L));
    }

    @Test
    void testAddSeparate_success() throws Exception {
        when(savingService.isSavingIdValidForUser(anyLong())).thenReturn(true);
        when(savingService.addSeparate(anyLong(), any(BigDecimal.class))).thenReturn(true);

        mockMvc.perform(post("/savings/add-amount/separate/1")
                        .param("amount", "100.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/savings/all"));
    }

    @Test
    void testAddSeparate_failure() throws Exception {
        when(savingService.isSavingIdValidForUser(anyLong())).thenReturn(true);
        when(savingService.addSeparate(anyLong(), any(BigDecimal.class))).thenReturn(false);

        mockMvc.perform(post("/savings/add-amount/separate/1")
                        .param("amount", "100.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/savings/add-amount/separate/1"))
                .andExpect(flash().attribute("invalid", true));
    }

    @Test
    void testDeleteSaving() throws Exception {
        mockMvc.perform(delete("/savings/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/savings/all"));
    }
}
