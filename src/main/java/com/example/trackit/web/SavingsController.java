package com.example.trackit.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/savings")
public class SavingsController {
    @GetMapping("/create")
    public String addSaving(){
        return "add-saving";
    }

    









    //TODO: Make the logic for saving

    //TODO: MAKE THE INTER. FOR ALL FORMS
}
