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
    public Exercise createExercise(Exercise exercise) {
        String name = exercise.getName();
        boolean exerciseExists = exerciseRepository.findByNameIgnoreCase(name).isPresent();
        if(!exerciseExists){
            exerciseRepository.saveAndFlush(exercise);
            return exercise;
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
        return exerciseRepository.findByNameIgnoreCase(name);
    }

    @Override
    public Exercise updateExercise(Long id, Exercise exercise) {
        boolean exerciseNameExists = exerciseRepository.findByNameIgnoreCase(exercise.getName()).isPresent();
        if(exerciseNameExists){
            throw new IllegalStateException("Could not update. Exercise with name: " + exercise.getName() + " already exists.");
        }
        Optional<Exercise> existingExerciseOptional = exerciseRepository.findById(id);
        if(existingExerciseOptional.isEmpty()){
            throw new IllegalStateException("Could not find exercise with id: " + id);
        }
        exercise.setId(id);
        return exerciseRepository.save(exercise);
    }

    @Override
    public void deleteExercise(Long id) {
        exerciseRepository.deleteById(id);
    }
}
