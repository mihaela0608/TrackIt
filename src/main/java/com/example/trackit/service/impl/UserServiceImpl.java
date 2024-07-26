package com.example.trackit.service.impl;

import com.example.trackit.model.dto.UserData;
import com.example.trackit.model.dto.UserDetailsDto;
import com.example.trackit.model.dto.UserForAdmin;
import com.example.trackit.model.dto.UserRegisterDto;
import com.example.trackit.model.entity.Expense;
import com.example.trackit.model.entity.Saving;
import com.example.trackit.model.entity.User;
import com.example.trackit.repository.*;
import com.example.trackit.service.UserDataService;
import com.example.trackit.service.UserService;
import com.example.trackit.service.session.UserHelperService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserHelperService userHelperService;
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final SavingRepository savingRepository;
    private final UserDataService userDataService;


//TODO : MAKE IT INTEGRATION TEST
    @Override
    public boolean registerUser(UserRegisterDto userRegisterDto) {
        if (userRepository.findByEmail(userRegisterDto.getEmail()).isPresent()){
            return false;
        }
        User user = getUser(userRegisterDto);
        User save = userRepository.saveAndFlush(user);
        addLastMonthProfile(save);
        return true;
    }

    private void addLastMonthProfile(User user) {
        UserData userData = new UserData(user.getId());
        userData.setLastMonthSavings(BigDecimal.ZERO);
        userData.setLastMonthExpenses(BigDecimal.ZERO);

        userDataService.saveUserData(userData);
    }

    @Override
    @Transactional
    public UserDetailsDto getUserDetails() {
        User user = userHelperService.getUser();
        UserDetailsDto userDetailsDto = modelMapper.map(user, UserDetailsDto.class);
        userDetailsDto.setRegistrationDate(String.valueOf(user.getRegistrationDate()));
        userDetailsDto.setExpensesSum(user.getExpenses().stream().map(Expense::getAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        userDetailsDto.setSavingsSum(user.getSavings().stream().map(Saving::getSavedAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        return userDetailsDto;
    }

    @Override
    public void updateUserProgress(User user) {
        UserData userData = new UserData(user.getId());
        userData.setLastMonthExpenses(user.getExpenses().stream().map(Expense::getAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        userData.setLastMonthSavings(user.getSavings().stream().map(Saving::getSavedAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        userDataService.updateUserData(userData);
    }

    @Override
    public List<UserForAdmin> findAllForAdmin() {
        return userRepository.findAll().stream()
                .filter(u -> u.getId() != 1)
                .map(u -> modelMapper.map(u, UserForAdmin.class))
                .toList();
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).get();
        if (userHelperService.getUser().getId() != 1){
            throw new NullPointerException();
        }
        budgetRepository.deleteAll(budgetRepository.findByUser(user));
        savingRepository.deleteAll(savingRepository.findByUser(user));
        expenseRepository.deleteAll(expenseRepository.findByUser(user));
        categoryRepository.deleteAll(categoryRepository.findByUser(user));
        userRepository.deleteById(id);
    }

    private User getUser(UserRegisterDto userRegisterDto) {
        User user = modelMapper.map(userRegisterDto, User.class);
        user.setRole(roleRepository.findByName("USER").get());
        if (userRepository.count() == 0){
            user.setRole(roleRepository.findByName("ADMIN").get());
        }
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        return user;
    }
}