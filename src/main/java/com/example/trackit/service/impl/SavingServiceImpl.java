package com.example.trackit.service.impl;

import com.example.trackit.model.dto.AddSavingDto;
import com.example.trackit.model.entity.Saving;
import com.example.trackit.repository.SavingRepository;
import com.example.trackit.service.SavingService;
import com.example.trackit.service.session.UserHelperService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
}
