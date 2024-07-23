package com.example.trackit.service;

import com.example.trackit.model.dto.UserRegisterDto;
import com.example.trackit.model.entity.Role;
import com.example.trackit.model.entity.User;
import com.example.trackit.repository.*;
import com.example.trackit.service.impl.UserServiceImpl;
import com.example.trackit.service.session.UserHelperService;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClient;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestUserServiceImpl {

    //private final UserRepository userRepository;
    //    private final RoleRepository roleRepository;
    //    private final ModelMapper modelMapper;
    //    private final PasswordEncoder passwordEncoder;
    //    private final UserHelperService userHelperService;
    //    private final RestClient restClient;
    //    private final BudgetRepository budgetRepository;
    //    private final CategoryRepository categoryRepository;
    //    private final ExpenseRepository expenseRepository;
    //    private final SavingRepository savingRepository;
    private UserServiceImpl testUserService;
    @Mock
    private UserRepository userRepository;
    @Mock
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
        testUserService = new UserServiceImpl(
                userRepository,
                roleRepository,
                new ModelMapper(),
                passwordEncoder,
                userHelperService,
                restClient,
                budgetRepository,
                categoryRepository,
                expenseRepository,
                savingRepository
        );
    }
    @Test
    void testUserRegistration(){
        UserRegisterDto userRegisterDto =
                new UserRegisterDto("testUsername", "testEmail", "testPassword", "testConfirm");

        when(passwordEncoder.encode(userRegisterDto.getPassword()))
                .thenReturn(userRegisterDto.getPassword()+userRegisterDto.getPassword());
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(new Role("USER")));
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(new Role("ADMIN")));

        testUserService.registerUser(userRegisterDto);

        verify(userRepository).save(userEntityCaptor.capture());
        User userActual = userEntityCaptor.getValue();
        Assertions.assertNotNull(userActual);
        Assertions.assertEquals(userActual.getUsername(), userRegisterDto.getUsername());
        Assertions.assertEquals(userActual.getEmail(), userRegisterDto.getEmail());
        Assertions.assertEquals(userActual.getPassword(), userRegisterDto.getPassword());
    }
}
