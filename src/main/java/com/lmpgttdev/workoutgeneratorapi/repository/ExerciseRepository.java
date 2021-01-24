package com.lmpgttdev.workoutgeneratorapi.repository;

import com.lmpgttdev.workoutgeneratorapi.model.Exercise;
import com.lmpgttdev.workoutgeneratorapi.model.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    List<Exercise> findAllByMuscleGroup(MuscleGroup muscleGroup);
    Optional<Exercise> findByNameIgnoreCase(String name);
}
