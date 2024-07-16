package com.example.trackit.web;

import com.example.trackit.service.CategoryService;
import com.example.trackit.service.ExpenseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final ExpenseService expenseService;
    private final CategoryService categoryService;

    public HomeController(ExpenseService expenseService, CategoryService categoryService) {
        this.expenseService = expenseService;
        this.categoryService = categoryService;
    }

    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("expenses", expenseService.allExpenses());
        model.addAttribute("categories", categoryService.getAllForUser());
        return "home";
    }
}
