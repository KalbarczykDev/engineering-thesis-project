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

    @Test
    void createUser() {
        var userBody = """
                {
                    "name": "John Doe",
                    "email": "john.doe@example.com"
                }
                """;

        client.post()
                .uri("/users")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(userBody)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.userId").exists()
                .jsonPath("$.name").isEqualTo("John Doe")
                .jsonPath("$.email").isEqualTo("john.doe@example.com");
    }

    @Test
    void createUserInvalidBody() {
        var invalidUserBody = """
                {
                    "name": "",
                    "email": "invalid-email"
                }
                """;

        client.post()
                .uri("/users")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(invalidUserBody)
                .exchange()
                .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users");
    }

    @Test
    void updateUser() {
        var userId = 1;
        var updateBody = """
                {
                    "name": "Jane Doe Updated",
                    "email": "jane.updated@example.com"
                }
                """;

        client.put()
                .uri("/users/" + userId)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(updateBody)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.userId").isEqualTo(userId)
                .jsonPath("$.name").isEqualTo("Jane Doe Updated")
                .jsonPath("$.email").isEqualTo("jane.updated@example.com");
    }

    @Test
    void updateUserNotFound() {
        int userIdNotFound = 999;
        var updateBody = """
                {
                    "name": "Non Existent User",
                    "email": "nonexistent@example.com"
                }
                """;

        client.put()
                .uri("/users/" + userIdNotFound)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(updateBody)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users/" + userIdNotFound)
                .jsonPath("$.message").isEqualTo("No user found for userId: " + userIdNotFound);
    }

    @Test
    void updateUserInvalidParameterNegativeValue() {
        int userIdInvalid = -1;
        var updateBody = """
                {
                    "name": "Test User",
                    "email": "test@example.com"
                }
                """;

        client.put()
                .uri("/users/" + userIdInvalid)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(updateBody)
                .exchange()
                .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users/" + userIdInvalid)
                .jsonPath("$.message").isEqualTo("Invalid userId: " + userIdInvalid);
    }

    @Test
    void deleteUser() {
        var userId = 1L;

        client.delete()
                .uri("/users?userId=" + userId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void deleteUserNotFound() {
        long userIdNotFound = 999L;

        client.delete()
                .uri("/users?userId=" + userIdNotFound)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users")
                .jsonPath("$.message").isEqualTo("No user found for userId: " + userIdNotFound);
    }

    @Test
    void deleteUserInvalidParameterNegativeValue() {
        long userIdInvalid = -1L;

        client.delete()
                .uri("/users?userId=" + userIdInvalid)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users")
                .jsonPath("$.message").isEqualTo("Invalid userId: " + userIdInvalid);
    }
}
