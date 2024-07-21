package com.example.trackit.service.impl;

import com.example.trackit.model.dto.UserData;
import com.example.trackit.model.dto.UserDetailsDto;
import com.example.trackit.model.dto.UserRegisterDto;
import com.example.trackit.model.entity.Expense;
import com.example.trackit.model.entity.Saving;
import com.example.trackit.model.entity.User;
import com.example.trackit.repository.RoleRepository;
import com.example.trackit.repository.UserRepository;
import com.example.trackit.service.UserService;
import com.example.trackit.service.session.UserHelperService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserHelperService userHelperService;
    private final RestClient restClient;



    @Override
    public boolean registerUser(UserRegisterDto userRegisterDto) {
        if (userRepository.findByEmail(userRegisterDto.getEmail()).isPresent()){
            return false;
        }
        User user = getUser(userRegisterDto);
        User save = userRepository.save(user);
        addLastMonthProfile(save);
        return true;
    }

    private void addLastMonthProfile(User user) {
        UserData userData = new UserData(user.getId());
        userData.setLastMonthSavings(BigDecimal.ZERO);
        userData.setLastMonthExpenses(BigDecimal.ZERO);
        restClient.post()
                .uri("http://localhost:8081/api/userdata")
                .body(userData)
                .retrieve();
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