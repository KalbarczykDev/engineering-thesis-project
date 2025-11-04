package dev.kalbarczyk.profileservice.persistence;

import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<ProfileEntity, Long> {
}
