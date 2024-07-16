package com.example.trackit.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddCategoryDto {
    @NotBlank
    @Size(min = 3, max = 50, message = "Name should be between 3 and 50 symbols")
    private String name;
    @NotBlank
    @Size(min = 3, max = 120, message = "Description should be between 3 and 120 symbols")
    private String description;

}
