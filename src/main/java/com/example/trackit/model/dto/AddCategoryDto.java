package com.example.trackit.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddCategoryDto {
    @NotBlank(message = "Name should not be blank")
    private String name;
    private String description;

}
