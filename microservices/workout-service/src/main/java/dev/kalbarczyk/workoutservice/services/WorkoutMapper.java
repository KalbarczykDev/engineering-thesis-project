package dev.kalbarczyk.workoutservice.services;


import dev.kalbarczyk.api.core.exercise.Exercise;
import dev.kalbarczyk.api.core.workout.ExerciseEntry;
import dev.kalbarczyk.api.core.workout.Series;
import dev.kalbarczyk.api.core.workout.Workout;
import dev.kalbarczyk.util.DateTimeUtil;
import dev.kalbarczyk.workoutservice.persistence.ExerciseEntity;
import dev.kalbarczyk.workoutservice.persistence.ExerciseEntryEntity;
import dev.kalbarczyk.workoutservice.persistence.SeriesEntity;
import dev.kalbarczyk.workoutservice.persistence.WorkoutEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class WorkoutMapper {

    public Workout entityToApi(final WorkoutEntity entity) {
        if (entity == null) return null;

        var apiEntries = entity.getExercises().stream()
                .map(this::entryEntityToApi)
                .collect(Collectors.toList());

        return new Workout(
                entity.getId(),
                entity.getUserId(),
                entity.getName(),
                apiEntries,
                DateTimeUtil.toString(entity.getCreatedAt()),
                DateTimeUtil.toString(entity.getUpdatedAt())
        );
    }

    public WorkoutEntity apiToEntity(final Workout api) {
        if (api == null) return null;

        var entity = new WorkoutEntity();
        entity.setId(api.id());
        entity.setUserId(api.userId());
        entity.setName(api.name());

        if (api.exercises() != null) {
            entity.setExercises(api.exercises().stream()
                    .map(this::entryApiToEntity)
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    private ExerciseEntry entryEntityToApi(final ExerciseEntryEntity entity) {
        var exerciseDto = new Exercise(
                entity.getExercise().getId(),
                entity.getExercise().getName(),
                entity.getExercise().getType(),
                entity.getExercise().getMuscleGroup(),
                entity.getExercise().getInstructions(),
                DateTimeUtil.toString(entity.getExercise().getCreatedAt()),
                DateTimeUtil.toString(entity.getExercise().getUpdatedAt())
        );


        var seriesDtos = entity.getSeries().stream()
                .map(s -> new Series(s.getReps(), s.getWeight(), s.getRestTimeSeconds()))
                .collect(Collectors.toList());

        return new ExerciseEntry(exerciseDto, seriesDtos);
    }

    private ExerciseEntryEntity entryApiToEntity(final ExerciseEntry api) {
        var entity = new ExerciseEntryEntity();

        var ex = api.exercise();
        var exerciseEntity = new ExerciseEntity(
                ex.id(), null, ex.name(), ex.type(), ex.muscleGroup(), ex.instructions(),
                DateTimeUtil.toLocalDateTime(ex.createdAt()),
                DateTimeUtil.toLocalDateTime(ex.updatedAt())
        );
        entity.setExercise(exerciseEntity);

        var seriesEntities = api.series().stream()
                .map(s -> {
                    var sEntity = new SeriesEntity();
                    sEntity.setReps(s.reps());
                    sEntity.setWeight(s.weight());
                    sEntity.setRestTimeSeconds(s.restTimeSeconds());
                    return sEntity;
                })
                .collect(Collectors.toList());
        entity.setSeries(seriesEntities);

        return entity;
    }
}