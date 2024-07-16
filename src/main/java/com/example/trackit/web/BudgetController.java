package com.example.trackit.web;

import com.example.trackit.model.dto.AddBudgetDto;
import com.example.trackit.model.dto.ViewAllBudgetDto;
import com.example.trackit.repository.CategoryRepository;
import com.example.trackit.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class BudgetController {
    private final CategoryRepository categoryRepository;
    private final BudgetService budgetService;

    public BudgetController(CategoryRepository categoryRepository, BudgetService budgetService) {
        this.categoryRepository = categoryRepository;
        this.budgetService = budgetService;
    }

    @GetMapping("/create-budget")
    public String viewAddBudget(Model model){
        model.addAttribute("categories", categoryRepository.findAll());
        if (!model.containsAttribute("budgetData")){
            model.addAttribute("budgetData", new AddBudgetDto());
        }
        return "new_budget";
    }
    @PostMapping("/create-budget")
    public String addBudget(@Valid AddBudgetDto budgetData, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("budgetData", budgetData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.budgetData", bindingResult);
            return "redirect:/create-budget";
        }
        if (!budgetService.addBudget(budgetData)){
            redirectAttributes.addFlashAttribute("exists", true);
            return "redirect:/create-budget";
        }
        return "redirect:/home";
    }
    @GetMapping("all-budgets")
    public String viewAllBudgets(Model model)  {
        List<ViewAllBudgetDto> viewAllBudgetDtos = budgetService.viewAll();
        model.addAttribute("budgets", viewAllBudgetDtos);
        return "all-budgets";
    }

    @GetMapping("/edit-budget/{category}")
    public String viewEditBudget(@PathVariable String category){
        return "edit-budget";
    }
    @PostMapping("/edit-budget/{category}")
    public String editBudget(@PathVariable String category, BigDecimal amount, RedirectAttributes redirectAttributes){
        boolean edited = budgetService.editBudget(category, amount);
        if (!edited){
            redirectAttributes.addFlashAttribute("invalid", true);
            return "redirect:/edit-budget/" + category;
        }


        return "redirect:/all-budgets";
    }

}
