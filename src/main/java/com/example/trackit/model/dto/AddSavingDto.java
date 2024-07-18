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
    @NotBlank
    @Size(min = 3, max = 25)
    private String goal;
    @Positive
    @NotNull
    private BigDecimal targetAmount;
    @Size(min = 3, max = 240)
    private String description;


}
