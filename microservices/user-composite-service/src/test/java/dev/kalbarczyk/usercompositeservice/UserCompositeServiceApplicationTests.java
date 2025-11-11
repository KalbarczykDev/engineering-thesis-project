package dev.kalbarczyk.usercompositeservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.api.core.profile.UpdateProfile;
import dev.kalbarczyk.api.core.user.CreateUser;
import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.api.exceptions.InvalidInputException;
import dev.kalbarczyk.api.exceptions.NotFoundException;
import dev.kalbarczyk.usercompositeservice.services.UserCompositeIntegration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class UserCompositeServiceApplicationTests {

    private static final Long USER_ID_OK = 1L;
    private static final Long USER_ID_NOT_FOUND = 2L;
    private static final Long USER_ID_INVALID = 3L;

    @Autowired
    private WebTestClient client;
    @MockitoBean
    private UserCompositeIntegration compositeIntegration;

    @BeforeEach
    void setUp() {
        when(compositeIntegration.getUser(USER_ID_OK))
                .thenReturn(Mono.just(new User(
                        USER_ID_OK,
                        "username",
                        "username",
                        "email",
                        "password",
                        LocalDateTime.now().toString(),
                        LocalDateTime.now().toString()
                )));

        when(compositeIntegration.getProfile(USER_ID_OK))
                .thenReturn(Mono.just(
                        new Profile(
                                USER_ID_OK,
                                "displayName",
                                "bio",
                                "location",
                                LocalDateTime.now().toString(),
                                LocalDateTime.now().toString()
                        )));

        when(compositeIntegration.getUser(USER_ID_NOT_FOUND))
                .thenThrow(new NotFoundException("NOT FOUND: " + USER_ID_NOT_FOUND));

        when(compositeIntegration.getUser(USER_ID_INVALID))
                .thenThrow(new InvalidInputException("INVALID: " + USER_ID_INVALID));

        when(compositeIntegration.updateProfile(eq(USER_ID_OK), any(UpdateProfile.class)))
                .thenReturn(Mono.just(new Profile(
                        USER_ID_OK,
                        "newDisplayName",
                        "newBio",
                        "newLocation",
                        LocalDateTime.now().toString(),
                        LocalDateTime.now().toString()
                )));

        when(compositeIntegration.createUserAndProfile(any(CreateUser.class)))
                .thenReturn(Mono.zip(
                        Mono.just(new User(1L, "username", "username", "email", "password",
                                LocalDateTime.now().toString(),
                                LocalDateTime.now().toString())),
                        Mono.just(new Profile(1L, "displayName", "bio", "location",
                                LocalDateTime.now().toString(),
                                LocalDateTime.now().toString()))
                ));
    }

    @Test
    void shouldCreateUser() {
        var user = new CreateUser("username", "email", "password");
        client.post()
                .uri("/user-composite")
                .bodyValue(user)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody();

    }

    @Test
    void shouldDeleteUser() {
        client.delete()
                .uri("/user-composite/" + USER_ID_OK)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody();
    }

    @Test
    void shouldUpdateUserProfile() {
        client.put()
                .uri("/user-composite/" + USER_ID_OK + "/profile")
                .bodyValue(new UpdateProfile("newDisplayName", "newBio", "newLocation"))
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.displayName").isEqualTo("newDisplayName")
                .jsonPath("$.bio").isEqualTo("newBio")
                .jsonPath("$.location").isEqualTo("newLocation");
    }


    @Test
    void getUserProfile() {
        client.get().uri("/user-composite/" + USER_ID_OK + "/profile")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.userId").isEqualTo(USER_ID_OK)
                .jsonPath("$.username").isEqualTo("username")
                .jsonPath("$.email").isEqualTo("email")
                .jsonPath("$.displayName").isEqualTo("displayName");
    }

    @Test
    void getUserNotFound() {
        client.get()
                .uri("/user-composite/" + USER_ID_NOT_FOUND + "/profile")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/user-composite/" + USER_ID_NOT_FOUND + "/profile")
                .jsonPath("$.message").isEqualTo("NOT FOUND: " + USER_ID_NOT_FOUND);
    }

    @Test
    void getUserInvalidInput() {
        client.get()
                .uri("/user-composite/" + USER_ID_INVALID + "/profile")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/user-composite/" + USER_ID_INVALID + "/profile")
                .jsonPath("$.message").isEqualTo("INVALID: " + USER_ID_INVALID);
    }

}
