package com.example.trackit.scheduling;

import com.example.trackit.repository.UserRepository;
import com.example.trackit.service.BudgetService;
import com.example.trackit.service.ExpenseService;
import com.example.trackit.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@AllArgsConstructor
public class MonthlyProgress {
    private final UserService userService;
    private final BudgetService budgetService;
    private final ExpenseService expenseService;
    private final UserRepository userRepository;
    @Scheduled(cron = "0 0 0 1 * ?")
    public void performMonthlyTask() {
        userRepository.findAll()
                       .forEach(userService::updateUserProgress);
        expenseService.deleteAll();
        budgetService.deleteAll();
        System.out.println("Executing monthly task at the end of the month");
    }
}
