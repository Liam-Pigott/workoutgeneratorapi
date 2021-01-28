package com.lmpgttdev.workoutgeneratorapi.equipment;

import com.lmpgttdev.workoutgeneratorapi.model.Equipment;
import com.lmpgttdev.workoutgeneratorapi.model.Exercise;
import com.lmpgttdev.workoutgeneratorapi.repository.EquipmentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@Sql(scripts = "/data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class EquipmentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Test
    public void whenFindById_thenReturnEquipment() {
        Optional<Equipment> equipment = equipmentRepository.findById(1L);
        assertTrue(equipment.isPresent());
    }

    @Test
    public void whenFindById_thenReturnEmptyIfNotExists() {
        Optional<Equipment> equipment = equipmentRepository.findById(9999L);
        assertTrue(equipment.isEmpty());
    }

    @Test
    public void whenFindByName_thenReturnEquipment() {
        Optional<Equipment> equipment = equipmentRepository.findByNameIgnoreCase("barbell");
        assertTrue(equipment.isPresent());
    }

}
