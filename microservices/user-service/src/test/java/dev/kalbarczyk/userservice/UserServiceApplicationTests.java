package dev.kalbarczyk.userservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.api.event.Event;
import dev.kalbarczyk.userservice.persistence.UserEntity;
import dev.kalbarczyk.userservice.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;


@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {
        "spring.cloud.stream.defaultBinder=rabbit",
        "logging.level.se.magnus=DEBUG"})
class UserServiceApplicationTests extends MySqlTestBase {

    @Autowired
    private WebTestClient client;

    @Autowired
    private UserRepository repository;

    private UserEntity savedUser;

    @Autowired
    @Qualifier("messageProcessor")
    private Consumer<Event<Long, User>> messageProcessor;

    @BeforeEach
    void setupDb() {
        repository.deleteAll();

        var user = UserEntity.builder().username("username").slug("username").password("password").email("email").build();
        savedUser = repository.save(user);
    }

    @Test
    void shouldGetUserById() {
        var userId = savedUser.getId();
        client.get()
                .uri("/users/" + userId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.userId").isEqualTo(userId)
                .jsonPath("$.username").isEqualTo(savedUser.getUsername())
                .jsonPath("$.slug").isEqualTo(savedUser.getSlug())
                .jsonPath("$.email").isEqualTo(savedUser.getEmail())
                .jsonPath("$.password").isEqualTo(savedUser.getPassword())
                .jsonPath("$.createdAt").isEqualTo(savedUser.getCreatedAt())
                .jsonPath("$.updatedAt").isEqualTo(savedUser.getUpdatedAt());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        var userId = savedUser.getId() + 1;
        client.get()
                .uri("/users/" + userId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users/" + userId)
                .jsonPath("$.message").isEqualTo("No user found for userId: " + userId);
    }

    @Test
    void shouldCreateUser() {
        var newUser = new User(null, "newUsername", "newusername", "newEmail@gmail.com", "newPassword", null, null);
        client.post()
                .uri("/users")
                .bodyValue(newUser)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.userId").isNumber()
                .jsonPath("$.username").isEqualTo("newUsername")
                .jsonPath("$.slug").isEqualTo("newusername")
                .jsonPath("$.email").isEqualTo("newEmail@gmail.com")
                .jsonPath("$.password").isEqualTo("newPassword")
                .jsonPath("$.createdAt").isNotEmpty()
                .jsonPath("$.updatedAt").isNotEmpty();
    }

    @Test
    void shouldThrowWhenCreatingInvalidUser() {
        var newUser = new User(null, null, null, "mail.com", "n", null, null);

        client.post()
                .uri("/users")
                .bodyValue(newUser)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                .expectHeader().contentType(APPLICATION_JSON);


    }


    @Test
    void shouldUpdateUser() {
        var userId = savedUser.getId();
        var updated = new User(userId, "updatedUsername", "updatedUsername", "updated@gmail.com", "updatedPassword", null, null);

        client.put()
                .uri("/users/" + userId)
                .bodyValue(updated)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.userId").isEqualTo(userId)
                .jsonPath("$.username").isEqualTo("updatedUsername")
                .jsonPath("$.slug").isEqualTo("updatedusername")
                .jsonPath("$.email").isEqualTo("updated@gmail.com")
                .jsonPath("$.password").isEqualTo("updatedPassword");

        var entity = repository.findById(userId).orElseThrow();
        assertEquals("updatedUsername", entity.getUsername());
        assertEquals("updated@gmail.com", entity.getEmail());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingUser() {
        var userId = savedUser.getId() + 1;
        var updated = new User(userId,
                "wrong",
                "wrong",
                "wrong@mail.com",
                "pass",
                savedUser.getCreatedAt().toString(), savedUser.getUpdatedAt().toString());

        client.put()
                .uri("/users/" + userId)
                .bodyValue(updated)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users/" + userId)
                .jsonPath("$.message").isEqualTo("No user found for userId: " + userId);
    }

    @Test
    void shouldThrowWhenUpdatingUserWithInvalidData() {
        var userId = savedUser.getId();
        var updated = new User(userId, null, null, "invalidEmail", "p", null, null);

        client.put()
                .uri("/users/" + userId)
                .bodyValue(updated)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                .expectHeader().contentType(APPLICATION_JSON);
    }


    @Test
    void shouldDeleteUser() {
        var userId = savedUser.getId();
        client.delete()
                .uri("/users?userId=" + userId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);

        assertTrue(repository.findById(userId).isEmpty());
    }
}
