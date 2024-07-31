package com.example.trackit.web;

import com.example.trackit.model.dto.AddCategoryDto;
import com.example.trackit.service.CategoryService;
import com.example.trackit.service.session.UserHelperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

class TestCategoryController {

    @Mock
    private CategoryService categoryService;

    @Mock
    private UserHelperService userHelperService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void testViewCreateCategory() throws Exception {
        mockMvc.perform(get("/categories/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-category"))
                .andExpect(model().attributeExists("categoryData"));
    }

    @Test
    void testCreateCategory_withErrors() throws Exception {
        AddCategoryDto categoryData = new AddCategoryDto();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/categories/create")
                        .flashAttr("categoryData", categoryData)
                        .flashAttr("org.springframework.validation.BindingResult.categoryData", bindingResult))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categories/create"));
    }



    @Test
    void testViewAll() throws Exception {
        mockMvc.perform(get("/categories/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("all-categories"))
                .andExpect(model().attributeExists("categories"));
    }

    @Test
    void testDeleteCategory_success() throws Exception {
        when(categoryService.deleteCategory(anyString())).thenReturn(true);

        mockMvc.perform(delete("/categories/delete/SomeCategory"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categories/all"));
    }

    @Test
    void testDeleteCategory_failure() throws Exception {
        when(categoryService.deleteCategory(anyString())).thenReturn(false);

        mockMvc.perform(delete("/categories/delete/SomeCategory"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categories/all"))
                .andExpect(flash().attribute("deleteError", true));
    }
}
