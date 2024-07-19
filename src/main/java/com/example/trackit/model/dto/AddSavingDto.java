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

    @NotNull(message = "{addSavingDto.goal.NotBlank}")
    @Size(min = 3, max = 25, message = "{addSavingDto.goal.Size}")
    private String goal;

    @Positive(message = "{addSavingDto.targetAmount.Positive}")
    @NotNull(message = "{addSavingDto.targetAmount.NotNull}")
    private BigDecimal targetAmount;

    @Size(max = 240, message = "{addSavingDto.description.Size}")
    private String description;
}
