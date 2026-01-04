package dev.kalbarczyk.profileservice.config;

import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.api.core.profile.ProfileService;
import dev.kalbarczyk.api.core.profile.UpdateProfile;
import dev.kalbarczyk.api.event.Event;
import dev.kalbarczyk.api.exceptions.EventProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;


@Configuration
public class MessageProcessorConfig {

    private static final Logger log = LoggerFactory.getLogger(MessageProcessorConfig.class);

    private final ProfileService profileService;

    public MessageProcessorConfig(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Bean
    public Consumer<Event<Long, Profile>> messageProcessor() {
        return event -> {

            log.info("Process message created at {}...", event.getEventCreatedAt());

            switch (event.getEventType()) {
                case CREATE -> {
                    var profile = event.getData();
                    log.info("Create profile with ID: {}", profile.userId());
                    profileService.createProfile(profile).block();
                }
                case DELETE -> {
                    var userId = event.getKey();
                    log.info("Delete profile with UserID: {}", userId);
                    profileService.deleteProfile(userId)
                            .doOnError(e -> log.error("Failed to delete profile: {}", e.getMessage()))
                            .onErrorResume(_ -> Mono.empty())
                            .subscribe();
                }
                case UPDATE -> {
                    var profile = event.getData();
                    var updateProfile = new UpdateProfile(profile.displayName(), profile.bio(), profile.location());
                    log.info("Update profile with ID: {}", profile.userId());
                    profileService.updateProfile(profile.userId(), updateProfile).block();
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
