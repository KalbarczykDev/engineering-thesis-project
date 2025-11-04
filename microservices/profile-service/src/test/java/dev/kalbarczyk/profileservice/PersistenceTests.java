package dev.kalbarczyk.profileservice;

import static org.junit.jupiter.api.Assertions.*;

import dev.kalbarczyk.profileservice.persistence.ProfileEntity;
import dev.kalbarczyk.profileservice.persistence.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
public class PersistenceTests extends MongoDbTestBase {

    @Autowired
    private ProfileRepository repository;

    private ProfileEntity savedEntity;

    @BeforeEach
    void setupDb() {
        repository.deleteAll();

        var entity = ProfileEntity.builder()
                .userId(1L)
                .displayName("test")
                .avatarUrl("https://avatars.example.com")
                .location("Test City")
                .bio("This is a test profile.")
                .build();

        savedEntity = repository.save(entity);

        assertEqualsProfile(entity, savedEntity);
    }

    @Test
    void shouldCreateProfile() {
        var entity = ProfileEntity.builder()
                .userId(2L)
                .displayName("test")
                .avatarUrl("https://avatars.example.com")
                .location("Test City")
                .bio("This is a test profile.")
                .build();

        repository.save(entity);

        var foundEntity = repository.findById(entity.getUserId()).orElseThrow();

        assertEqualsProfile(entity, foundEntity);
        assertEquals(2, repository.count());
    }

    @Test
    void shouldUpdateProfile() {
        savedEntity.setDisplayName("Updated");
        repository.save(savedEntity);
        var foundEntity = repository.findById(savedEntity.getUserId()).orElseThrow();
        assertEquals(1, (long) foundEntity.getVersion());
        assertEquals("Updated", foundEntity.getDisplayName());
    }

    @Test
    void shouldDeleteProfile() {
        repository.delete(savedEntity);
        assertFalse(repository.existsById(savedEntity.getUserId()));
    }

    @Test
    void shouldGetByUserId() {
        var foundEntity = repository.findById(savedEntity.getUserId()).orElseThrow();
        assertEqualsProfile(savedEntity, foundEntity);
    }

    @Test
    void shouldThrowDuplicateError() {
        assertThrows(Exception.class, () -> {
            var entity = ProfileEntity.builder()
                    .userId(1L)
                    .displayName("test")
                    .avatarUrl("https://avatars.example.com")
                    .location("Test City")
                    .bio("This is a test profile.")
                    .build();
            repository.save(entity);
        });
    }

    @Test
    void shouldThrowOptimisticLockError(){
        var entity1 = repository.findById(savedEntity.getUserId()).orElseThrow();
        var entity2 = repository.findById(savedEntity.getUserId()).orElseThrow();

        entity1.setDisplayName("First Update");
        repository.save(entity1);

        assertThrows(Exception.class, () -> {
            entity2.setDisplayName("Second Update");
            repository.save(entity2);
        });

        var updatedEntity = repository.findById(savedEntity.getUserId()).orElseThrow();
        assertEquals(1, (long) updatedEntity.getVersion());
        assertEquals("First Update", updatedEntity.getDisplayName());
    }


    private void assertEqualsProfile(final ProfileEntity entity, final ProfileEntity savedEntity) {
        assert entity.getUserId().equals(savedEntity.getUserId());
        assert entity.getDisplayName().equals(savedEntity.getDisplayName());
        assert entity.getAvatarUrl().equals(savedEntity.getAvatarUrl());
        assert entity.getLocation().equals(savedEntity.getLocation());
        assert entity.getBio().equals(savedEntity.getBio());
    }

}
