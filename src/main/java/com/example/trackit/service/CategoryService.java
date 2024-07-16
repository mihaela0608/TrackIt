package com.example.trackit.service;

import com.example.trackit.model.dto.AddCategoryDto;
import com.example.trackit.model.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllForUser();
    boolean addNewCategory(AddCategoryDto addCategoryDto);
}
