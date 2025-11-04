package dev.kalbarczyk.userservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.userservice.persistence.UserEntity;
import dev.kalbarczyk.userservice.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;


@SpringBootTest(webEnvironment = RANDOM_PORT)
class UserServiceApplicationTests extends MySqlTestBase {

    @Autowired
    private WebTestClient client;

    @Autowired
    private UserRepository repository;

    private UserEntity savedUser;


    @BeforeEach
    void setupDb() {
        repository.deleteAll();

        var user = UserEntity.builder().username("username").password("password").email("email").build();
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
                .jsonPath("$.email").isEqualTo(savedUser.getEmail())
                .jsonPath("$.password").isEqualTo(savedUser.getPassword())
                .jsonPath("$.createdAt").isEqualTo(savedUser.getCreatedAt())
                .jsonPath("$.updatedAt").isEqualTo(savedUser.getUpdatedAt());
    }

    @Test
    void shouldThrowWhenUserNotFound(){
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
    void shouldCreateUser(){
        var newUser = new User(null, "newUsername", "newEmail@gmail.com", "newPassword", null, null);
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
                .jsonPath("$.email").isEqualTo("newEmail@gmail.com")
                .jsonPath("$.password").isEqualTo("newPassword")
                .jsonPath("$.createdAt").isNotEmpty()
                .jsonPath("$.updatedAt").isNotEmpty();
    }

    @Test
    void shouldThrowWhenCreatingInvalidUser(){
        var newUser = new User(null, null, "mail.com", "n", null, null);

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
        var updated = new User(userId, "updatedUsername", "updated@gmail.com", "updatedPassword", null, null);

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
                .jsonPath("$.email").isEqualTo("updated@gmail.com")
                .jsonPath("$.password").isEqualTo("updatedPassword");

        var entity = repository.findById(userId).orElseThrow();
        assertEquals("updatedUsername", entity.getUsername());
        assertEquals("updated@gmail.com", entity.getEmail());
    }

    @Test
    void shouldThrowWhenUpdatingUser() {
        var userId = savedUser.getId() + 1;
        var updated = new User(userId, "wrong", "wrong@mail.com", "pass", null, null);

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
