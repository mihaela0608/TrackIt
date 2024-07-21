package com.example.trackit.web;

import com.example.trackit.model.dto.UserRegisterDto;
import com.example.trackit.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    public String viewRegister(Model model){
        if (!model.containsAttribute("userData")){
            model.addAttribute("userData", new UserRegisterDto());
        }
        return "register";
    }
    @PostMapping("/register")
    public String doRegister(@Valid UserRegisterDto userData, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors() || !userData.getPassword().equals(userData.getConfirmPassword())){
            redirectAttributes.addFlashAttribute("userData", userData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userData", bindingResult);
            return "redirect:/register";
        }
        boolean success = userService.registerUser(userData);
        if (!success){
            redirectAttributes.addFlashAttribute("userData", userData);
            redirectAttributes.addFlashAttribute("occupied", true);
            return "redirect:/register";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("invalid", true);
        return "redirect:/login";
    }
    @GetMapping("/logout")
    public String logout(){
        return "redirect:/";
    }
    @GetMapping("/profile")
    public String viewProfile(Model model){
        model.addAttribute("userData", userService.getUserDetails());
        return "user-profile";
    }
}