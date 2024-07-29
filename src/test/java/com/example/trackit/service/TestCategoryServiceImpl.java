package com.example.trackit.service;

import com.example.trackit.model.dto.AddCategoryDto;
import com.example.trackit.model.entity.Category;
import com.example.trackit.model.entity.User;
import com.example.trackit.repository.*;
import com.example.trackit.service.impl.CategoryServiceImpl;
import com.example.trackit.service.session.UserHelperService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TestCategoryServiceImpl {
    @Autowired
    private CategoryRepository categoryRepository;
    @Mock
    private UserHelperService userHelperService;
    @MockBean
    private UserDataService userDataService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SavingRepository savingRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        // Изчистване на всички данни
        categoryRepository.deleteAll();
        expenseRepository.deleteAll();
        savingRepository.deleteAll();
        budgetRepository.deleteAll();
        userRepository.deleteAll();
        categoryService = new CategoryServiceImpl(categoryRepository, userHelperService, new ModelMapper());
    }

//    @Test
//    void getAllForUser() {
//        User admin = new User("admin", "admin_unique@abv.bg", "admincheto");
//        admin = userRepository.save(admin);
//        userRepository.deleteAll();
//        admin.setId(1L);
//        userRepository.save(admin);
//        User user = new User("iva", "iva_unique@abv.bg", "ivcheto");
//        user.setId(2L);
//        userRepository.save(user);
//        Category category1 = new Category("Food", "Eating");
//        category1.setUser(admin);
//        categoryRepository.save(category1);
//
//        Category category2 = new Category("Gym", "Workout");
//        category2.setUser(user);
//        categoryRepository.save(category2);
//
//        when(userHelperService.getUser()).thenReturn(user);
//
//        List<Category> allForUser = categoryService.getAllForUser();
//
//        Assertions.assertNotNull(allForUser);
//        Assertions.assertEquals(2, allForUser.size());
//        Assertions.assertEquals("Gym", allForUser.get(0).getName());
//        Assertions.assertEquals("Workout", allForUser.get(0).getDescription());
//    }

    @Test
    void testAddNewCategoryReturnsFalseIfCategoryNameExists(){
        User user = new User("ivan", "ivan@gmail.com", "ivo88");
        user = userRepository.save(user);
        when(userHelperService.getUser()).thenReturn(user);

        Category category = new Category("Food", "Eating");
        category.setUser(user);
        categoryRepository.save(category);

        AddCategoryDto addCategoryDto = new AddCategoryDto("Food", "Restaurant");
        boolean added = categoryService.addNewCategory(addCategoryDto);

        Assertions.assertFalse(added);
    }


}
