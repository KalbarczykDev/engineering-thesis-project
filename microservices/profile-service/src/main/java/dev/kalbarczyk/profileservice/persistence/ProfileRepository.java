package dev.kalbarczyk.profileservice.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProfileRepository extends ReactiveCrudRepository<ProfileEntity, Long> {
    Mono<ProfileEntity> findByUserId(Long userId);
}
