package dev.kalbarczyk.workoutservice.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface WorkoutRepository extends ReactiveCrudRepository<WorkoutEntity, String> {
    Flux<WorkoutEntity> findAllByUserId(Long userId);
}
