package com.lmpgttdev.workoutgeneratorapi.model;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum MuscleGroup {
    CHEST("Chest"),
    BACK("Back"),
    ARMS("Arms"),
    ABS("Abdominals"),
    LEGS("Legs"),
    SHOULDERS("Shoulders"),
    CALVES("Calves"),
    HAMSTRINGS("Hamstrings"),
    QUADS("Quadriceps"),
    GLUTES("Glutes"),
    BICEPS("Biceps"),
    TRICEPS("Triceps"),
    FOREARMS("Forearms"),
    TRAPS("Trapezius (Traps)"),
    LATS("Latissimus Dorsi (Lats)"),
    UPPER_BODY("Upper Body"),
    LOWER_BODY("Lower Body"),
    CORE("Core"),
    UNKNOWN("Unknown");

    private final String label;

    MuscleGroup(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    /**
     * Helper to get muscle group when only the label is known.
     *
     * @param label
     * @return
     */
    public static Optional<MuscleGroup> getByLabel(String label) {
        return Arrays.stream(values())
                .filter(group -> group.label.equals(label))
                .findFirst();
    }

    public static Optional<MuscleGroup> getByNameIgnoreCase(String name){
        return Arrays.stream(values())
                .filter(group -> group.name().equalsIgnoreCase(name))
                .findFirst();
    }

    public static Map<String, String> getNamesAndLabels(){
        return Arrays.stream(MuscleGroup.values())
                .collect(Collectors.toMap(MuscleGroup::name, MuscleGroup::getLabel));
    }
}
