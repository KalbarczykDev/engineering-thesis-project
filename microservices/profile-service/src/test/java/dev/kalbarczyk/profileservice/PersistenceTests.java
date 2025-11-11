package dev.kalbarczyk.profileservice;

import static org.junit.jupiter.api.Assertions.*;

import dev.kalbarczyk.profileservice.persistence.ProfileEntity;
import dev.kalbarczyk.profileservice.persistence.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import java.util.Objects;

@DataMongoTest
public class PersistenceTests extends MongoDbTestBase {

    @Autowired
    private ProfileRepository repository;

    private ProfileEntity savedEntity;

    @BeforeEach
    void setupDb() {

        StepVerifier.create(repository.deleteAll()).verifyComplete();


        var entity = ProfileEntity.builder()
                .userId(1L)
                .displayName("test")
                .location("Test City")
                .bio("This is a test profile.")
                .build();

        StepVerifier.create(repository.save(entity))
                .expectNextMatches(createdEntity -> {
                    savedEntity = createdEntity;
                    return entity.equals(savedEntity);
                })
                .verifyComplete();

    }

    @Test
    void shouldCreateProfile() {
        var newEntity = ProfileEntity.builder()
                .userId(2L)
                .displayName("test")
                .location("Test City")
                .bio("This is a test profile.")
                .build();

        StepVerifier.create(repository.save(newEntity))
                .expectNextMatches(createdEntity -> Objects.equals(newEntity.getUserId(), createdEntity.getUserId()))
                .verifyComplete();

        StepVerifier.create(repository.findById(newEntity.getUserId()))
                .expectNextMatches(newEntity::equals)
                .verifyComplete();

        StepVerifier.create(repository.count()).expectNext(2L).verifyComplete();
    }

//    @Test
//    void shouldUpdateProfile() {
//        savedEntity.setDisplayName("Updated");
//        repository.save(savedEntity);
//        var foundEntity = repository.findById(savedEntity.getUserId()).orElseThrow();
//        assertEquals(1, (long) foundEntity.getVersion());
//        assertEquals("Updated", foundEntity.getDisplayName());
//    }
//
//    @Test
//    void shouldDeleteProfile() {
//        repository.delete(savedEntity);
//        assertFalse(repository.existsById(savedEntity.getUserId()));
//    }
//
//    @Test
//    void shouldGetByUserId() {
//        var foundEntity = repository.findById(savedEntity.getUserId()).orElseThrow();
//        assertEqualsProfile(savedEntity, foundEntity);
//    }
//
//    @Test
//    void shouldThrowDuplicateError() {
//        assertThrows(Exception.class, () -> {
//            var entity = ProfileEntity.builder()
//                    .userId(1L)
//                    .displayName("test")
//                    .location("Test City")
//                    .bio("This is a test profile.")
//                    .build();
//            repository.save(entity);
//        });
//    }
//
//    @Test
//    void shouldThrowOptimisticLockError() {
//        var entity1 = repository.findById(savedEntity.getUserId()).orElseThrow();
//        var entity2 = repository.findById(savedEntity.getUserId()).orElseThrow();
//
//        entity1.setDisplayName("First Update");
//        repository.save(entity1);
//
//        assertThrows(Exception.class, () -> {
//            entity2.setDisplayName("Second Update");
//            repository.save(entity2);
//        });
//
//        var updatedEntity = repository.findById(savedEntity.getUserId()).orElseThrow();
//        assertEquals(1, (long) updatedEntity.getVersion());
//        assertEquals("First Update", updatedEntity.getDisplayName());
//    }


}
