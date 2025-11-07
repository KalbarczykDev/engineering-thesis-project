package dev.kalbarczyk.workoutservice.persistence;

import org.springframework.data.repository.CrudRepository;

public interface WorkoutRepository extends CrudRepository<WorkoutEntity, Long> {
}
