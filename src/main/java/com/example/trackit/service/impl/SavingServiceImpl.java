package com.example.trackit.service.impl;

import com.example.trackit.model.dto.AddSavingDto;
import com.example.trackit.model.entity.Saving;
import com.example.trackit.repository.SavingRepository;
import com.example.trackit.service.SavingService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class SavingServiceImpl implements SavingService {
    private final SavingRepository savingRepository;
    private final ModelMapper modelMapper;

    public SavingServiceImpl(SavingRepository savingRepository, ModelMapper modelMapper) {
        this.savingRepository = savingRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean addSaving(AddSavingDto addSavingDto) {
        if (savingRepository.findByGoal(addSavingDto.getGoal()).isPresent()){
            return false;
        }
        Saving saving = modelMapper.map(addSavingDto, Saving.class);
        savingRepository.save(saving);
        return true;
    }
}
