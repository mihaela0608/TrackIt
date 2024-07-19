package com.example.trackit.service.impl;

import com.example.trackit.model.dto.AddFromBudget;
import com.example.trackit.model.dto.AddSavingDto;
import com.example.trackit.model.dto.SavingDetails;
import com.example.trackit.model.entity.Saving;
import com.example.trackit.repository.SavingRepository;
import com.example.trackit.service.SavingService;
import com.example.trackit.service.session.UserHelperService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SavingServiceImpl implements SavingService {
    private final SavingRepository savingRepository;
    private final ModelMapper modelMapper;
    private final UserHelperService userHelperService;

    public SavingServiceImpl(SavingRepository savingRepository, ModelMapper modelMapper, UserHelperService userHelperService) {
        this.savingRepository = savingRepository;
        this.modelMapper = modelMapper;
        this.userHelperService = userHelperService;
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
}
