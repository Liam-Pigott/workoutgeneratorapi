package com.lmpgttdev.workoutgeneratorapi.exercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmpgttdev.workoutgeneratorapi.controller.ExerciseController;
import com.lmpgttdev.workoutgeneratorapi.exception.DuplicateObjectException;
import com.lmpgttdev.workoutgeneratorapi.model.Exercise;
import com.lmpgttdev.workoutgeneratorapi.model.ExerciseType;
import com.lmpgttdev.workoutgeneratorapi.model.MuscleGroup;
import com.lmpgttdev.workoutgeneratorapi.service.impl.ExerciseServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(ExerciseController.class)
public class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExerciseServiceImpl exerciseService;

    private List<Exercise> exerciseList;

    @Before
    public void setUp(){
        this.exerciseList = new ArrayList<>();
        this.exerciseList.add(new Exercise(1L, "Dumbbell Chest Press", "Push weights away from chest", ExerciseType.STRENGTH, MuscleGroup.CHEST));
        this.exerciseList.add(new Exercise(2L, "Bodyweight squat", "Squat without additional weight", ExerciseType.STRENGTH, MuscleGroup.QUADS));
    }

    @Test
    public void whenGetAllExercises_thenItShouldReturnListOfExercises() throws Exception {
        given(exerciseService.getAllExercises()).willReturn(exerciseList);

        mockMvc.perform(get("/api/v1/exercises")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }

    @Test
    public void whenFindExerciseById_thenItShouldReturnExercise() throws Exception {
        Exercise exercise = exerciseList.get(0);
        given(exerciseService.getExerciseById(1L)).willReturn(Optional.of(exercise));

        mockMvc.perform(get("/api/v1/exercises/1")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(exercise.getId()))
                .andExpect(jsonPath("$.name").value(exercise.getName()));
    }

    @Test
    public void whenFindExerciseByIdNotExists_thenItShouldReturnNotFound() throws Exception {
        given(exerciseService.getExerciseById(999L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/exercises/999")).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenExerciseIsCreated_thenItShouldReturnOk() throws Exception {
        Exercise exercise = new Exercise("Kettlebell swing", "Swing kettlebell between legs", ExerciseType.STRENGTH, MuscleGroup.CORE);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(exercise);

        given(exerciseService.createExercise(exercise)).willReturn(Optional.of(exercise));

        mockMvc.perform(post("/api/v1/exercises")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        verify(exerciseService, times(1)).createExercise(exercise);
    }

    @Test
    public void whenExerciseIsCreated_thenItShouldReturnThrowWhenNameExists() throws Exception {
        Exercise exercise = new Exercise("Kettlebell swing", "Swing kettlebell between legs", ExerciseType.STRENGTH, MuscleGroup.CORE);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(exercise);

        doThrow(new DuplicateObjectException("Exercise with name: " + exercise.getName() + " already exists")).when(exerciseService).createExercise(exercise);

        mockMvc.perform(post("/api/v1/exercises")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isConflict());

        verify(exerciseService, times(1)).createExercise(exercise);
    }
}
