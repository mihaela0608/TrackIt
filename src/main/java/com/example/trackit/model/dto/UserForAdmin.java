package com.example.trackit.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserForAdmin {
    private long id;
    private String username;
    private String email;
}
