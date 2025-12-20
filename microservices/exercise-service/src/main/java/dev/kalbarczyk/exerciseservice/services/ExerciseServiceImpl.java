package dev.kalbarczyk.exerciseservice.services;


import dev.kalbarczyk.api.core.exercise.CreateExercise;
import dev.kalbarczyk.api.core.exercise.Exercise;
import dev.kalbarczyk.api.core.exercise.ExerciseService;
import dev.kalbarczyk.api.exceptions.NotFoundException;
import dev.kalbarczyk.exerciseservice.persistence.ExerciseEntity;
import dev.kalbarczyk.exerciseservice.persistence.ExerciseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.logging.Level.FINE;


@RestController
public class ExerciseServiceImpl implements ExerciseService {

    //logger
    private static final Logger log = LoggerFactory.getLogger(ExerciseServiceImpl.class);

    private final ExerciseRepository repository;
    private final ExerciseMapper mapper;

    public ExerciseServiceImpl(ExerciseRepository repository, ExerciseMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Exercise> createExercise(final CreateExercise exercise) {
        log.info("createExercise: tries to create exercise for name {}", exercise.name());

        var newExercise = new ExerciseEntity();
        newExercise.setName(exercise.name());
        newExercise.setType(exercise.type());
        newExercise.setMuscleGroup(exercise.muscleGroup());
        newExercise.setInstructions(exercise.instructions());
        return repository.save(newExercise).map(mapper::entityToApi);

    }

    @Override
    public Mono<Void> deleteExercise(final Long id) {

        if (id < 1) {
            throw new IllegalArgumentException("Invalid exercise id: " + id);
        }

        log.debug("deleteExercise: tries to delete exercise for id {}", id);
        return repository.findById(id).log(log.getName(), FINE).map(repository::delete).flatMap(e -> e);
    }

    @Override
    public Mono<Exercise> getExercise(final Long id) {
        log.debug("getExercise: tries to get exercise for id {}", id);

        if (id < 1) {
            throw new IllegalArgumentException("Invalid exercise id: " + id);
        }

        log.info("Will get exercise for id={}", id);

        return repository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("No exercise found for id: " + id)))
                .log(log.getName(), FINE)
                .map(mapper::entityToApi);

    }

    @Override
    public Mono<Exercise> updateExercise(final Exercise exercise) {
        log.debug("updateExercise: tries to get exercise for id {}", exercise.id());

        if (exercise.id() < 1) {
            throw new IllegalArgumentException("Invalid exercise id: " + exercise.id());
        }


        return repository.findById(exercise.id())
                .switchIfEmpty(Mono.error(new NotFoundException("No exercise found for id: " + exercise.id())))
                .flatMap(existingEntity -> {
                    existingEntity.setName(exercise.name());
                    existingEntity.setType(exercise.type());
                    existingEntity.setMuscleGroup(exercise.muscleGroup());
                    existingEntity.setInstructions(exercise.instructions());
                    return repository.save(existingEntity);
                })
                .map(mapper::entityToApi)
                .doOnSuccess(updated ->
                        log.debug("updateExercise: modified an entity with id: {}", updated.id())
                )
                .log(log.getName(), FINE);

    }

    @Override
    public Mono<List<Exercise>> getExercises() {
        log.debug("getExercises: tries to get exercises for all exercises");
        return repository.findAll()
                .map(mapper::entityToApi)
                .collectList()
                .log(log.getName(), FINE);
    }
}
