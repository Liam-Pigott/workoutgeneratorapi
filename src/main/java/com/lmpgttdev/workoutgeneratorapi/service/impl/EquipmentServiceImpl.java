package com.lmpgttdev.workoutgeneratorapi.service.impl;

import com.lmpgttdev.workoutgeneratorapi.model.Equipment;
import com.lmpgttdev.workoutgeneratorapi.repository.EquipmentRepository;
import com.lmpgttdev.workoutgeneratorapi.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Override
    public Optional<Equipment> getEquipmentById(Long id) {
        return equipmentRepository.findById(id);
    }

    @Override
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }
}
