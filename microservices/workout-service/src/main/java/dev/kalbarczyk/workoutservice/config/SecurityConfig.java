package dev.kalbarczyk.workoutservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/openapi/**", "/swagger-ui.html", "/swagger-ui/**").permitAll() // include all Swagger endpoints
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers(GET, "/workouts/**").permitAll()
                        .pathMatchers(POST, "/workouts/**").permitAll()
                        .pathMatchers(PUT, "/workouts/**").permitAll()
                        .pathMatchers(DELETE, "/workouts/**").permitAll()
//                        .pathMatchers(POST, "/exercises/**").hasAuthority("SCOPE_user:write")
//                        .pathMatchers(PUT, "/exercises/**").hasAuthority("SCOPE_user:write")
//                        .pathMatchers(DELETE, "/exercises/**").hasAuthority("SCOPE_user:write")

                        .anyExchange().authenticated())
                .oauth2ResourceServer(server -> server.jwt(Customizer.withDefaults()));
        return http.build();
    }
}
