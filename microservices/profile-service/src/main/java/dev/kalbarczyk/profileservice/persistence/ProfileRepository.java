package dev.kalbarczyk.profileservice.persistence;

import dev.kalbarczyk.api.core.profile.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}
