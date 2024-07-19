package com.example.trackit.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AddSavingDto {
    @NotBlank(message = "Goal should be between 3 and 25 symbols")
    @Size(min = 3, max = 25, message = "Goal should be between 3 and 25 symbols")
    private String goal;
    @Positive(message = "Target amount should be positive number")
    @NotNull(message = "Target amount should be positive number")
    private BigDecimal targetAmount;
    @Size(max = 240, message = "Description should not be more than 240 symbols")
    private String description;


}
