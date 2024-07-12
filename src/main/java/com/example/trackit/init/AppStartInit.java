package com.example.trackit.init;

import com.example.trackit.model.dto.UserRegisterDto;
import com.example.trackit.model.entity.Category;
import com.example.trackit.model.entity.Role;
import com.example.trackit.repository.CategoryRepository;
import com.example.trackit.repository.RoleRepository;
import com.example.trackit.repository.UserRepository;
import com.example.trackit.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AppStartInit implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final Map<String, String> categoryDescriptions = Map.ofEntries(
            Map.entry("Groceries", "Expenses for purchasing food and household supplies."),
            Map.entry("Utilities", "Monthly expenses for electricity, water, gas, and other utilities."),
            Map.entry("Rent", "Monthly payment for housing accommodation."),
            Map.entry("Transportation", "Costs for public transport, fuel, car maintenance, and other transportation-related expenses."),
            Map.entry("Entertainment", "Expenses for movies, concerts, events, and other leisure activities."),
            Map.entry("Healthcare", "Medical expenses including doctor visits, medications, and health insurance."),
            Map.entry("Insurance", "Payments for health, home, auto, and other types of insurance."),
            Map.entry("Debt Repayment", "Payments made towards reducing loans, credit card balances, and other debts."),
            Map.entry("Education", "Expenses for tuition, books, and other educational materials."),
            Map.entry("Dining Out", "Expenses for eating at restaurants and cafes."),
            Map.entry("Clothing", "Expenses for purchasing clothes and accessories."),
            Map.entry("Gifts", "Money spent on gifts for family, friends, and special occasions."),
            Map.entry("Other", "Money left")
    );

    public AppStartInit(RoleRepository roleRepository, CategoryRepository categoryRepository, UserRepository userRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        if (roleRepository.count() == 0){
            roleRepository.save(new Role("ADMIN"));
            roleRepository.save(new Role("USER"));
        }
        if (userRepository.count() == 0){
            userService.registerUser(new UserRegisterDto("admin_1", "adminov@gmail.com", "admin11", "admin11"));
        }
        if (categoryRepository.count() == 0){
            for (String name : categoryDescriptions.keySet()) {
                Category category = new Category(name, categoryDescriptions.get(name));
                category.setUser(userRepository.findById(1L).get());
                categoryRepository.save(category);
            }
        }
    }
}
