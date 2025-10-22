package dev.kalbarczyk.exerciseservice.service;

import dev.kalbarczyk.exerciseservice.model.Exercise;
import dev.kalbarczyk.exerciseservice.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public List<Exercise> findAll() {
       return exerciseRepository.findAll();
    }
}
