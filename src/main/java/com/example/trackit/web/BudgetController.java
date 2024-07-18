package com.example.trackit.web;

import com.example.trackit.model.dto.AddBudgetDto;
import com.example.trackit.model.dto.ViewAllBudgetDto;
import com.example.trackit.repository.CategoryRepository;
import com.example.trackit.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/budgets")
public class BudgetController {
    private final CategoryRepository categoryRepository;
    private final BudgetService budgetService;

    public BudgetController(CategoryRepository categoryRepository, BudgetService budgetService) {
        this.categoryRepository = categoryRepository;
        this.budgetService = budgetService;
    }

    @GetMapping("/create")
    public String viewAddBudget(Model model){
        model.addAttribute("categories", categoryRepository.findAll());
        if (!model.containsAttribute("budgetData")){
            model.addAttribute("budgetData", new AddBudgetDto());
        }
        return "new-budget";
    }
    @PostMapping("/create")
    public String addBudget(@Valid AddBudgetDto budgetData, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("budgetData", budgetData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.budgetData", bindingResult);
            return "redirect:/budgets/create";
        }
        if (!budgetService.addBudget(budgetData)){
            redirectAttributes.addFlashAttribute("exists", true);
            return "redirect:/budgets/create";
        }
        return "redirect:/home";
    }
    @GetMapping("all")
    public String viewAllBudgets(Model model)  {
        List<ViewAllBudgetDto> viewAllBudgetDtos = budgetService.viewAll();
        model.addAttribute("budgets", viewAllBudgetDtos);
        return "all-budgets";
    }

    @GetMapping("/edit/{category}")
    public String viewEditBudget(@PathVariable String category){
        return "edit-budget";
    }
    @PostMapping("/edit/{category}")
    public String editBudget(@PathVariable String category, @RequestParam BigDecimal amount, RedirectAttributes redirectAttributes){
        boolean edited = budgetService.editBudget(category, amount);
        if (!edited){
            redirectAttributes.addFlashAttribute("invalid", true);
            return "redirect:/budgets/edit/" + category;
        }

        return "redirect:/budgets/all";
    }

}
