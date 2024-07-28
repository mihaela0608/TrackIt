package com.example.trackit.service.impl;

import com.example.trackit.model.dto.AddCategoryDto;
import com.example.trackit.model.dto.CategoryDetailsDto;
import com.example.trackit.model.entity.Category;
import com.example.trackit.repository.CategoryRepository;
import com.example.trackit.service.CategoryService;
import com.example.trackit.service.session.UserHelperService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

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
        return categoryRepository.findAll().stream()
                .filter(c -> c.getUser().getId() == 1 || c.getUser().getId() == userHelperService.getUser().getId()).toList();
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

    @Override
    public List<CategoryDetailsDto> getAllDetails() {
        return getAllForUser().stream().map(CategoryServiceImpl::getCategoryDetailsDto
        ).toList();
    }

    @Override
    @Transactional
    public boolean deleteCategory(String categoryName) {
        Category category = categoryRepository.findAll().stream()
                .filter(c -> c.getUser().getId() == userHelperService.getUser().getId())
                .filter(c -> c.getName().equals(categoryName))
                .findFirst().orElseThrow();
        if (category.getUser().getId() == 1){
            throw new NullPointerException();
        }
        if (!userHelperService.getUser().getBudgets().stream().filter(b -> Objects.equals(b.getCategory().getName(), categoryName)).toList().isEmpty()){
            return false;
        }
        categoryRepository.delete(category);
        return  true;
    }

    private static CategoryDetailsDto getCategoryDetailsDto(Category c) {
        CategoryDetailsDto categoryDetailsDto = new CategoryDetailsDto();
        categoryDetailsDto.setCategoryName(c.getName());
        categoryDetailsDto.setDescription(c.getDescription());
        categoryDetailsDto.setUserId(c.getUser().getId());
        return categoryDetailsDto;
    }
}
