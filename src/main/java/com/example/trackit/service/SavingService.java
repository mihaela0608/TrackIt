package com.example.trackit.service;

import com.example.trackit.model.dto.AddFromBudget;
import com.example.trackit.model.dto.AddSavingDto;
import com.example.trackit.model.dto.SavingDetails;

import java.util.List;

public interface SavingService {
    void addSaving(AddSavingDto addSavingDto);
    List<SavingDetails> allDetails();

    boolean addAmountFromBudget(long id, AddFromBudget addFromBudget);
    boolean isSavingIdValidForUser(long id);
}
