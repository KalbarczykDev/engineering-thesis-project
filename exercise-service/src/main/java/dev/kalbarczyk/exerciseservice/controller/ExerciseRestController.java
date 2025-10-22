package dev.kalbarczyk.exerciseservice.controller;

import dev.kalbarczyk.exerciseservice.model.Exercise;
import dev.kalbarczyk.exerciseservice.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseRestController {

    private final ExerciseService exerciseService;

    @GetMapping
    public List<Exercise> findAll() {
        return exerciseService.findAll();
    }
}
