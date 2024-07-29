package com.example.trackit.service;

import com.example.trackit.model.dto.AddCategoryDto;
import com.example.trackit.model.dto.CategoryDetailsDto;
import com.example.trackit.model.entity.Budget;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    @Test
    void testAddNewCategoryReturnsTrueAndCreatesNew(){
        User user = new User("ivan", "ivan@gmail.com", "ivo88");
        user = userRepository.save(user);
        when(userHelperService.getUser()).thenReturn(user);


        AddCategoryDto addCategoryDto = new AddCategoryDto("Food", "Restaurant");
        boolean added = categoryService.addNewCategory(addCategoryDto);

        Assertions.assertTrue(added);
        Assertions.assertEquals(1, categoryRepository.findByUser(user).size());
        Category category = categoryRepository.findByUser(user).get(0);
        Assertions.assertNotNull(category);
        Assertions.assertEquals("Food", category.getName());
        Assertions.assertEquals("Restaurant", category.getDescription());

    }

    @Test
    void deleteCategoryThrowsWhenNotFound(){
        User user = new User("ivan", "ivan@gmail.com", "ivo88");
        user = userRepository.save(user);
        when(userHelperService.getUser()).thenReturn(user);

        Category category = new Category("Food", "Eating");
        category.setUser(user);
        categoryRepository.save(category);


        Assertions.assertThrows(NoSuchElementException.class,() -> categoryService.deleteCategory("Gym"));

    }
    @Test
    void deleteCategoryReturnsFalseWhenCategoryIsUsed(){
        User user = new User("ivan", "ivan@gmail.com", "ivo88");
        user = userRepository.save(user);


        Category category = new Category("Food", "Eating");
        category.setUser(user);
        categoryRepository.save(category);
        Budget budget = new Budget(BigDecimal.TEN, BigDecimal.ZERO, category);
        budget.setUser(user);
        budget = budgetRepository.save(budget);
        user.setBudgets(List.of(budget));
        userRepository.save(user);
        when(userHelperService.getUser()).thenReturn(user);



        Assertions.assertFalse(categoryService.deleteCategory("Food"));

    }

    @Test
    void deleteCategoryReturnsTrue(){
        User user = new User("ivan", "ivan@gmail.com", "ivo88");
        user = userRepository.save(user);


        Category category = new Category("Food", "Eating");
     category.setUser(user);
        categoryRepository.save(category);
        when(userHelperService.getUser()).thenReturn(user);

        boolean added = categoryService.deleteCategory("Food");
        Assertions.assertTrue(added);
        Optional<Category> category1 = categoryRepository.findByName("Food");
        Assertions.assertTrue(category1.isEmpty());
    }

    @Test
    void getAllDetails(){
        User user = new User("ivan", "ivan@gmail.com", "ivo88");
        user = userRepository.save(user);


        Category category = new Category("Food", "Eating");
        category.setUser(user);
        categoryRepository.save(category);
        when(userHelperService.getUser()).thenReturn(user);

        List<CategoryDetailsDto> allDetails = categoryService.getAllDetails();

        Assertions.assertNotNull(allDetails);
        Assertions.assertEquals(1, allDetails.size());
        CategoryDetailsDto categoryDetailsDto = allDetails.get(0);
        Assertions.assertEquals(categoryDetailsDto.getCategoryName(), category.getName());
        Assertions.assertEquals(categoryDetailsDto.getDescription(), category.getDescription());
        Assertions.assertEquals(categoryDetailsDto.getUserId(), user.getId());
    }


}
