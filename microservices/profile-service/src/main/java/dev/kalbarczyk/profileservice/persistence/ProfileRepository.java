package dev.kalbarczyk.profileservice.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProfileRepository extends ReactiveCrudRepository<ProfileEntity, Long> {
    Mono<ProfileEntity> findByUserId(Long userId);
}
