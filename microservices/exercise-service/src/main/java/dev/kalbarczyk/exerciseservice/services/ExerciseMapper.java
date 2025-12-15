package dev.kalbarczyk.exerciseservice.services;

import dev.kalbarczyk.api.core.exercise.Exercise;
import dev.kalbarczyk.exerciseservice.persistence.ExerciseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {

    DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Mapping(target = "createdAt",
            expression = "java(entity.getCreatedAt() != null ? entity.getCreatedAt().format(FORMATTER) : null)")
    @Mapping(target = "updatedAt",
            expression = "java(entity.getUpdatedAt() != null ? entity.getUpdatedAt().format(FORMATTER) : null)")
    Exercise entityToApi(ExerciseEntity entity);

    @Mapping(target = "createdAt", expression = "java(api.createdAt() != null ? java.time.LocalDateTime.parse(api.createdAt()) : null)")
    @Mapping(target = "updatedAt", expression = "java(api.updatedAt() != null ? java.time.LocalDateTime.parse(api.updatedAt()) : null)")
    @Mapping(target = "version", ignore = true)
    ExerciseEntity apiToEntity(Exercise api);

    List<Exercise> entityToApi(List<ExerciseEntity> entities);

    List<ExerciseEntity> apiToEntity(List<Exercise> entities);
}
