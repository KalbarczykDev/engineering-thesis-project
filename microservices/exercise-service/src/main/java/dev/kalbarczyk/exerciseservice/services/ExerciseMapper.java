package dev.kalbarczyk.exerciseservice.services;


import dev.kalbarczyk.api.core.exercise.Exercise;
import dev.kalbarczyk.exerciseservice.persistence.ExerciseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExerciseMapper {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_DATE_TIME;

    public static Exercise entityToApi(ExerciseEntity entity) {
        if (entity == null) return null;

        return new Exercise(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getMuscleGroup(),
                entity.getInstructions(),
                toString(entity.getCreatedAt()),
                toString(entity.getUpdatedAt())
        );
    }

    public static ExerciseEntity apiToEntity(Exercise api) {
        if (api == null) return null;

        ExerciseEntity entity = new ExerciseEntity();
        entity.setId(api.id());
        entity.setName(api.name());
        entity.setType(api.type());
        entity.setMuscleGroup(api.muscleGroup());
        entity.setInstructions(api.instructions());
        entity.setCreatedAt(toLocalDateTime(api.createdAt()));
        entity.setUpdatedAt(toLocalDateTime(api.updatedAt()));
        return entity;
    }

    private static String toString(LocalDateTime time) {
        return time == null ? null : time.format(ISO);
    }

    private static LocalDateTime toLocalDateTime(String time) {
        return time == null ? null : LocalDateTime.parse(time, ISO);
    }
}