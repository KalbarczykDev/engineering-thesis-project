package dev.kalbarczyk.profileservice;

import static org.junit.jupiter.api.Assertions.*;

import dev.kalbarczyk.profileservice.persistence.ProfileEntity;
import dev.kalbarczyk.profileservice.persistence.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
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

    @Test
    void shouldUpdateProfile() {
        savedEntity.setDisplayName("Updated");

        StepVerifier.create(repository.save(savedEntity))
                .expectNextMatches(updatedEntity ->
                        updatedEntity.getVersion() == 1 &&
                                "Updated".equals(updatedEntity.getDisplayName()))
                .verifyComplete();

        StepVerifier.create(repository.findById(savedEntity.getUserId()))
                .expectNextMatches(foundEntity ->
                        foundEntity.getVersion() == 1 &&
                                "Updated".equals(foundEntity.getDisplayName()))
                .verifyComplete();
    }

    @Test
    void shouldDeleteProfile() {
        StepVerifier.create(repository.delete(savedEntity)).verifyComplete();
        StepVerifier.create(repository.existsById(savedEntity.getUserId())).expectNext(false).verifyComplete();
    }

    @Test
    void shouldGetByUserId() {
        StepVerifier.create(repository.findByUserId(savedEntity.getUserId()))
                .expectNextMatches(foundEntity -> savedEntity.equals(foundEntity))
                .verifyComplete();
    }

    @Test
    void shouldThrowDuplicateError() {
        var entity = ProfileEntity.builder()
                .userId(1L)
                .displayName("test")
                .location("Test City")
                .bio("This is a test profile.")
                .build();
        StepVerifier.create(repository.save(entity)).expectError(DuplicateKeyException.class).verify();
    }

    @Test
    void shouldThrowOptimisticLockError() {
        var entity1 = repository.findById(savedEntity.getUserId()).block();
        var entity2 = repository.findById(savedEntity.getUserId()).block();

        assertNotNull(entity1);
        entity1.setDisplayName("First Update");
        repository.save(entity1).block();

        assertNotNull(entity2);
        StepVerifier.create(repository.save(entity2)).expectError(OptimisticLockingFailureException.class).verify();

        StepVerifier.create(repository.findById(savedEntity.getUserId()))
                .expectNextMatches(foundEntity ->
                        foundEntity.getVersion() == 1
                                && foundEntity.getDisplayName().equals("First Update"))
                .verifyComplete();
    }


}
