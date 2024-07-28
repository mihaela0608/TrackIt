package com.example.trackit.service;

import com.example.trackit.model.dto.AddFromBudget;
import com.example.trackit.model.entity.Budget;
import com.example.trackit.model.entity.Category;
import com.example.trackit.model.entity.Saving;
import com.example.trackit.model.entity.User;
import com.example.trackit.repository.BudgetRepository;
import com.example.trackit.repository.CategoryRepository;
import com.example.trackit.repository.SavingRepository;
import com.example.trackit.repository.UserRepository;
import com.example.trackit.service.impl.SavingServiceImpl;
import com.example.trackit.service.session.UserHelperService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class TestSavingServiceImpl {
    @Autowired
    private SavingRepository savingRepository;
    @Mock
    private UserHelperService userHelperService;
    @Autowired
    private BudgetRepository budgetRepository;

    @InjectMocks
    private SavingServiceImpl savingService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private UserDataService userDataService;

    @BeforeEach
    void setUp(){
        categoryRepository.deleteAll();
        userRepository.deleteAll();
        savingRepository.deleteAll();
        budgetRepository.deleteAll();
        savingService = new SavingServiceImpl(
                savingRepository,
                new ModelMapper(),
                userHelperService,
                budgetRepository
        );

    }

    @Test
    void isSavingIdValidForUser(){
        Saving saving = new Saving("New dress", BigDecimal.valueOf(50));
        saving.setId(1);
        User user = new User();
        user.setSavings(List.of(saving));
        when(userHelperService.getUser()).thenReturn(user);

        boolean savingIdValidForUser = savingService.isSavingIdValidForUser(1);
        Assertions.assertTrue(savingIdValidForUser);
        boolean savingIdValidForUser2 = savingService.isSavingIdValidForUser(2);
        Assertions.assertFalse(savingIdValidForUser2);

    }

    @Test
    void testAddFromBudgetReturnsFalseWhenBudgetNotExists(){
        AddFromBudget addFromBudget = new AddFromBudget("Something strange", BigDecimal.TEN);
        User user = new User();
        when(userHelperService.getUser()).thenReturn(user);
        boolean added = savingService.addFromBudget(5L, addFromBudget);
        Assertions.assertFalse(added);
    }
    @Test
    void testAddFromBudgetReturnsFalseWhenBudgetHasNotEnoughMoney(){
        AddFromBudget addFromBudget = new AddFromBudget("Food", BigDecimal.TEN);
        User user = new User(
                "test", "test@test", "test123"
        );
        user = userRepository.save(user);
        when(userHelperService.getUser()).thenReturn(user);
        Category category = new Category("Food", "Eating");
        category.setUser(user);
        category = categoryRepository.save(category);
        Budget budget = new Budget(BigDecimal.TEN, BigDecimal.TEN, category);
        budget.setUser(user);
        boolean added = savingService.addFromBudget(1L, addFromBudget);
        Assertions.assertFalse(added);
    }

    @Test
    void testDeleteSavingThrowsWhenUserIsNotValid(){
        User user = new User(
                "test", "test@test", "test123"
        );
        user = userRepository.save(user);
        when(userHelperService.getUser()).thenReturn(user);
        Saving saving = new Saving("Car", BigDecimal.valueOf(5000));
        User realUser = new User(
                "real", "real@test", "test123"
        );
        userRepository.save(realUser);
        saving.setUser(realUser);
        saving = savingRepository.save(saving);
        Saving finalSaving = saving;
        Assertions.assertThrows(NullPointerException.class,() -> savingService.deleteSaving(finalSaving.getId()));
    }
    @Test
    @Transactional
    void testDeleteSaving(){

        User user = new User(
                "test", "test@test", "test123"
        );
        user = userRepository.save(user);
        when(userHelperService.getUser()).thenReturn(user);
        Saving saving = new Saving("Car", BigDecimal.valueOf(5000));
        saving.setUser(user);
        saving = savingRepository.save(saving);

        savingService.deleteSaving(saving.getId());
        Assertions.assertEquals(0, savingRepository.count());
        Assertions.assertEquals(0, (long) userRepository.findByEmail("test@test").get().getSavings().size());
    }
}
