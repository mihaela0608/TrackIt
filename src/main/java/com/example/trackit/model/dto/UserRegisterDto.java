package com.example.trackit.model.dto;

import com.example.trackit.model.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {

        @NotNull(message = "{userRegisterDto.username.NotBlank}")
        @Size(min = 3, max = 20, message = "{userRegisterDto.username.Size}")
        private String username;

        @NotNull(message = "{userRegisterDto.email.NotBlank}")
        @Size(min = 6, max = 40, message = "{userRegisterDto.email.Size}")
        @Email(message = "{userRegisterDto.email.Email}")
        private String email;

        @NotNull(message = "{userRegisterDto.password.NotBlank}")
        @Size(min = 5, max = 20, message = "{userRegisterDto.password.Size}")
        private String password;

        private String confirmPassword;
}
