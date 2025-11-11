package dev.kalbarczyk.profilecompositeservice.config;

import dev.kalbarczyk.profilecompositeservice.services.UserCompositeIntegration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.CompositeReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
@RequiredArgsConstructor
public class HealthCheckConfiguration {
    private final UserCompositeIntegration userCompositeIntegration;

    @Bean
    ReactiveHealthContributor coreServices() {

        final var registry = new LinkedHashMap<String, ReactiveHealthContributor>();
        registry.put("user", (ReactiveHealthIndicator) userCompositeIntegration::getUserHealth);
        registry.put("profile", (ReactiveHealthIndicator) userCompositeIntegration::getProfileHealth);

        return CompositeReactiveHealthContributor.fromMap(registry);
    }
}
