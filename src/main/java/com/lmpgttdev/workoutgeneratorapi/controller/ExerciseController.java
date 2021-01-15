package com.lmpgttdev.workoutgeneratorapi.controller;


import com.lmpgttdev.workoutgeneratorapi.model.Exercise;
import com.lmpgttdev.workoutgeneratorapi.service.impl.ExerciseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Exercise> getAllExercise(){
        return exerciseService.getAllExercises();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Exercise> getExerciseById(@PathVariable Long id){
        Optional<Exercise> exercise = exerciseService.getById(id);
        if(exercise.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok((exercise.get()));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public void createExercise(@RequestBody Exercise exercise){
        exerciseService.createExercise(exercise);
    }
}
