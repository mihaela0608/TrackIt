package com.example.trackit.service;

import com.example.trackit.model.entity.Saving;
import com.example.trackit.model.entity.User;
import com.example.trackit.repository.BudgetRepository;
import com.example.trackit.repository.SavingRepository;
import com.example.trackit.service.impl.SavingServiceImpl;
import com.example.trackit.service.session.UserHelperService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestSavingServiceImpl {
    @Mock
    private SavingRepository savingRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserHelperService userHelperService;
    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private SavingServiceImpl savingService;

    @Test
    void isSavingIdValidForUser(){
        Saving saving = new Saving("New dress", BigDecimal.valueOf(50));
        saving.setId(1);
        User user = new User();
        user.setSavings(List.of(saving));
        when(userHelperService.getUser()).thenReturn(user);

        boolean savingIdValidForUser = savingService.isSavingIdValidForUser(1);
        Assertions.assertTrue(savingIdValidForUser);
        boolean savingIdValidForUser2 = savingService.isSavingIdValidForUser(2);
        Assertions.assertFalse(savingIdValidForUser2);

    }
}
