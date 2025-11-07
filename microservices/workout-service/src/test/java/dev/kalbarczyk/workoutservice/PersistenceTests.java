package dev.kalbarczyk.workoutservice;

import dev.kalbarczyk.workoutservice.persistence.WorkoutEntity;
import dev.kalbarczyk.workoutservice.persistence.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
public class PersistenceTests extends MongoDbTestBase {

    @Autowired
    private WorkoutRepository repository;

    private WorkoutEntity savedEntity;


    @BeforeEach
    void setupDb() {
        repository.deleteAll();

        var entity = WorkoutEntity.builder()
                .userId(1L)
                .date(LocalDateTime.now())
                .name("Morning Workout")
                .build();


        savedEntity = repository.save(entity);

        assertEqualsWorkout(entity, savedEntity);
    }

    @Test
    void shouldCreateWorkout() {
        var entity = WorkoutEntity.builder()
                .userId(2L)
                .date(LocalDateTime.now())
                .name("Evening Workout")
                .build();

        repository.save(entity);

        var foundEntity = repository.findById(entity.getId()).orElseThrow();

        assertEqualsWorkout(entity, foundEntity);
        assertEquals(2, repository.count());
    }

    @Test
    void shouldUpdateWorkout() {
        savedEntity.setName("Evening Workout");
        repository.save(savedEntity);
        var foundEntity = repository.findById(savedEntity.getId()).orElseThrow();
        assertEquals(1, (long) foundEntity.getVersion());
        assertEquals("Evening Workout", foundEntity.getName());
    }

    @Test
    void shouldDeleteWorkout() {
        repository.delete(savedEntity);
        assertEquals(0, repository.count());
    }

    @Test
    void shouldGetByWorkoutId() {
        var foundEntity = repository.findById(savedEntity.getId()).orElseThrow();
        assertEqualsWorkout(savedEntity, foundEntity);
    }


    @Test
    void shouldThrowDuplicateError() {
        assertThrows(Exception.class, () -> {
            var entity = WorkoutEntity.builder()
                    .id(savedEntity.getId())
                    .userId(savedEntity.getUserId())
                    .date(savedEntity.getDate())
                    .name(savedEntity.getName())
                    .build();
            repository.save(entity);
        });
    }

    @Test
    void shouldThrowOptimisticLockError(){
        var entity1 = repository.findById(savedEntity.getId()).orElseThrow();
        var entity2 = repository.findById(savedEntity.getId()).orElseThrow();

        entity1.setName("Workout 1");
        repository.save(entity1);

        assertThrows(Exception.class, () -> {
            entity2.setName("Workout 2");
            repository.save(entity2);
        });

        var updatedEntity = repository.findById(savedEntity.getId()).orElseThrow();
        assertEquals(1, (long) updatedEntity.getVersion());
        assertEquals("Workout 1", updatedEntity.getName());
    }

    private void assertEqualsWorkout(final WorkoutEntity entity, final WorkoutEntity savedEntity) {
        assert entity.getUserId().equals(savedEntity.getUserId());
        assert entity.getName().equals(savedEntity.getName());
        assert entity.getDate().equals(savedEntity.getDate());
    }
}
