package dev.kalbarczyk.userservice;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Flux.just;

import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.userservice.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class UserServiceApplicationTests extends MySqlTestBase {

    @Autowired
    private WebTestClient client;

    @Autowired
    private UserRepository repository;

    @Test
    void shouldGetUserById() {

        var userId = 1L;

        postAndVerifyUser(userId, HttpStatus.OK);

        assertTrue(repository.findById(userId).isPresent());

        getAndVerifyUser(userId, HttpStatus.OK).jsonPath("$.userId").isEqualTo(userId);

    }


    private WebTestClient.BodyContentSpec getAndVerifyUser(final Long userId, final HttpStatus expectedStatus) {
        return getAndVerifyUser("/" + userId, expectedStatus);
    }

    private WebTestClient.BodyContentSpec getAndVerifyUser(final String userIdPath, final HttpStatus expectedStatus) {
        return client.get()
                .uri("/users" + userIdPath)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody();
    }


    private WebTestClient.BodyContentSpec postAndVerifyUser(final Long userId, final HttpStatus expectedStatus) {
        var user = new User(null, "username" + userId, "email" + userId + "@example.com", "secret",
                LocalDateTime.now().toString(), LocalDateTime.now().toString());

        return client.post()
                .uri("/users")
                .bodyValue(user)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody();
    }

    private WebTestClient.BodyContentSpec deleteAndVerifyUser(final Long userId, final HttpStatus expectedStatus) {
        return client.delete()
                .uri("/users/" + userId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBody();
    }


}
