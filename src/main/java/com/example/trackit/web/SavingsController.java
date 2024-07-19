package com.example.trackit.web;

import com.example.trackit.model.dto.AddSavingDto;
import com.example.trackit.service.SavingService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/savings")
public class SavingsController {
    private final SavingService savingService;

    public SavingsController(SavingService savingService) {
        this.savingService = savingService;
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









    //TODO: Make the logic for saving

    //TODO: MAKE THE INTER. FOR ALL FORMS
}
