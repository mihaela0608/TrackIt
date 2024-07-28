package com.example.trackit.service;

import com.example.trackit.model.entity.Category;
import com.example.trackit.model.entity.User;
import com.example.trackit.repository.CategoryRepository;
import com.example.trackit.repository.ExpenseRepository;
import com.example.trackit.repository.SavingRepository;
import com.example.trackit.repository.UserRepository;
import com.example.trackit.service.impl.CategoryServiceImpl;
import com.example.trackit.service.session.UserHelperService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
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
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp(){
        categoryRepository.deleteAll();
        expenseRepository.deleteAll();
        savingRepository.deleteAll();
        userRepository.deleteAll();
        categoryService = new CategoryServiceImpl(categoryRepository, userHelperService, new ModelMapper());
    }


        @Test
        void getAllForUser() {
        //TODO: SMALL PROBLEM
            User admin = new User("admin", "admin@abv.bg", "admincheto");
            admin.setId(1L);
            admin = userRepository.save(admin);

            User user = new User("iva", "iva@abv.bg", "ivcheto");
            user.setId(2L);
            user = userRepository.save(user);

            Category category1 = new Category("Food", "Eating");
            category1.setUser(admin);
            categoryRepository.save(category1);

            Category category2 = new Category("Gym", "Workout");
            category2.setUser(user);
            categoryRepository.save(category2);

            when(userHelperService.getUser()).thenReturn(user);

            List<Category> allForUser = categoryService.getAllForUser();

            Assertions.assertNotNull(allForUser);
            Assertions.assertEquals(2, allForUser.size());
            Assertions.assertEquals("Food", allForUser.get(0).getName());
            Assertions.assertEquals("Eating", allForUser.get(0).getDescription());
            Assertions.assertEquals("Gym", allForUser.get(1).getName());
            Assertions.assertEquals("Workout", allForUser.get(1).getDescription());

        }
}
