package com.lmpgttdev.workoutgeneratorapi.exercise;

import com.lmpgttdev.workoutgeneratorapi.model.Exercise;
import com.lmpgttdev.workoutgeneratorapi.model.ExerciseType;
import com.lmpgttdev.workoutgeneratorapi.model.MuscleGroup;
import com.lmpgttdev.workoutgeneratorapi.repository.ExerciseRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@Sql(scripts = "/data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ExerciseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Test
    public void whenFindById_thenReturnExercise() {
        Optional<Exercise> exercise = exerciseRepository.findById(1L);
        assertTrue(exercise.isPresent());
    }

    @Test
    public void whenFindById_thenReturnEmptyIfNotExists() {
        Optional<Exercise> exercise = exerciseRepository.findById(9999L);
        assertTrue(exercise.isEmpty());
    }

    @Test
    public void whenFindByName_thenReturnExercise() {
        Optional<Exercise> exercise = exerciseRepository.findByNameIgnoreCase("tricep dip");
        assertTrue(exercise.isPresent());
    }

    @Test
    public void whenFindByMuscleGroup_thenReturnExercises() {
        List<Exercise> exercises = exerciseRepository.findAllByMuscleGroup(MuscleGroup.CHEST);
        assertTrue(exercises.size() > 0);
        assertEquals(exercises.stream().filter(e -> e.getMuscleGroup().equals(MuscleGroup.CHEST)).count(), exercises.size());
    }

    @Test
    public void whenInsertExercise_thenReturnExercise() {
        Exercise exercise = new Exercise("Test exercise", "This is a test exercise", ExerciseType.STRENGTH, MuscleGroup.ARMS);
        entityManager.persist(exercise);
        entityManager.flush();

        Optional<Exercise> found = exerciseRepository.findByNameIgnoreCase(exercise.getName());
        assertTrue(found.isPresent());
        assertEquals(exercise.getName(), found.get().getName());
    }

    @Test
    public void whenUpdateExercise_thenExerciseNameShouldChange() {
        String oldName = "tricep dip";
        String newName = "Updated name";
        Exercise exercise = exerciseRepository.findByNameIgnoreCase(oldName).orElse(null);
        if (exercise != null) {
            exercise.setName(newName);
            entityManager.persist(exercise);
            entityManager.flush();
        }

        Optional<Exercise> updatedExercise = exerciseRepository.findByNameIgnoreCase(newName);
        assertTrue(updatedExercise.isPresent());
        assertEquals(updatedExercise.get().getName(), newName);
    }

    @Test
    public void whenDeleteExercise_thenExerciseShouldNotBeReturned() {
        Optional<Exercise> exerciseToDelete = exerciseRepository.findByNameIgnoreCase("Hammer curl");
        exerciseToDelete.ifPresent(exercise -> exerciseRepository.deleteById(exercise.getId()));

        Optional<Exercise> foundDeletedExercise = exerciseRepository.findById(exerciseToDelete.get().getId());
        assertTrue(foundDeletedExercise.isEmpty());

    }
}
