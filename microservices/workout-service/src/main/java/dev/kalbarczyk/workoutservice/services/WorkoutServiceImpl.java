package dev.kalbarczyk.workoutservice.services;

import dev.kalbarczyk.api.core.workout.WorkoutService;
import dev.kalbarczyk.workoutservice.persistence.WorkoutRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WorkoutServiceImpl implements WorkoutService {

    private static final Logger log = LoggerFactory.getLogger(WorkoutServiceImpl.class);

    private final WorkoutRepository repository;
    private final WorkoutMapper mapper;

    public WorkoutServiceImpl(WorkoutRepository repository, WorkoutMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

}
