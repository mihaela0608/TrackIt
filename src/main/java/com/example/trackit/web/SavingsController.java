package com.example.trackit.web;

import com.example.trackit.model.dto.AddSavingDto;
import com.example.trackit.service.BudgetService;
import com.example.trackit.service.SavingService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/savings")
public class SavingsController {
    private final SavingService savingService;
    private final BudgetService budgetService;

    public SavingsController(SavingService savingService, BudgetService budgetService) {
        this.savingService = savingService;
        this.budgetService = budgetService;
    }


    @GetMapping("/create")
    public String viewCreateSaving(Model model){
        if (!model.containsAttribute("savingData")){
            model.addAttribute("savingData", new AddSavingDto());
        }
        return "add-saving";
    }

    @PostMapping("/create")
    public String createSaving(@Valid AddSavingDto savingData, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("savingData", savingData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.savingData", bindingResult);
            return "redirect:/savings/create";
        }
        savingService.addSaving(savingData);
        return "redirect:/home";
    }
    @GetMapping("/all")
    public String viewAllSavings(Model model){
        model.addAttribute("savings", savingService.allDetails());
        return "all-savings";
    }

    @GetMapping("/choose/{id}")
    public String chooseType(@PathVariable Long id, Model model){
        if (!savingService.isSavingIdValidForUser(id)){
            throw new NullPointerException();
        }
        //TODO: MAKE IT BETTER
        model.addAttribute("id", id);
        return "choose-saving";
    }
    @PostMapping("/choose/{id}")
    public String selectType(@PathVariable Long id, @RequestParam("budget-source") String source){
        if (source.equals("budget")){
            return "redirect:/savings/add-amount/budget/" + id;
        } else if (source.equals("separate")) {
            return "redirect:/savings/add-amount/separate/" + id;
        }
        return "redirect:/home";
    }
    @GetMapping("/add-amount/budget/{id}")
    public String viewFromBudget(@PathVariable Long id, Model model){
        if (!savingService.isSavingIdValidForUser(id)){
            throw new NullPointerException();
        }
        model.addAttribute("budgets", budgetService.viewAll());
        model.addAttribute("id", id);

        return "add-from-budget";
    }
    @PostMapping("/add-amount/budget/{id}")
    public String addFromBudget(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes){
        if (!savingService.isSavingIdValidForUser(id)){
            throw new NullPointerException();
        }
        return "redirect:/savings/all";
    }

    @GetMapping("/add-amount/separate/{id}")
    public String viewSeparate(@PathVariable Long id, Model model){
        if (!savingService.isSavingIdValidForUser(id)){
            throw new NullPointerException();
        }
        model.addAttribute("id", id);
        return "add-separate";
    }
    @PostMapping("/add-amount/separate/{id}")
    public String addSeparate(@PathVariable Long id, @RequestParam BigDecimal amount, Model model, RedirectAttributes redirectAttributes){
        if (!savingService.isSavingIdValidForUser(id)){
            throw new NullPointerException();
        }
        boolean success= savingService.addSeparate(id, amount);
        if (!success){
            redirectAttributes.addFlashAttribute("invalid", true);
            return "redirect:/savings/add-amount/separate/" + id;
        }
        return "redirect:/savings/all";
    }

}
