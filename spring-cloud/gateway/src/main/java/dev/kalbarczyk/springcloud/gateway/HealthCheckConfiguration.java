package dev.kalbarczyk.springcloud.gateway;

import static java.util.logging.Level.FINE;

import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class HealthCheckConfiguration {
    private static final Logger log = LoggerFactory.getLogger(HealthCheckConfiguration.class);

    private final WebClient webClient;


    public HealthCheckConfiguration(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Bean
    ReactiveHealthContributor healthcheckMicroservices() {

        var registry = new LinkedHashMap<String, ReactiveHealthIndicator>();

        registry.put("user", () -> getHealth("http://user"));
        registry.put("profile", () -> getHealth("http://profile"));
        registry.put("user-composite", () -> getHealth("http://user-composite"));

        return CompositeReactiveHealthContributor.fromMap(registry);
    }

    private Mono<Health> getHealth(final String baseUrl) {
        var url = baseUrl + "/actuator/health";
        log.debug("Setting up a call to the Health API on URL: {}", url);
        return webClient.get().uri(url).retrieve().bodyToMono(String.class)
                .map(_ -> new Health.Builder().up().build())
                .onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build()))
                .log(log.getName(), FINE);
    }

}
