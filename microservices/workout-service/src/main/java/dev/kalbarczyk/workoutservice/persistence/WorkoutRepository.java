package dev.kalbarczyk.workoutservice.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRepository extends ReactiveCrudRepository<WorkoutEntity, String> {
}
