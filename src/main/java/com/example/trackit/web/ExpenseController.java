package com.example.trackit.web;

import com.example.trackit.model.dto.AddExpenseDto;
import com.example.trackit.model.entity.Budget;
import com.example.trackit.repository.BudgetRepository;
import com.example.trackit.service.ExpenseService;
import com.example.trackit.service.session.UserHelperService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {
    private final BudgetRepository budgetRepository;
    private final ExpenseService expenseService;
    private final UserHelperService userHelperService;

    public ExpenseController(BudgetRepository budgetRepository, ExpenseService expenseService, UserHelperService userHelperService) {
        this.budgetRepository = budgetRepository;
        this.expenseService = expenseService;
        this.userHelperService = userHelperService;
    }

    @GetMapping("/create")
    @Transactional
    public String viewExpenseAdding(Model model){
        if (!model.containsAttribute("expenseData")){
            model.addAttribute("expenseData", new AddExpenseDto());
        }
        model.addAttribute("categories", budgetRepository.findAll().stream()
                .filter(b -> b.getUser().getId() == userHelperService.getUser().getId())
                .map(Budget::getCategory));
        return "create-expense";
    }
    @PostMapping("/create")
    public String createExpense(@Valid AddExpenseDto expenseData, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("expenseData", expenseData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.expenseData", bindingResult);
            return "redirect:/expenses/create";
        }
        if (!expenseService.addExpense(expenseData)){
            redirectAttributes.addFlashAttribute("notValid", true);
            return "redirect:/expenses/create";
        }
        return "redirect:/home";
    }
    @GetMapping("/all")
    public String viewAll(Model model){
        model.addAttribute("expenses", expenseService.allDetails());
        return "all-expenses";
    }
}
