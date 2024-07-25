package com.example.trackit.service;

import com.example.trackit.model.dto.UserRegisterDto;
import com.example.trackit.model.entity.Role;
import com.example.trackit.model.entity.User;
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
import org.mockito.Captor;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

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
    private ModelMapper modelMapper;

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

    @Captor
    private ArgumentCaptor<User> userEntityCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
        Assertions.assertEquals("USER", user.getRole().getName());
    }
}
