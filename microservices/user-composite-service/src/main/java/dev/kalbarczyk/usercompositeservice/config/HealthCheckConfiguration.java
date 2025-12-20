package dev.kalbarczyk.usercompositeservice.config;

import dev.kalbarczyk.usercompositeservice.services.UserCompositeIntegration;
import org.springframework.boot.actuate.health.CompositeReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
public class HealthCheckConfiguration {
    private final UserCompositeIntegration userCompositeIntegration;

    public HealthCheckConfiguration(UserCompositeIntegration userCompositeIntegration) {
        this.userCompositeIntegration = userCompositeIntegration;
    }

    @Bean
    ReactiveHealthContributor coreServices() {

        final var registry = new LinkedHashMap<String, ReactiveHealthContributor>();
        registry.put("user", (ReactiveHealthIndicator) userCompositeIntegration::getUserHealth);
        registry.put("profile", (ReactiveHealthIndicator) userCompositeIntegration::getProfileHealth);

        return CompositeReactiveHealthContributor.fromMap(registry);
    }
}
