package dev.kalbarczyk.exerciseservice.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends ReactiveCrudRepository<ExerciseEntity, Long> {
}
