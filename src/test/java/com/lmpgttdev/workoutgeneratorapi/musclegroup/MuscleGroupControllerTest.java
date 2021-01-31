package com.lmpgttdev.workoutgeneratorapi.musclegroup;

import com.lmpgttdev.workoutgeneratorapi.controller.MuscleGroupController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


@RunWith(SpringRunner.class)
@WebMvcTest(MuscleGroupController.class)
public class MuscleGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenGetAllMuscleGroup_thenItShouldReturnListOfMuscleGroup() throws Exception {

    }
}
