package dev.kalbarczyk.exerciseservice.services;


import dev.kalbarczyk.api.core.exercise.CreateExercise;
import dev.kalbarczyk.api.core.exercise.Exercise;
import dev.kalbarczyk.api.core.exercise.ExerciseService;
import dev.kalbarczyk.exerciseservice.persistence.ExerciseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;


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
    public Mono<Exercise> createExercise(CreateExercise exercise) {
        return null;
    }

    @Override
    public Mono<Void> deleteExercise(Long id) {
        return null;
    }

    @Override
    public Mono<Exercise> getExercise(Long id) {
        return null;
    }

    @Override
    public Mono<Exercise> updateExercise(Exercise exercise) {
        return null;
    }

    @Override
    public Mono<List<Exercise>> getExercises() {
        return null;
    }
}
