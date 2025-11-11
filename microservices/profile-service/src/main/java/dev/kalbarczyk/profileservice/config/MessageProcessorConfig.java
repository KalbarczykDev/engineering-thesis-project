package dev.kalbarczyk.profileservice.config;

import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.api.core.profile.ProfileService;
import dev.kalbarczyk.api.event.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MessageProcessorConfig {

    private final ProfileService profileService;


    @Bean
    public Consumer<Event<Long, Profile>> messageProcessor(){
       return event -> {

           log.info("Process message created at {}...",event.getEventCreatedAt());



       };
    }


}
