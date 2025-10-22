package dev.kalbarczyk.exerciseservice.init;

import dev.kalbarczyk.exerciseservice.model.Exercise;
import dev.kalbarczyk.exerciseservice.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ExerciseSeeder implements CommandLineRunner {

    private final ExerciseRepository exerciseRepository;

    private final List<Exercise> exercises = List.of(
            new Exercise( "Push-Up", "Strength", "Chest", "None", "A basic upper body exercise.", "https://example.com/pushup.jpg"),
            new Exercise( "Squat", "Strength", "Legs", "None", "A fundamental lower body exercise.", "https://example.com/squat.jpg"),
            new Exercise( "Plank", "Core", "Abdominals", "None", "An isometric core strength exercise.", "https://example.com/plank.jpg")
    );

    @Override
    public void run(final String... args){
        if(exerciseRepository.findAll().isEmpty()) {
            exerciseRepository.saveAll(exercises);
        }
    }
}
