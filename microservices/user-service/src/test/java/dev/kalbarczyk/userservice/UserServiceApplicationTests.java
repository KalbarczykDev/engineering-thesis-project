package dev.kalbarczyk.userservice;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class UserServiceApplicationTests extends MySqlTestBase {

    @Autowired
    private WebTestClient client;

    @Test
    void getUserById() {

        var userId = 1;

        client.get()
                .uri("/users/" + userId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.userId").isEqualTo(userId);

    }

    @Test
    void getUserInvalidParameterString() {

        client.get()
                .uri("/users/no-integer")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(BAD_REQUEST)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users/no-integer");
    }

    @Test
    void getUserNotFound() {

        int userIdNotFound = 13;

        client.get()
                .uri("/users/" + userIdNotFound)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users/" + userIdNotFound)
                .jsonPath("$.message").isEqualTo("No user found for userId: " + userIdNotFound);
    }

    @Test
    void getUserInvalidParameterNegativeValue() {

        int userIdInvalid = -1;

        client.get()
                .uri("/users/" + userIdInvalid)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users/" + userIdInvalid)
                .jsonPath("$.message").isEqualTo("Invalid userId: " + userIdInvalid);
    }
}
