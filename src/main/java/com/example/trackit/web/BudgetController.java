package com.example.trackit.web;

import com.example.trackit.model.dto.AddBudgetDto;
import com.example.trackit.repository.CategoryRepository;
import com.example.trackit.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
}
