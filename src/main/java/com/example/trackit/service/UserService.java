package com.example.trackit.service;

import com.example.trackit.model.dto.UserRegisterDto;

public interface UserService {
    boolean registerUser(UserRegisterDto userRegisterDto);
}
