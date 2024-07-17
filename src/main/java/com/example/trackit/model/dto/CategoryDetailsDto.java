package com.example.trackit.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDetailsDto {
    private String categoryName;
    private String description;
    private long userId;

}
