package com.example.trackit.service.impl;

import com.example.trackit.model.dto.AddFromBudget;
import com.example.trackit.model.dto.AddSavingDto;
import com.example.trackit.model.dto.SavingDetails;
import com.example.trackit.model.entity.Budget;
import com.example.trackit.model.entity.Saving;
import com.example.trackit.repository.BudgetRepository;
import com.example.trackit.repository.SavingRepository;
import com.example.trackit.service.SavingService;
import com.example.trackit.service.session.UserHelperService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SavingServiceImpl implements SavingService {
    private final SavingRepository savingRepository;
    private final ModelMapper modelMapper;
    private final UserHelperService userHelperService;
    private final BudgetRepository budgetRepository;

    public SavingServiceImpl(SavingRepository savingRepository, ModelMapper modelMapper, UserHelperService userHelperService, BudgetRepository budgetRepository) {
        this.savingRepository = savingRepository;
        this.modelMapper = modelMapper;
        this.userHelperService = userHelperService;
        this.budgetRepository = budgetRepository;
    }

    @Override
    public void addSaving(AddSavingDto addSavingDto) {
        Saving saving = modelMapper.map(addSavingDto, Saving.class);
        saving.setUser(userHelperService.getUser());
        savingRepository.save(saving);
    }

    @Override
    @Transactional
    public List<SavingDetails> allDetails() {
        return userHelperService.getUser().getSavings().stream()
                .map(s -> modelMapper.map(s, SavingDetails.class)).toList();
    }

    @Override
    public boolean addAmountFromBudget(long id, AddFromBudget addFromBudget) {
        return false;
    }

    @Override
    @Transactional
    public boolean isSavingIdValidForUser(long id) {
        Optional<Saving> optionalSaving = userHelperService.getUser().getSavings().stream()
                .filter(s -> s.getId() == id).findFirst();
        return optionalSaving.isPresent();
    }

    @Override
    public boolean addSeparate(long id, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0){
            return false;
        }
        Saving saving = savingRepository.findById(id).get();
        saving.setSavedAmount(saving.getSavedAmount().add(amount));
        savingRepository.save(saving);
        return true;
    }

    @Override
    @Transactional
    public boolean addFromBudget(long id, AddFromBudget fromBudget) {
        Optional<Budget> budget = userHelperService.getUser().getBudgets().stream()
                .filter(b -> b.getCategory().getName().equals(fromBudget.getCategoryName()))
                .findFirst();
        if (budget.isEmpty()){
            return false;
        }
        BigDecimal amount = budget.get().getAmount().subtract(budget.get().getSpentAmount());
        if (amount.compareTo(fromBudget.getNewAmount()) < 0){
            return false;
        }
        budget.get().setSpentAmount(budget.get().getSpentAmount().add(fromBudget.getNewAmount()));
        budgetRepository.save(budget.get());
        Saving saving = savingRepository.findById(id).get();
        saving.setSavedAmount(saving.getSavedAmount().add(fromBudget.getNewAmount()));
        savingRepository.save(saving);
        return true;
    }


}
