package dev.kalbarczyk.profileservice.persistence;

import dev.kalbarczyk.api.core.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
