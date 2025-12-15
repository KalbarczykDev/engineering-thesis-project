package dev.kalbarczyk.exerciseservice.services;


import dev.kalbarczyk.api.core.exercise.CreateExercise;
import dev.kalbarczyk.api.core.exercise.Exercise;
import dev.kalbarczyk.api.core.exercise.ExerciseService;
import dev.kalbarczyk.exerciseservice.persistence.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository repository;
    private final ExerciseMapper mapper;


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
