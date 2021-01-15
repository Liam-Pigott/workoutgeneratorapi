package com.lmpgttdev.workoutgeneratorapi.service.impl;

import com.lmpgttdev.workoutgeneratorapi.model.Exercise;
import com.lmpgttdev.workoutgeneratorapi.model.MuscleGroup;
import com.lmpgttdev.workoutgeneratorapi.repository.ExerciseRepository;
import com.lmpgttdev.workoutgeneratorapi.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseServiceImpl implements ExerciseService {


    @Autowired
    private ExerciseRepository exerciseRepository;

    @Override
    public void createExercise(Exercise exercise) {
        String name = exercise.getName();
        Exercise existingExercise = exerciseRepository.findExerciseByName(name).orElse(null);
        if(existingExercise == null){
            exerciseRepository.save(exercise);
        }
        else{
            throw new IllegalStateException("Exercise with name: " + name + " already exists");
        }
    }

    @Override
    public Optional<Exercise> getById(Long id) {
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
        return exerciseRepository.findExerciseByName(name);
    }

    @Override
    public void updateExercise(Long id, Exercise exercise) {

    }

    @Override
    public void deleteExercise(Long id) {
        exerciseRepository.deleteById(id); //return value
    }
}
