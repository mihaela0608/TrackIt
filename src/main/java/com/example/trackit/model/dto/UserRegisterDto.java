package com.example.trackit.model.dto;

import com.example.trackit.model.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

        @NotBlank
        @Size(min = 3, max = 20, message = "Username length must be between 3 and 20 symbols")
        private String username;
        @NotBlank
        @Size(min = 6, max = 40, message = "Email length must be between 6 and 40 symbols")
        @Email
        private String email;

        @NotBlank
        @Size(min = 5, max = 20, message = "Password length must be between 5 and 20 symbols")
        private String password;

        private String confirmPassword;

}
