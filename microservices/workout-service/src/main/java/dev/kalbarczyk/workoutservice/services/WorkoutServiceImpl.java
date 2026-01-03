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
        log.info("createWorkout: tries to create workout for userId {}", workout.userId());

        return null;
    }

    @Override
    public Mono<Workout> updateWorkout(final Workout workout) {
        return null;
    }

    @Override
    public Mono<List<Workout>> getHistory(final int userId) {
        return null;
    }
}
