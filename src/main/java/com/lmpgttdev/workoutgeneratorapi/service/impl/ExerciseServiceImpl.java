package com.lmpgttdev.workoutgeneratorapi.service.impl;

import com.lmpgttdev.workoutgeneratorapi.exception.DuplicateObjectException;
import com.lmpgttdev.workoutgeneratorapi.exception.ResourceNotFoundException;
import com.lmpgttdev.workoutgeneratorapi.model.Exercise;
import com.lmpgttdev.workoutgeneratorapi.model.MuscleGroup;
import com.lmpgttdev.workoutgeneratorapi.repository.EquipmentRepository;
import com.lmpgttdev.workoutgeneratorapi.repository.ExerciseRepository;
import com.lmpgttdev.workoutgeneratorapi.service.ExerciseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Override
    public Optional<Exercise> createExercise(Exercise exercise) {
        String name = exercise.getName();
        // ifPresentOrElse not suitable for throwing so this seems like the cleaner approach
        boolean exerciseExists = exerciseRepository.findByNameIgnoreCase(name).isPresent();
        if (!exerciseExists) {
            //Make sure equipment exists
            validateAndSetEquipment(exercise);
            exerciseRepository.saveAndFlush(exercise);
            return Optional.of(exercise);
        } else {
            throw new DuplicateObjectException("Exercise with name: " + name + " already exists");
        }
    }

    @Override
    public Optional<Exercise> getExerciseById(Long id) {
        return exerciseRepository.findById(id);
    }

    @Override
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    @Override
    public List<Exercise> getAllExercisesByMuscleGroup(MuscleGroup muscleGroup) {
        return exerciseRepository.findAllByMuscleGroup(muscleGroup);
    }

    @Override
    public Optional<Exercise> getExerciseByName(String name) {
        return exerciseRepository.findByNameIgnoreCase(name);
    }

    @Override
    public void updateExercise(Long id, Exercise exercise) {
        boolean exerciseNameExists = exerciseRepository.findByNameIgnoreCase(exercise.getName()).isPresent();
        if (exerciseNameExists) {
            throw new DuplicateObjectException("Could not update. Exercise with name: " + exercise.getName() + " already exists.");
        }
        Optional<Exercise> existingExerciseOptional = exerciseRepository.findById(id);
        if (existingExerciseOptional.isEmpty()) {
            throw new ResourceNotFoundException("Could not find exercise with id: " + id);
        }
        validateAndSetEquipment(exercise);
        exercise.setId(id);
        exerciseRepository.save(exercise);
    }

    @Override
    public void deleteExercise(Long id) {
        //If trying to delete an object that doesn't exist, I still want to know about it
        exerciseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find exercise with id: " + id));
        exerciseRepository.deleteById(id);
    }


    private void validateAndSetEquipment(Exercise exercise) {
        //Get the associated equipment if exists or log warning
        if (exercise.getEquipment() != null)
            equipmentRepository.findByNameIgnoreCase(exercise.getEquipment().getName()).ifPresentOrElse(
                    exercise::setEquipment,
                    () -> {
                        //TODO: Add support to create equipment for admin users. In the meantime use existing data set and log attempts made.
                        log.warn("Save attempted with unsupported equipment: " + exercise.getEquipment().getName());
                        exercise.setEquipment(null);
                    });
    }
}
