package dev.kalbarczyk.userservice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import java.time.LocalDateTime;
import java.util.List;

import dev.kalbarczyk.userservice.persistence.UserEntity;
import dev.kalbarczyk.userservice.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;


@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
public class PersistenceTests extends MySqlTestBase {

    @Autowired
    private UserRepository repository;

    private UserEntity savedEntity;


    @BeforeEach
    void setupDb() {
        repository.deleteAll();

        var entity = UserEntity.builder()
                .username("username")
                .email("email@example.com")
                .password("secret")
                .build();

        savedEntity = repository.save(entity);

        assertEqualsUser(entity, savedEntity);
    }


    @Test
    void create() {
        var newEntity = UserEntity.builder()
                .username("newuser")
                .email("email")
                .password("newsecret")
                .build();

        repository.save(newEntity);

        var foundEntity = repository.findById(newEntity.getId()).orElseThrow();

        assertEqualsUser(newEntity, foundEntity);
        assertEquals(2, repository.count());

    }

    private void assertEqualsUser(final UserEntity expectedEntity, final UserEntity actualEntity) {
        assertEquals(expectedEntity.getId(), actualEntity.getId());
        assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
        assertEquals(expectedEntity.getUsername(), actualEntity.getUsername());
        assertEquals(expectedEntity.getEmail(), actualEntity.getEmail());
        assertEquals(expectedEntity.getPassword(), actualEntity.getPassword());
    }


}
