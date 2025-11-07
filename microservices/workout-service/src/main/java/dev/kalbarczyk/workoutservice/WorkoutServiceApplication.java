package dev.kalbarczyk.workoutservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("dev.kalbarczyk")
public class WorkoutServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkoutServiceApplication.class, args);
    }

}
