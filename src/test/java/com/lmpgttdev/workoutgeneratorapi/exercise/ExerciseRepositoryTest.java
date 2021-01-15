package com.lmpgttdev.workoutgeneratorapi.exercise;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ExerciseRepositoryTest {

    @Test
    public void whenFindByMuscleGroup_thenReturnExercises(){
        assertTrue(false);
    }

    @Test
    public void whenFindById_thenReturnExerciseIfExists(){
        assertTrue(false);
    }

    @Test
    public void whenFindById_thenReturnName(){
        assertTrue(false);
    }

    @Test
    public void whenFindById_throwWhenExerciseNotExists(){
        assertTrue(false);
    }

    @Test
    public void whenInsertExercise_thenReturnExercise(){
        assertTrue(false);
    }

    @Test
    public void whenUpdateExercise_thenExerciseNameShouldChange(){
        assertTrue(false);
    }

    @Test
    public void whenDeleteExercise_thenExerciseShouldNotBeReturned(){
        assertTrue(false);
    }
}
