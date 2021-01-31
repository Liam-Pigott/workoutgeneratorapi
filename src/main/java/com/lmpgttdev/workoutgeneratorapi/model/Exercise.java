package com.lmpgttdev.workoutgeneratorapi.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "Exercise")
public class Exercise implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", unique=true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ExerciseType type;

    @Column(name = "muscle_group")
    @Enumerated(EnumType.STRING)
    private MuscleGroup muscleGroup;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="equipment_id", referencedColumnName = "id")
    Equipment equipment;

    public Exercise(@NotBlank String name, String description, ExerciseType type, MuscleGroup muscleGroup, Equipment equipment) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.muscleGroup = muscleGroup;
        this.equipment = equipment;
    }
}
