package dev.kalbarczyk.usercompositeservice.config;

import static org.springframework.http.HttpMethod.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

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
                        .pathMatchers(POST, "/user-composite/**").permitAll()
                        .pathMatchers(GET, "/user-composite/**").permitAll()
                        .pathMatchers(PUT, "/user-composite/**").hasAuthority("SCOPE_user:write")
                        .pathMatchers(DELETE, "/user-composite/**").hasAuthority("SCOPE_user:write")

                        .anyExchange().authenticated())
                .oauth2ResourceServer(server -> server.jwt(Customizer.withDefaults()));
        return http.build();
    }
}
