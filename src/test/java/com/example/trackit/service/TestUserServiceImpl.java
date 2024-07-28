package com.example.trackit.service;

import com.example.trackit.model.dto.UserDetailsDto;
import com.example.trackit.model.dto.UserRegisterDto;
import com.example.trackit.model.entity.*;
import com.example.trackit.repository.*;
import com.example.trackit.service.impl.UserServiceImpl;
import com.example.trackit.service.session.UserHelperService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class TestUserServiceImpl {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UserServiceImpl testUserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Mock
    private UserHelperService userHelperService;


    @Mock
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserDataService userDataService;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private SavingRepository savingRepository;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        savingRepository.deleteAll();
        budgetRepository.deleteAll();
        categoryRepository.deleteAll();
        expenseRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // Setting up roles
        Role userRole = new Role();
        userRole.setName("USER");
        roleRepository.save(userRole);
//        when(roleRepository.save(any(Role.class))).thenReturn(userRole);

        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        roleRepository.save(adminRole);
//        when(roleRepository.save(any(Role.class))).thenReturn(adminRole);

        // Create a UserServiceImpl instance with mocks
        testUserService = new UserServiceImpl(
                userRepository,
                roleRepository,
                new ModelMapper(),
                passwordEncoder,
                userHelperService,
                budgetRepository,
                categoryRepository,
                expenseRepository,
                savingRepository,
                userDataService
        );
    }

    @Test
    @Transactional
    void testUserRegistration() {
        // Arrange
        UserRegisterDto userRegisterDto = new UserRegisterDto("testUsername", "testEmail", "testPassword", "testConfirm");

        // Mocking dependencies
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
//        when(userRepository.findByEmail("testEmail")).thenReturn(Optional.of(new User()));

        // Act
        boolean result = testUserService.registerUser(userRegisterDto);

        // Assert
        Assertions.assertTrue(result);
        Optional<User> registeredUser = userRepository.findByEmail("testEmail");
        Assertions.assertTrue(registeredUser.isPresent());
        User user = registeredUser.get();
        Assertions.assertEquals(userRegisterDto.getUsername(), user.getUsername());
        Assertions.assertEquals(userRegisterDto.getEmail(), user.getEmail());
        Assertions.assertEquals("encodedPassword", user.getPassword());
        Assertions.assertNotNull(user.getRole());
        Assertions.assertEquals("ADMIN", user.getRole().getName());
        UserRegisterDto userRegisterDto2 = new UserRegisterDto("testUsername", "testEmail2", "testPassword", "testConfirm");
        boolean result2 = testUserService.registerUser(userRegisterDto2);

        // Assert
        Assertions.assertTrue(result2);
        Optional<User> registeredUser2 = userRepository.findByEmail("testEmail2");
        Assertions.assertTrue(registeredUser2.isPresent());
        User user2 = registeredUser2.get();
        Assertions.assertEquals(userRegisterDto2.getUsername(), user2.getUsername());
        Assertions.assertEquals(userRegisterDto2.getEmail(), user2.getEmail());
        Assertions.assertEquals("encodedPassword", user2.getPassword());
        Assertions.assertNotNull(user2.getRole());
        Assertions.assertEquals("USER", user2.getRole().getName());
    }

    @Test
    void testDeleteUserThrowsWhenIsNotAdmin(){
        UserRegisterDto userRegisterDto = new UserRegisterDto("testUsername", "testEmail", "testPassword", "testConfirm");
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        testUserService.registerUser(userRegisterDto);

        Optional<User> optionalUser = userRepository.findByEmail("testEmail");
        Assertions.assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();

        User currentUser = new User();
        currentUser.setId(5L);
        when(userHelperService.getUser()).thenReturn(currentUser);

        Assertions.assertThrows(NullPointerException.class, () -> testUserService.deleteUser(user.getId()));
    }

    @Test
    void testDeleteUserThrowsWhenUserIsInvalid(){
        Assertions.assertThrows(NoSuchElementException.class, () -> testUserService.deleteUser(5L));
    }

    @Test
    void testDeleteUserDeletesEverythingForTheUser(){
        // Arrange: Register an admin user
        UserRegisterDto userRegisterDtoAdmin = new UserRegisterDto("admin", "admin@ad", "testPassword", "testConfirm");
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        testUserService.registerUser(userRegisterDtoAdmin);
        Optional<User> optionalAdmin = userRepository.findByEmail("admin@ad");
        Assertions.assertTrue(optionalAdmin.isPresent());
        User adminUser = optionalAdmin.get();
        adminUser.setId(1L);
        userRepository.deleteAll();
        userRepository.save(adminUser);

        // Arrange: Register a regular user
        UserRegisterDto userRegisterDto = new UserRegisterDto("testUsername", "testEmail", "testPassword", "testConfirm");
        testUserService.registerUser(userRegisterDto);

        // Retrieve and verify the registered regular user
        Optional<User> optionalUser = userRepository.findByEmail("testEmail");
        Assertions.assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();

        // Arrange: Add related entities (categories, budgets, savings, expenses) for the regular user
        Category categoryForSaving = new Category("Food", "Some food");
        categoryForSaving.setUser(user);
        categoryRepository.saveAndFlush(categoryForSaving);

        Budget budget = new Budget(BigDecimal.TEN, BigDecimal.ZERO, categoryForSaving);
        budget.setUser(user);
        budgetRepository.saveAndFlush(budget);

        Saving saving = new Saving("Rent", BigDecimal.valueOf(400));
        saving.setUser(user);
        savingRepository.saveAndFlush(saving);

        Expense expense = new Expense(categoryForSaving, BigDecimal.TEN);
        expense.setUser(user);
        expenseRepository.saveAndFlush(expense);

        // Mock the admin user in userHelperService
        when(userHelperService.getUser()).thenReturn(adminUser);

        // Act: Delete the regular user
        testUserService.deleteUser(user.getId());

        // Assert: Verify that all related entities and the user are deleted
        Assertions.assertEquals(0, categoryRepository.count());
        Assertions.assertEquals(0, budgetRepository.count());
        Assertions.assertEquals(0, savingRepository.count());
        Assertions.assertEquals(0, expenseRepository.count());
        Assertions.assertTrue(userRepository.findByEmail(userRegisterDto.getEmail()).isEmpty());
    }

    @Test
    void testGetUserDetails() {
        // Setup mock User object
        User user = new User("ivan", "ivan@gmail.com", "ivcho");
        user.setId(1L);
        user.setRegistrationDate(LocalDate.MIN);

        Category category = new Category("Food", "Something for eating");
        category.setUser(user);

        Saving saving1 = new Saving("Emergency Fund");
        saving1.setUser(user);
        saving1.setSavedAmount(BigDecimal.valueOf(300));
        Saving saving2 = new Saving("Vacation Fund");
        saving2.setUser(user);
        saving2.setSavedAmount(BigDecimal.valueOf(200));

        Expense expense1 = new Expense(category, BigDecimal.valueOf(100));
        expense1.setUser(user);
        Expense expense2 = new Expense(category, BigDecimal.valueOf(50));
        expense2.setUser(user);

        user.setExpenses(Arrays.asList(expense1, expense2));
        user.setSavings(Arrays.asList(saving1, saving2));

        // Setup mock UserDetailsDto for mapToDetails
        UserDetailsDto mappedDetails = new UserDetailsDto();
        mappedDetails.setLastMonthExpenses(BigDecimal.valueOf(200));
        mappedDetails.setLastMonthSavings(BigDecimal.valueOf(100));

        // Mock the behaviors
        when(userHelperService.getUser()).thenReturn(user);
        when(userDataService.mapToDetails(any(UserDetailsDto.class))).thenReturn(mappedDetails);

        // Test the method
        UserDetailsDto userDetailsDto = testUserService.getUserDetails();

        // Assertions
        Assertions.assertEquals(BigDecimal.valueOf(150), userDetailsDto.getExpensesSum());
        Assertions.assertEquals(BigDecimal.valueOf(500), userDetailsDto.getSavingsSum());
        Assertions.assertEquals(String.valueOf(LocalDate.MIN), userDetailsDto.getRegistrationDate());
        Assertions.assertEquals(BigDecimal.valueOf(200), userDetailsDto.getLastMonthExpenses());
        Assertions.assertEquals(BigDecimal.valueOf(100), userDetailsDto.getLastMonthSavings());
    }


}
