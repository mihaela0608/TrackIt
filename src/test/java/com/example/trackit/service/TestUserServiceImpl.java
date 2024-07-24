package com.example.trackit.service;

import com.example.trackit.model.dto.UserRegisterDto;
import com.example.trackit.model.entity.Role;
import com.example.trackit.model.entity.User;
import com.example.trackit.repository.*;
import com.example.trackit.service.impl.UserServiceImpl;
import com.example.trackit.service.session.UserHelperService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClient;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
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
    @Mock
    private RestClient restClient;
    @Mock
    private  BudgetRepository budgetRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private  SavingRepository savingRepository;
    @Captor
    private ArgumentCaptor<User> userEntityCaptor;


    public TestUserServiceImpl() {
    }

    @BeforeEach
    void setUp(){
        Role userRole = new Role();
        userRole.setName("USER");
        roleRepository.save(userRole);

        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        roleRepository.save(adminRole);
    }
    @Test
    @Transactional
    void testUserRegistration(){
        UserRegisterDto userRegisterDto =
                new UserRegisterDto("testUsername", "testEmail", "testPassword", "testConfirm");
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        doNothing().when(restClient).post()
                .uri(anyString())
                .body(any())
                .retrieve();
        boolean result = testUserService.registerUser(userRegisterDto);
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
