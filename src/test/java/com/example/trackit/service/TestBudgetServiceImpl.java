package com.example.trackit.service;


import com.example.trackit.model.dto.ViewAllBudgetDto;
import com.example.trackit.model.entity.Budget;
import com.example.trackit.model.entity.Category;
import com.example.trackit.model.entity.User;
import com.example.trackit.repository.BudgetRepository;
import com.example.trackit.repository.CategoryRepository;
import com.example.trackit.service.impl.BudgetServiceImpl;
import com.example.trackit.service.session.UserHelperService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestBudgetServiceImpl {
    //private ViewAllBudgetDto map(Budget budget){
    //        ViewAllBudgetDto budgetDto = modelMapper.map(budget, ViewAllBudgetDto.class);
    //        budgetDto.setSpentAmount(budget.getSpentAmount());
    //        return budgetDto;
    //    }
    private BudgetServiceImpl budgetService;
    //  private final BudgetRepository budgetRepository;
    //    private final UserHelperService userHelperService;
    //    private final CategoryRepository categoryRepository;
    //    private final ModelMapper modelMapper;
    @Mock
    private BudgetRepository budgetRepository;
    @Mock
    private UserHelperService userHelperService;
    @Mock
    CategoryRepository categoryRepository;

    @BeforeEach
    void setUp(){
        budgetService = new BudgetServiceImpl(
          budgetRepository,
          userHelperService,
          categoryRepository,
          new ModelMapper()
        );
    }

    @Test
    void testViewAllBudgets(){
        when(categoryRepository.findByName("Groceries")).thenReturn(Optional.of(new Category("Groceries")));
        Budget testBudget1 = new Budget(BigDecimal.valueOf(32.00), BigDecimal.ZERO, categoryRepository.findByName("Groceries").get());
        User user = new User();
        user.setBudgets(List.of(testBudget1));
        when(userHelperService.getUser()).thenReturn(user);

        List<ViewAllBudgetDto> viewAllBudgetDtos = budgetService.viewAll();
        ViewAllBudgetDto viewAllBudgetDto = viewAllBudgetDtos.get(0);
        Assertions.assertNotNull(viewAllBudgetDto);
        Assertions.assertEquals(viewAllBudgetDto.getAmount(), testBudget1.getAmount());
        Assertions.assertEquals(viewAllBudgetDto.getSpentAmount(), testBudget1.getSpentAmount());
        Assertions.assertEquals(viewAllBudgetDto.getCategoryName(), testBudget1.getCategory().getName());


    }

}
