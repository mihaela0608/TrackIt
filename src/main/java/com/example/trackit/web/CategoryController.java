package com.example.trackit.web;

import com.example.trackit.model.dto.AddCategoryDto;
import com.example.trackit.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/create-category")
    public String viewCreateCategory(Model model){
        if (!model.containsAttribute("categoryData")){
            model.addAttribute("categoryData", new AddCategoryDto());
        }
        return "create-category";
    }
    @PostMapping("/create-category")
    public String createCategory(@Valid AddCategoryDto categoryData, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("categoryData", categoryData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoryData", bindingResult);
            return "redirect:/create-category";
        }
        boolean added = categoryService.addNewCategory(categoryData);
        if (!added){
            redirectAttributes.addFlashAttribute("occupied", true);
            return "redirect:/create-category";
        }
        return "redirect:/home";
    }
}
