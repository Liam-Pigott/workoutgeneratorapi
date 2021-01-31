package com.lmpgttdev.workoutgeneratorapi.equipment;

import com.lmpgttdev.workoutgeneratorapi.model.Equipment;
import com.lmpgttdev.workoutgeneratorapi.repository.EquipmentRepository;
import com.lmpgttdev.workoutgeneratorapi.service.impl.EquipmentServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentServiceTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @InjectMocks
    private EquipmentServiceImpl equipmentService;

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
    public void whenGetById_thenItShouldReturnEquipment(){
        Equipment toFind = equipmentList.get(0);
        given(equipmentRepository.findById(1L)).willReturn(Optional.of(toFind));

        Optional<Equipment> foundEquipment = equipmentRepository.findById(1L);

        assertTrue(foundEquipment.isPresent());
        assertEquals(toFind.getName(), foundEquipment.get().getName());
    }

    @Test
    public void whenGetAllEquipment_thenItShouldReturnListOfEquipment(){
        given(equipmentRepository.findAll()).willReturn(equipmentList);

        assertEquals(equipmentService.getAllEquipment().size(), 4);
    }
}
