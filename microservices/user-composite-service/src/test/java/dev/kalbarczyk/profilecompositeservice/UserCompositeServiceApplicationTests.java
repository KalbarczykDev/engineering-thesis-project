package dev.kalbarczyk.profilecompositeservice;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.profilecompositeservice.services.UserCompositeIntegration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class UserCompositeServiceApplicationTests {

    private static final int USER_ID_OK = 1;
    private static final int USER_ID_NOT_FOUND = 2;
    private static final int USER_ID_INVALID = 3;

    @Autowired
    private WebTestClient client;
    @MockitoBean
    private UserCompositeIntegration compositeIntegration;

    @BeforeEach
    void setUp() {
        when(compositeIntegration.getUser(USER_ID_OK))
                .thenReturn(
                        new User(
                                USER_ID_OK,
                                "username",
                                "email",
                                "password",
                                LocalDateTime.now().toString(),
                                LocalDateTime.now().toString()
                        ));

        when(compositeIntegration.getProfile(USER_ID_OK))
                .thenReturn(
                        new Profile(
                                USER_ID_OK,
                                "displayName",
                                "https://via.placeholder.com/400x400",
                                "bio",
                                "location",
                                LocalDateTime.now().toString(),
                                LocalDateTime.now().toString()
                        ));

        when(compositeIntegration.getUser(USER_ID_NOT_FOUND))
                .thenThrow(new RuntimeException("NOT FOUND: " + USER_ID_NOT_FOUND));

        when(compositeIntegration.getUser(USER_ID_INVALID))
                .thenThrow(new RuntimeException("INVALID: " + USER_ID_INVALID));
    }

    @Test
    void getUserById() {
        client.get().uri("/user-composite/" + USER_ID_OK)
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

}
