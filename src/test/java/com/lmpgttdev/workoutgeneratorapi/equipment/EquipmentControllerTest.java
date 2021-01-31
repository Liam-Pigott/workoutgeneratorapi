package com.lmpgttdev.workoutgeneratorapi.equipment;

import com.lmpgttdev.workoutgeneratorapi.controller.EquipmentController;
import com.lmpgttdev.workoutgeneratorapi.exception.ResourceNotFoundException;
import com.lmpgttdev.workoutgeneratorapi.model.Equipment;
import com.lmpgttdev.workoutgeneratorapi.service.impl.EquipmentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EquipmentController.class)
public class EquipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipmentServiceImpl equipmentService;

    private final String equipmentEndpoint = "/api/v1/equipment";

    private List<Equipment> equipmentList;

    @Before
    public void setUp(){
        this.equipmentList = new ArrayList<>();
        this.equipmentList.add(new Equipment("Dumbbell"));
        this.equipmentList.add(new Equipment("Barbell"));
        this.equipmentList.add(new Equipment("Ab roller"));
        this.equipmentList.add(new Equipment("Resistance bands"));
    }

    @Test
    public void whenGetAllEquipment_thenItShouldReturnListOfEquipment() throws Exception {
        given(equipmentService.getAllEquipment()).willReturn(equipmentList);

        mockMvc.perform(get(equipmentEndpoint))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(4)));
    }

    @Test
    public void whenFindEquipmentById_thenItShouldReturnEquipment() throws Exception {
        Equipment equipment = equipmentList.get(0);
        given(equipmentService.getEquipmentById(1L)).willReturn(Optional.of(equipment));

        mockMvc.perform(get(equipmentEndpoint + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(equipment.getId()))
                .andExpect(jsonPath("$.name").value(equipment.getName()));
    }

    @Test
    public void whenFindEquipmentByIdNotExists_thenItShouldReturnNotFound() throws Exception {
        given(equipmentService.getEquipmentById(999L)).willReturn(Optional.empty());

        mockMvc.perform(get(equipmentEndpoint + "/999")).andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof ResourceNotFoundException));
    }
}
