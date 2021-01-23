package com.lmpgttdev.workoutgeneratorapi.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "Exercise")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ExerciseType type;

    @Column(name = "muscle_group")
    @Enumerated(EnumType.STRING)
    private MuscleGroup muscleGroup;

    public Exercise(@NotBlank String name, String description, ExerciseType type, MuscleGroup muscleGroup) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.muscleGroup = muscleGroup;
    }
}
