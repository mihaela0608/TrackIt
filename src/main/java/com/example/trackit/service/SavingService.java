package com.example.trackit.service;

import com.example.trackit.model.dto.AddFromBudget;
import com.example.trackit.model.dto.AddSavingDto;
import com.example.trackit.model.dto.SavingDetails;

import java.math.BigDecimal;
import java.util.List;

public interface SavingService {
    void addSaving(AddSavingDto addSavingDto);
    List<SavingDetails> allDetails();

    boolean addAmountFromBudget(long id, AddFromBudget addFromBudget);
    boolean isSavingIdValidForUser(long id);
    boolean addSeparate(long id, BigDecimal amount);
    boolean addFromBudget(long id, AddFromBudget fromBudget);

    void deleteSaving(Long id);

}
