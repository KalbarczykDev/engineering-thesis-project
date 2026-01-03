package dev.kalbarczyk.exerciseservice.config;

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
                        .pathMatchers(GET, "/exercises/**").permitAll()
                        .pathMatchers(POST, "/exercises/**").permitAll()
                        // .pathMatchers(POST, "/exercises/**").hasAuthority("SCOPE_exercise:write")
                        .pathMatchers(PUT, "/exercises/**").hasAuthority("SCOPE_exercise:write")
                        .pathMatchers(DELETE, "/exercises/**").hasAuthority("SCOPE_exercise:write")

                        .anyExchange().authenticated())
                .oauth2ResourceServer(server -> server.jwt(Customizer.withDefaults()));
        return http.build();
    }
}
