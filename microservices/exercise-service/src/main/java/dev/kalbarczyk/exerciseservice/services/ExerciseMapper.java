package dev.kalbarczyk.exerciseservice.services;


import dev.kalbarczyk.api.core.exercise.Exercise;
import dev.kalbarczyk.exerciseservice.persistence.ExerciseEntity;
import dev.kalbarczyk.util.DateTimeUtil;
import org.springframework.stereotype.Component;


@Component
public class ExerciseMapper {

    public Exercise entityToApi(final ExerciseEntity entity) {
        if (entity == null) return null;

        return new Exercise(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getMuscleGroup(),
                entity.getInstructions(),
                DateTimeUtil.toString(entity.getCreatedAt()),
                DateTimeUtil.toString(entity.getUpdatedAt())
        );
    }

    public ExerciseEntity apiToEntity(final Exercise api) {
        if (api == null) return null;

        var entity = new ExerciseEntity();
        entity.setId(api.id());
        entity.setName(api.name());
        entity.setType(api.type());
        entity.setMuscleGroup(api.muscleGroup());
        entity.setInstructions(api.instructions());
        entity.setCreatedAt(DateTimeUtil.toLocalDateTime(api.createdAt()));
        entity.setUpdatedAt(DateTimeUtil.toLocalDateTime(api.updatedAt()));
        return entity;
    }

}