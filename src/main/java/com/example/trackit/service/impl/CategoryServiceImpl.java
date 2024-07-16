package com.example.trackit.service.impl;

import com.example.trackit.model.dto.AddCategoryDto;
import com.example.trackit.model.entity.Category;
import com.example.trackit.repository.CategoryRepository;
import com.example.trackit.service.CategoryService;
import com.example.trackit.service.session.UserHelperService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserHelperService userHelperService;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, UserHelperService userHelperService, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.userHelperService = userHelperService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Category> getAllForUser() {
        return categoryRepository.findAll().stream().filter(c -> c.getUser().getId() == 1 || c.getUser().getId() == userHelperService.getUser().getId()).toList();
    }

    @Override
    public boolean addNewCategory(AddCategoryDto addCategoryDto) {
        if (!getAllForUser().stream().filter(c -> c.getName().equals(addCategoryDto.getName())).toList().isEmpty()){
            return false;
        }
        Category category = modelMapper.map(addCategoryDto, Category.class);
        category.setUser(userHelperService.getUser());
        categoryRepository.save(category);
        return true;
    }
}
