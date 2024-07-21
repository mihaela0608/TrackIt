package com.example.trackit.web;

import com.example.trackit.model.dto.AddCategoryDto;
import com.example.trackit.service.CategoryService;
import com.example.trackit.service.session.UserHelperService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final UserHelperService userHelperService;

    public CategoryController(CategoryService categoryService, UserHelperService userHelperService) {
        this.categoryService = categoryService;
        this.userHelperService = userHelperService;
    }

    @GetMapping("/create")
    public String viewCreateCategory(Model model){
        if (!model.containsAttribute("categoryData")){
            model.addAttribute("categoryData", new AddCategoryDto());
        }
        return "create-category";
    }
    @PostMapping("/create")
    public String createCategory(@Valid AddCategoryDto categoryData, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("categoryData", categoryData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoryData", bindingResult);
            return "redirect:/categories/create";
        }
        boolean added = categoryService.addNewCategory(categoryData);
        if (!added){
            redirectAttributes.addFlashAttribute("occupied", true);
            return "redirect:/categories/create";
        }
        return "redirect:/home";
    }

    @GetMapping("/all")
    public String viewAll(Model model){
        model.addAttribute("categories", categoryService.getAllDetails());
        model.addAttribute("id", userHelperService.getUser().getId());
        return "all-categories";
    }

}
