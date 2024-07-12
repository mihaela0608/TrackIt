package com.example.trackit.service.impl;

import com.example.trackit.model.dto.UserRegisterDto;
import com.example.trackit.model.entity.User;
import com.example.trackit.repository.RoleRepository;
import com.example.trackit.repository.UserRepository;
import com.example.trackit.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean registerUser(UserRegisterDto userRegisterDto) {
        if (userRepository.findByEmail(userRegisterDto.getEmail()).isPresent()){
            return false;
        }
        User user = getUser(userRegisterDto);
        userRepository.save(user);
        return true;
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
