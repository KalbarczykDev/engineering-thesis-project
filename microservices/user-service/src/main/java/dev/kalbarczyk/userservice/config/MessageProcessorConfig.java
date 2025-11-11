package dev.kalbarczyk.userservice.config;

import dev.kalbarczyk.api.core.user.CreateUser;
import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.api.core.user.UserService;
import dev.kalbarczyk.api.event.Event;
import dev.kalbarczyk.api.exceptions.EventProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MessageProcessorConfig {
    private final UserService userService;

    @Bean
    public Consumer<Event<Long, User>> messageProcessor() {
        return event -> {
            log.info("Process message created at {}...", event.getEventCreatedAt());

            switch (event.getEventType()) {
                case CREATE -> {
                    var user = event.getData();
                    var createUser = new CreateUser(user.username(), user.email(), user.password());
                    log.info("Create user with userId: {}", user.userId());
                    userService.createUser(createUser).block();
                }
                case DELETE -> {
                    var userId = event.getKey();
                    log.info("Delete user with UserID: {}", userId);
                    userService.deleteUser(userId).block();
                }
                case UPDATE -> {
                    var user = event.getData();
                    log.info("Update user with ID: {}", user.userId());
                    userService.updateUser(user.userId(), user).block();
                }
                default -> {
                    var errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE, UPDATE or DELETE event";
                    log.warn(errorMessage);
                    throw new EventProcessingException(errorMessage);
                }

            }


        };
    }
}