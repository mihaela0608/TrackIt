
package com.example.trackit.service;

import com.example.trackit.model.dto.UserDetailsDto;
import com.example.trackit.model.dto.UserForAdmin;
import com.example.trackit.model.dto.UserRegisterDto;
import com.example.trackit.model.entity.User;

import java.util.List;

public interface UserService {
    boolean registerUser(UserRegisterDto userRegisterDto);
    UserDetailsDto getUserDetails();
    void updateUserProgress(User user);

    List<UserForAdmin> findAllForAdmin();

    void deleteUser(Long id);
}
