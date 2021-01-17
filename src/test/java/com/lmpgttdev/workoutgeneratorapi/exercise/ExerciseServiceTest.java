package com.lmpgttdev.workoutgeneratorapi.exercise;

import com.lmpgttdev.workoutgeneratorapi.model.Exercise;
import com.lmpgttdev.workoutgeneratorapi.model.ExerciseType;
import com.lmpgttdev.workoutgeneratorapi.model.MuscleGroup;
import com.lmpgttdev.workoutgeneratorapi.repository.ExerciseRepository;
import com.lmpgttdev.workoutgeneratorapi.service.impl.ExerciseServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseServiceImpl exerciseService;

    @Captor
    private ArgumentCaptor<Exercise> exerciseArgumentCaptor;

    @Test
    public void whenGivenNewExercise_thenItShouldSaveNewExercise(){
        Exercise exercise = new Exercise("A new exercise", "This is a test exercise", ExerciseType.STRENGTH, MuscleGroup.ABS);

        given(exerciseRepository.findByNameIgnoreCase(exercise.getName())).willReturn(Optional.empty());

        exerciseService.createExercise(exercise);

        then(exerciseRepository).should().saveAndFlush(exerciseArgumentCaptor.capture());
        Exercise exerciseCaptorValue = exerciseArgumentCaptor.getValue();
        assertEquals(exerciseCaptorValue, exercise);
    }

    @Test
    public void whenGivenNewExercise_thenItShouldThrowWhenNameAlreadyExists(){
        Exercise duplicateExercise = new Exercise("Duplicate exercise", "This will be a duplicate exercises", ExerciseType.STRENGTH, MuscleGroup.ABS);

        given(exerciseRepository.findByNameIgnoreCase(duplicateExercise.getName())).willReturn(Optional.of(duplicateExercise));

        assertThatThrownBy(() -> exerciseService.createExercise(duplicateExercise))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Exercise with name: " + duplicateExercise.getName() + " already exists");

        then(exerciseRepository).should(never()).saveAndFlush(any(Exercise.class));
    }

    @Test
    public void whenGivenExistingExerciseId_thenItShouldUpdateExercise(){
        Exercise existingExercise = new Exercise("An existing exercise", "This is an existing exercise", ExerciseType.CARDIO, MuscleGroup.CORE);
        Exercise newExercise = new Exercise("An updated exercise", "This is an updated exercise", ExerciseType.CARDIO, MuscleGroup.CORE);
        given(exerciseRepository.findById(2L)).willReturn(Optional.of(existingExercise));

        when(exerciseRepository.save(any(Exercise.class))).thenAnswer(i -> {
            Exercise exercise = i.getArgument(0);
            exercise.setId(2L);
            return exercise;
        });

        Exercise updatedExercise = exerciseService.updateExercise(2L, newExercise);
        assertEquals(newExercise, updatedExercise);
    }

    @Test
    public void whenGivenNonExistingExerciseId_thenItShouldThrowWhenUpdateExercise(){
        Exercise exerciseToUpdate = new Exercise("An updated exercise", "This is an updated exercise", ExerciseType.CARDIO, MuscleGroup.CORE);
        given(exerciseRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> exerciseService.updateExercise(1L, exerciseToUpdate))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Could not find exercise with id: " + 1L);

        then(exerciseRepository).should(never()).save(any(Exercise.class));
    }



}
