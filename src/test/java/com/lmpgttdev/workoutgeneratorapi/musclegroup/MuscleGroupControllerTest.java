package com.lmpgttdev.workoutgeneratorapi.musclegroup;

import com.lmpgttdev.workoutgeneratorapi.controller.MuscleGroupController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MuscleGroupController.class)
public class MuscleGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenGetAllMuscleGroup_thenItShouldReturnListOfMuscleGroup() throws Exception {
        mockMvc.perform(get("/api/v1/musclegroups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(19)))
                .andExpect(jsonPath("$.TRAPS", containsString("Trapezius (Traps)")));
    }
}
