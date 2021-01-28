package com.lmpgttdev.workoutgeneratorapi.service;

import com.lmpgttdev.workoutgeneratorapi.model.Equipment;
import com.lmpgttdev.workoutgeneratorapi.model.Exercise;
import com.lmpgttdev.workoutgeneratorapi.model.MuscleGroup;

import java.util.List;
import java.util.Optional;

public interface EquipmentService {

    //TODO: Read only for now - can be extended at a later date to add custom equipment
    Optional<Equipment> getEquipmentById(Long id);
    List<Equipment> getAllEquipment();
}
