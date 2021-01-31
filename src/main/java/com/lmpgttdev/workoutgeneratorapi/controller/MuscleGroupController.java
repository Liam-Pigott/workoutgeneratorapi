package com.lmpgttdev.workoutgeneratorapi.controller;

import com.lmpgttdev.workoutgeneratorapi.model.MuscleGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Simple controller for getting enum values and labels to expose in frontend
 */
@RestController
@RequestMapping(path = "/api/v1/musclegroups")
public class MuscleGroupController {

    @GetMapping
    public ResponseEntity<Map<String, String>> getAllMuscleGroups(){
        return ResponseEntity.ok(MuscleGroup.getNamesAndLabels());
    }
}
