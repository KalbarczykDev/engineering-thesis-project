package dev.kalbarczyk.profileservice;

import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.api.event.Event;
import dev.kalbarczyk.profileservice.persistence.ProfileEntity;
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
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;


import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

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
        var event = new Event<>(Event.Type.CREATE, profile.userId(),profile);
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
//
//    @Test
//    void shouldThrowWhenProfileNotFound() {
//        var userId = savedProfile.getUserId() + 1;
//        client.get()
//                .uri("/profiles/" + userId)
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
//                .expectHeader().contentType(APPLICATION_JSON)
//                .expectBody()
//                .jsonPath("$.path").isEqualTo("/profiles/" + userId)
//                .jsonPath("$.message").isEqualTo("No Profile found for userId: " + userId);
//    }
//
//    @Test
//    void shouldCreateProfile() {
//        var newProfile = new Profile(2L, "newDisplayName", "New Bio", "New City", null, null);
//        client.post()
//                .uri("/profiles")
//                .bodyValue(newProfile)
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isEqualTo(HttpStatus.OK)
//                .expectHeader().contentType(APPLICATION_JSON)
//                .expectBody()
//                .jsonPath("$.userId").isEqualTo(newProfile.userId())
//                .jsonPath("$.displayName").isEqualTo(newProfile.displayName())
//                .jsonPath("$.bio").isEqualTo(newProfile.bio())
//                .jsonPath("$.location").isEqualTo(newProfile.location())
//                .jsonPath("$.createdAt").isNotEmpty()
//                .jsonPath("$.updatedAt").isNotEmpty();
//
//    }
//
//    @Test
//    void shouldThrowWhenCreatingInvalidProfile() {
//        var newProfile = new Profile(-888888L, null, null, null, null, null);
//        client.post()
//                .uri("/profiles")
//                .bodyValue(newProfile)
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
//                .expectHeader().contentType(APPLICATION_JSON);
//    }


//    @Test
//    void shouldUpdateProfile() {
//        var userId = savedProfile.getUserId();
//        var updated = new Profile(1L, "updatedDisplayName", "updatedBio", "Updated City", savedProfile.getCreatedAt().toString(), savedProfile.getUpdatedAt().toString());
//
//        client.put()
//                .uri("/profiles/" + userId)
//                .bodyValue(updated)
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isEqualTo(HttpStatus.OK)
//                .expectHeader().contentType(APPLICATION_JSON)
//                .expectBody()
//                .jsonPath("$.userId").isEqualTo(userId)
//                .jsonPath("$.displayName").isEqualTo(updated.displayName())
//                .jsonPath("$.location").isEqualTo(updated.location())
//                .jsonPath("$.bio").isEqualTo(updated.bio());
//
//        var entity = repository.findById(userId).orElseThrow();
//        assertEquals(updated.displayName(), entity.getDisplayName());
//        assertEquals(updated.location(), entity.getLocation());
//        assertEquals(updated.bio(), entity.getBio());
//    }
//
//    @Test
//    void shouldThrowWhenUpdatingProfileForNonExistingUser() {
//        var userId = savedProfile.getUserId() + 1;
//        var updated = new Profile(userId, "updatedDisplayName",
//                "updatedBio", "Updated City",
//                savedProfile.getCreatedAt().toString(), savedProfile.getUpdatedAt().toString());
//
//        client.put()
//                .uri("/profiles/" + userId)
//                .bodyValue(updated)
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
//                .expectHeader().contentType(APPLICATION_JSON);
//    }
//
//    @Test
//    void shouldThrowWhenUpdatingProfileWithInvalidData() {
//        var userId = savedProfile.getUserId();
//        var updated = new Profile(userId, null, null, null, null, null);
//
//        client.put()
//                .uri("/profiles/" + userId)
//                .bodyValue(updated)
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
//                .expectHeader().contentType(APPLICATION_JSON);
//    }
//
//    @Test
//    void shouldDeleteProfile() {
//        var userId = savedProfile.getUserId();
//        client.delete()
//                .uri("/profiles?userId=" + userId)
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isEqualTo(HttpStatus.OK);
//
//        assertTrue(repository.findById(userId).isEmpty());
//    }




}