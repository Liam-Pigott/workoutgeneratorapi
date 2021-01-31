package com.lmpgttdev.workoutgeneratorapi.exercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmpgttdev.workoutgeneratorapi.controller.ExerciseController;
import com.lmpgttdev.workoutgeneratorapi.exception.DuplicateObjectException;
import com.lmpgttdev.workoutgeneratorapi.exception.InvalidParameterException;
import com.lmpgttdev.workoutgeneratorapi.exception.ResourceNotFoundException;
import com.lmpgttdev.workoutgeneratorapi.model.Equipment;
import com.lmpgttdev.workoutgeneratorapi.model.Exercise;
import com.lmpgttdev.workoutgeneratorapi.model.ExerciseType;
import com.lmpgttdev.workoutgeneratorapi.model.MuscleGroup;
import com.lmpgttdev.workoutgeneratorapi.service.impl.ExerciseServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ExerciseController.class)
public class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ExerciseServiceImpl exerciseService;


    private List<Exercise> exerciseList;

    @Before
    public void setUp(){
        this.exerciseList = new ArrayList<>();
        this.exerciseList.add(new Exercise(1L, "Dumbbell Chest Press", "Push weights away from chest", ExerciseType.STRENGTH, MuscleGroup.CHEST, new Equipment("Dumbbell")));
        this.exerciseList.add(new Exercise(2L, "Bodyweight squat", "Squat without additional weight", ExerciseType.STRENGTH, MuscleGroup.QUADS, null));
        this.exerciseList.add(new Exercise(3L, "Barbell Chest Press", "Push weights away from chest", ExerciseType.STRENGTH, MuscleGroup.CHEST, new Equipment("Barbell")));
    }

    private final String malformedJson = "{\n" +
            "    \"name\": \"Barbell bench press\",\n" +
            "    \"description\": \"string\",\n" +
            "    \"tye\":\"STRENGTH\",\n" +
            "    \"muscleGrou\":\"CHEST\"\n" +
            "    \"equipment\":{\"name\":\"Barbell\"}" +
            "}";

    @Test
    public void whenGetAllExercises_thenItShouldReturnListOfExercises() throws Exception {
        given(exerciseService.getAllExercises()).willReturn(exerciseList);

        mockMvc.perform(get("/api/v1/exercises"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(exerciseList.size())));
    }

    @Test
    public void whenFindExercisesByMuscleGroup_thenReturnListOfExercises() throws Exception {
        String param = "chest";
        List<Exercise> chestExercises = exerciseList.stream().filter(e -> e.getMuscleGroup().equals(MuscleGroup.CHEST)).collect(Collectors.toList());
        given(exerciseService.getAllExercisesByMuscleGroup(param)).willReturn(chestExercises);

        mockMvc.perform(get("/api/v1/exercises")
                .param("muscleGroup", param))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.*.muscleGroup", allOf(hasItem("CHEST"))));
    }

    @Test
    public void whenFindExercisesByMuscleGroup_thenThrowMuscleGroupNotValid() throws Exception {
        String param = "ches";
        given(exerciseService.getAllExercisesByMuscleGroup(param)).willThrow(InvalidParameterException.class);

        mockMvc.perform(get("/api/v1/exercises")
                .param("muscleGroup", param))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof InvalidParameterException));
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
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof ResourceNotFoundException));
    }

    @Test
    public void whenExerciseIsCreated_thenItShouldReturnOk() throws Exception {
        Exercise exercise = exerciseList.get(0);
        String json = mapper.writeValueAsString(exercise);

        given(exerciseService.createExercise(Mockito.any(Exercise.class))).willReturn(Optional.of(exercise));

        mockMvc.perform(post("/api/v1/exercises")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        verify(exerciseService, times(1)).createExercise(Mockito.any(Exercise.class));
    }

    @Test
    public void whenExerciseIsCreated_thenItShouldReturnThrowWhenNameExists() throws Exception {
        Exercise exercise = exerciseList.get(0);
        String json = mapper.writeValueAsString(exercise);

        doThrow(DuplicateObjectException.class).when(exerciseService).createExercise(Mockito.any(Exercise.class));

        mockMvc.perform(post("/api/v1/exercises")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isConflict())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof DuplicateObjectException));

        verify(exerciseService, times(1)).createExercise(Mockito.any(Exercise.class));
    }

    @Test
    public void whenCreateExercise_thenItShouldThrowWhenMalformedJson() throws Exception {
        mockMvc.perform(post("/api/v1/exercises")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof HttpMessageNotReadableException));

        then(exerciseService).should(never()).createExercise(Mockito.any(Exercise.class));
    }

    @Test
    public void whenUpdateExercise_thenItShouldReturnOkResponse() throws Exception {
        Exercise toUpdate = exerciseList.get(1);
        Exercise newExercise = new Exercise("Russian twists", "Sit with feet elevated from the floor and rotate torso from side to side.",
                ExerciseType.STRENGTH, MuscleGroup.CORE, null);

        String json = mapper.writeValueAsString(newExercise);

        mockMvc.perform(put("/api/v1/exercises/" + toUpdate.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNoContent());

        verify(exerciseService, times(1)).updateExercise(toUpdate.getId(), newExercise);
    }

    @Test
    public void whenUpdateExercise_thenItShouldThrowWhenIdNotExists() throws Exception {
        Long idNotExist = 999L;
        Exercise newExercise = new Exercise("Russian twists", "Sit with feet elevated from the floor and rotate torso from side to side.",
                ExerciseType.STRENGTH, MuscleGroup.CORE, null);

        String json = mapper.writeValueAsString(newExercise);

        doThrow(new ResourceNotFoundException("Could not find exercise with id: " + idNotExist)).when(exerciseService).updateExercise(idNotExist, newExercise);

        mockMvc.perform(put("/api/v1/exercises/" + idNotExist)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(content().string(containsString("Could not find exercise with id: " + idNotExist)))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof ResourceNotFoundException));;
    }

    @Test
    public void whenUpdateExercise_thenItShouldThrowWhenNameExists() throws Exception {
        Long existingId = 1L;
        String existingName = "Russian twists";
        Exercise newExercise = new Exercise(existingName, "Sit with feet elevated from the floor and rotate torso from side to side.",
                ExerciseType.STRENGTH, MuscleGroup.CORE, null);

        String json = mapper.writeValueAsString(newExercise);

        doThrow(new DuplicateObjectException("Exercise with name " + existingName + " already exists")).when(exerciseService).updateExercise(existingId, newExercise);

        mockMvc.perform(put("/api/v1/exercises/" + existingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(content().string(containsString("Exercise with name " + existingName + " already exists")))
                .andExpect(status().isConflict())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof DuplicateObjectException));
    }

    @Test
    public void whenDeleteExerciseById_thenReturn204Response() throws Exception {
        Exercise exercise = exerciseList.get(0);
        Long existingId = exercise.getId();

        mockMvc.perform(delete("/api/v1/exercises/" + existingId))
                .andExpect(status().isNoContent());

        verify(exerciseService, times(1)).deleteExercise(existingId);
    }

    @Test
    public void whenDeleteExerciseById_thenReturn404WhenNotFound() throws Exception {
        Long idNotExist = 1L;
        doThrow(new ResourceNotFoundException("Could not find exercise with id: " + idNotExist)).when(exerciseService).deleteExercise(idNotExist);

        mockMvc.perform(delete("/api/v1/exercises/" + idNotExist))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof ResourceNotFoundException));

        verify(exerciseService, times(1)).deleteExercise(idNotExist);
    }
}
