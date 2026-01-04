package dev.kalbarczyk.workoutservice.services;

import dev.kalbarczyk.api.core.workout.Workout;
import dev.kalbarczyk.api.core.workout.WorkoutService;
import dev.kalbarczyk.workoutservice.persistence.WorkoutRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
public class WorkoutServiceImpl implements WorkoutService {

    private static final Logger log = LoggerFactory.getLogger(WorkoutServiceImpl.class);

    private final WorkoutRepository repository;
    private final WorkoutMapper mapper;

    public WorkoutServiceImpl(WorkoutRepository repository, WorkoutMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Workout> createWorkout(final Workout workout) {
        log.info("createWorkout: creating workout for userId {}", workout.userId());

        var newEntity = mapper.apiToEntity(workout);
        newEntity.setId(null);

        return repository.save(newEntity)
                .map(mapper::entityToApi)
                .log(log.getName(), java.util.logging.Level.FINE);
    }

    @Override
    public Mono<Workout> updateWorkout(final Workout workout) {
        log.info("updateWorkout: updating workout id {}", workout.id());

        return repository.findById(workout.id())
                .switchIfEmpty(Mono.error(new dev.kalbarczyk.api.exceptions.NotFoundException("Workout not found: " + workout.id())))
                .flatMap(existingEntity -> {
                    var updatedData = mapper.apiToEntity(workout);
                    existingEntity.setName(updatedData.getName());
                    existingEntity.setExercises(updatedData.getExercises());
                    existingEntity.setUserId(updatedData.getUserId());
                    return repository.save(existingEntity);
                })
                .map(mapper::entityToApi)
                .log(log.getName(), java.util.logging.Level.FINE);
    }

    @Override
    public Mono<List<Workout>> getHistory(final int userId) {
        log.info("getHistory: fetching workouts for userId {}", userId);

        return repository.findAllByUserId((long) userId)
                .map(mapper::entityToApi)
                .collectList()
                .log(log.getName(), java.util.logging.Level.FINE);
    }
}
