package com.lmpgttdev.workoutgeneratorapi.controller;


import com.lmpgttdev.workoutgeneratorapi.exception.ResourceNotFoundException;
import com.lmpgttdev.workoutgeneratorapi.model.Exercise;
import com.lmpgttdev.workoutgeneratorapi.model.MuscleGroup;
import com.lmpgttdev.workoutgeneratorapi.service.impl.ExerciseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseServiceImpl exerciseService;

    @GetMapping
    public List<Exercise> getAllExercises(@RequestParam(required = false) String muscleGroup) {
        if(muscleGroup != null){
            return exerciseService.getAllExercisesByMuscleGroup(muscleGroup);
        }
        return exerciseService.getAllExercises();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Exercise> getExerciseById(@PathVariable Long id) {
        Optional<Exercise> exercise = exerciseService.getExerciseById(id);
        return exercise.map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("Could not find exercise with id: " + id));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Exercise> createExercise(@RequestBody Exercise exercise) {
        return exerciseService.createExercise(exercise)
                .map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @PutMapping(path = "{id}", consumes = "application/json")
    public ResponseEntity<Void> updateExercise(@PathVariable Long id, @RequestBody Exercise exercise) {
        exerciseService.updateExercise(id, exercise);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteExerciseById(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
