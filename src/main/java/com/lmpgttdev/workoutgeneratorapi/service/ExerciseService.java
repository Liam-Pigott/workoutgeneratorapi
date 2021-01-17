package com.lmpgttdev.workoutgeneratorapi.service;

import com.lmpgttdev.workoutgeneratorapi.model.Exercise;
import com.lmpgttdev.workoutgeneratorapi.model.MuscleGroup;

import java.util.List;
import java.util.Optional;

public interface ExerciseService {

    //Create
    Exercise createExercise(Exercise exercise);

    //Read
    Optional<Exercise> getById(Long id);
    List<Exercise> getAllExercises();
    List<Exercise> getAllExercisesByMuscleGroup(MuscleGroup muscleGroup);
    Optional<Exercise> getExerciseByName(String name);

    //Update
    Exercise updateExercise(Long id, Exercise exercise);

    //Delete
    void deleteExercise(Long id);
}
