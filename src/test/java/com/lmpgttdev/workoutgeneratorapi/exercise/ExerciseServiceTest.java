package com.lmpgttdev.workoutgeneratorapi.exercise;

import com.lmpgttdev.workoutgeneratorapi.exception.DuplicateObjectException;
import com.lmpgttdev.workoutgeneratorapi.exception.InvalidParameterException;
import com.lmpgttdev.workoutgeneratorapi.exception.ResourceNotFoundException;
import com.lmpgttdev.workoutgeneratorapi.model.Equipment;
import com.lmpgttdev.workoutgeneratorapi.model.Exercise;
import com.lmpgttdev.workoutgeneratorapi.model.ExerciseType;
import com.lmpgttdev.workoutgeneratorapi.model.MuscleGroup;
import com.lmpgttdev.workoutgeneratorapi.repository.EquipmentRepository;
import com.lmpgttdev.workoutgeneratorapi.repository.ExerciseRepository;
import com.lmpgttdev.workoutgeneratorapi.service.impl.ExerciseServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @InjectMocks
    private ExerciseServiceImpl exerciseService;

    @Captor
    private ArgumentCaptor<Exercise> exerciseArgumentCaptor;

    private List<Exercise> exerciseList;

    @Before
    public void setUp(){
        this.exerciseList = new ArrayList<>();
        this.exerciseList.add(new Exercise(1L, "Dumbbell Chest Press", "Push weights away from chest", ExerciseType.STRENGTH, MuscleGroup.CHEST, new Equipment("Dumbbell")));
        this.exerciseList.add(new Exercise(2L, "Bodyweight squat", "Squat without additional weight", ExerciseType.STRENGTH, MuscleGroup.QUADS, null));
        this.exerciseList.add(new Exercise(3L, "Barbell Chest Press", "Push weights away from chest", ExerciseType.STRENGTH, MuscleGroup.CHEST, new Equipment("Barbell")));

    }

    @Test
    public void whenGetById_thenItShouldReturnExercise(){
        Exercise toFind = exerciseList.get(0);
        given(exerciseRepository.findById(1L)).willReturn(Optional.of(toFind));

        Optional<Exercise> foundExercise = exerciseRepository.findById(1L);

        assertTrue(foundExercise.isPresent());
        assertEquals(toFind.getName(), foundExercise.get().getName());
    }

    @Test
    public void whenGetAllExercises_thenItShouldReturnListOfExercise(){
        given(exerciseRepository.findAll()).willReturn(exerciseList);

        assertEquals(exerciseService.getAllExercises().size(), exerciseList.size());
    }

    @Test
    public void whenGetExercisesByMuscleGroup_thenItShouldReturnListOfExercise(){
        List<Exercise> chestExercises = exerciseList.stream().filter(e -> e.getMuscleGroup().equals(MuscleGroup.CHEST)).collect(Collectors.toList());
        given(exerciseRepository.findAllByMuscleGroup(MuscleGroup.CHEST)).willReturn(chestExercises);

        assertEquals(exerciseService.getAllExercisesByMuscleGroup("chest").size(), 2);
    }

    @Test
    public void whenGetExercisesByMuscleGroup_thenItShouldThrowWhenMuscleGroupNotExists(){
        String muscleGroup = "unknown group";
        assertThatThrownBy(() -> exerciseService.getAllExercisesByMuscleGroup(muscleGroup))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessageContaining("Could not find corresponding muscle group value for: " + muscleGroup);
    }

    @Test
    public void whenGivenNewExercise_thenItShouldSaveNewExercise() {
        Exercise exercise = exerciseList.get(0);
        Equipment equipment = exercise.getEquipment();

        given(exerciseRepository.findByNameIgnoreCase(exercise.getName())).willReturn(Optional.empty());
        given(equipmentRepository.findByNameIgnoreCase(equipment.getName())).willReturn(Optional.of(equipment));

        Optional<Exercise> savedExercise = exerciseService.createExercise(exercise);

        then(exerciseRepository).should().saveAndFlush(exercise);
        assertEquals(exercise, savedExercise.orElse(null));
    }

    @Test
    public void whenGivenNewExercise_thenItShouldThrowWhenNameAlreadyExists() {
        Exercise duplicateExercise = new Exercise("Duplicate exercise", "This will be a duplicate exercises", ExerciseType.STRENGTH, MuscleGroup.ABS, null);

        given(exerciseRepository.findByNameIgnoreCase(duplicateExercise.getName())).willReturn(Optional.of(duplicateExercise));

        assertThatThrownBy(() -> exerciseService.createExercise(duplicateExercise))
                .isInstanceOf(DuplicateObjectException.class)
                .hasMessageContaining("Exercise with name: " + duplicateExercise.getName() + " already exists");

        then(exerciseRepository).should(never()).saveAndFlush(any(Exercise.class));
    }

    @Test
    public void whenGivenExistingExerciseId_thenItShouldUpdateExercise() {
        Exercise existingExercise = new Exercise("An existing exercise", "This is an existing exercise", ExerciseType.CARDIO, MuscleGroup.CORE, null);
        Exercise newExercise = new Exercise("An updated exercise", "This is an updated exercise", ExerciseType.CARDIO, MuscleGroup.CORE, null);
        given(exerciseRepository.findById(2L)).willReturn(Optional.of(existingExercise));

        when(exerciseRepository.save(any(Exercise.class))).thenAnswer(i -> {
            Exercise exercise = i.getArgument(0);
            exercise.setId(2L);
            return exercise;
        });

        exerciseService.updateExercise(2L, newExercise);

        then(exerciseRepository).should().save(exerciseArgumentCaptor.capture());
        Exercise exerciseCaptorValue = exerciseArgumentCaptor.getValue();
        assertEquals(newExercise, exerciseCaptorValue);
    }

    @Test
    public void whenGivenNonExistingExerciseId_thenItShouldThrowWhenUpdateExercise() {
        Exercise exerciseToUpdate = exerciseList.get(0);
        given(exerciseRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> exerciseService.updateExercise(1L, exerciseToUpdate))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Could not find exercise with id: " + 1L);

        then(exerciseRepository).should(never()).save(any(Exercise.class));
    }

    @Test
    public void whenDeleteExerciseById_thenItShouldRemoveExercise(){
        Long idToDelete = 1L;

        //chained will return for multiple calls to findbyid
        given(exerciseRepository.findById(idToDelete)).willReturn(Optional.ofNullable(exerciseList.get(0))).willReturn(Optional.empty());

        //1st call to findbyid in this method
        exerciseService.deleteExercise(idToDelete);

        //second call to verify
        Optional<Exercise> deletedExercise = exerciseService.getExerciseById(idToDelete);
        assertTrue(deletedExercise.isEmpty());
    }

}
