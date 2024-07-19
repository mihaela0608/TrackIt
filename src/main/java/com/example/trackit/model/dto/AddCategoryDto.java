package com.example.trackit.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddCategoryDto {
    @NotNull
    @Size(min = 3, max = 50, message = "{addCategoryDto.name.Size}")
    private String name;

    @NotNull
    @Size(min = 3, max = 120, message = "{addCategoryDto.description.Size}")
    private String description;
}
