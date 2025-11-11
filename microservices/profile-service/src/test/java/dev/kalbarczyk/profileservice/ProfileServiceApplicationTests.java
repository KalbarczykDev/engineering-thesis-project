package dev.kalbarczyk.profileservice;

import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.api.event.Event;
import dev.kalbarczyk.profileservice.persistence.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.function.Consumer;


@SpringBootTest(webEnvironment = RANDOM_PORT)
class ProfileServiceApplicationTests extends MongoDbTestBase {

    @Autowired
    private WebTestClient client;

    @Autowired
    private ProfileRepository repository;

    @Autowired
    @Qualifier("messageProcessor")
    private Consumer<Event<Long, Profile>> messageProcessor;


    @BeforeEach
    void setupDb() {
        repository.deleteAll().block();
    }

    @Test
    void shouldGetProfileByUserId() {
        var userId = 1L;

        assertNull(repository.findByUserId(userId).block());
        assertEquals(0, repository.count().block());


        var profile = new Profile(userId, "displayName", "Bio", "City", null, null);
        var event = new Event<>(Event.Type.CREATE, profile.userId(), profile);
        messageProcessor.accept(event);

        assertNotNull(repository.findByUserId(userId).block());
        assertEquals(1, repository.count().block());

        client.get()
                .uri("/profiles/" + userId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody();

    }

    @Test
    void shouldThrowWhenProfileNotFound() {
        var createdUserId = 1L;
        var profile = new Profile(createdUserId, "displayName", "Bio", "City", null, null);
        var event = new Event<>(Event.Type.CREATE, profile.userId(), profile);
        messageProcessor.accept(event);

        var savedProfile = repository.findByUserId(createdUserId).block();
        assertNotNull(savedProfile);

        var userId = savedProfile.getUserId() + 1;
        client.get()
                .uri("/profiles/" + userId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/profiles/" + userId)
                .jsonPath("$.message").isEqualTo("No profile found for userId: " + userId);
    }

    @Test
    void shouldCreateProfile() {
        var newProfile = new Profile(2L, "newDisplayName", "New Bio", "New City", null, null);
        var event = new Event<>(Event.Type.CREATE, newProfile.userId(), newProfile);
        messageProcessor.accept(event);

        var saved = repository.findByUserId(newProfile.userId()).block();
        assertNotNull(saved);
        assertEquals(newProfile.userId(), saved.getUserId());
        assertEquals(newProfile.displayName(), saved.getDisplayName());
        assertEquals(newProfile.bio(), saved.getBio());
        assertEquals(newProfile.location(), saved.getLocation());
    }

    @Test
    void shouldThrowWhenCreatingInvalidProfile() {
        var newProfile = new Profile(-888888L, null, null, null, null, null);
        var event = new Event<>(Event.Type.CREATE, newProfile.userId(), newProfile);

        assertThrows(RuntimeException.class, () -> messageProcessor.accept(event));
        assertEquals(0, repository.count().block());
    }


    @Test
    void shouldUpdateProfile() {
        var userId = 1L;
        var profile = new Profile(userId, "displayName", "bio", "city", null, null);
        var createEvent = new Event<>(Event.Type.CREATE, userId, profile);
        messageProcessor.accept(createEvent);

        var updated = new Profile(userId, "updatedDisplayName", "updatedBio", "Updated City", null, null);
        var updateEvent = new Event<>(Event.Type.UPDATE, userId, updated);
        messageProcessor.accept(updateEvent);

        var entity = repository.findByUserId(userId).block();
        assertNotNull(entity);
        assertEquals(updated.displayName(), entity.getDisplayName());
        assertEquals(updated.bio(), entity.getBio());
        assertEquals(updated.location(), entity.getLocation());
    }

    @Test
    void shouldThrowWhenUpdatingProfileForNonExistingUser() {
        var userId = 999L;
        var updated = new Profile(userId, "updatedDisplayName", "updatedBio", "Updated City", null, null);
        var event = new Event<>(Event.Type.UPDATE, userId, updated);

        assertThrows(RuntimeException.class, () -> messageProcessor.accept(event));
        assertNull(repository.findByUserId(userId).block());
    }

    @Test
    void shouldDeleteProfile() {
        var userId = 1L;
        var profile = new Profile(userId, "displayName", "bio", "city", null, null);
        messageProcessor.accept(new Event<>(Event.Type.CREATE, userId, profile));

        messageProcessor.accept(new Event<>(Event.Type.DELETE, userId, null));

        assertNull(repository.findByUserId(userId).block());
        assertEquals(0, repository.count().block());
    }


}