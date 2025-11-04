package dev.kalbarczyk.userservice;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;



import dev.kalbarczyk.userservice.persistence.UserEntity;
import dev.kalbarczyk.userservice.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
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

    @Test
    void update() {
        savedEntity.setEmail("newEmail@example.com");
        repository.save(savedEntity);

        var foundEntity = repository.findById(savedEntity.getId()).orElseThrow();
        assertEquals(1, (long) foundEntity.getVersion());
        assertEquals("newEmail@example.com", foundEntity.getEmail());
    }

    @Test
    void delete() {
        repository.delete(savedEntity);
        assertFalse(repository.existsById(savedEntity.getId()));
    }


    @Test
    void getByUserId() {
        var foundEntity = repository.findById(savedEntity.getId()).orElseThrow();
        assertEqualsUser(savedEntity, foundEntity);
    }

    @Test
    void duplicateError() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            var entity = UserEntity.builder().username("username").email("email@example.com").password("password").build();
            repository.save(entity);
        });
    }

    @Test
    void optimisticLockError() {
        var entity1 = repository.findById(savedEntity.getId()).orElseThrow();
        var entity2 = repository.findById(savedEntity.getId()).orElseThrow();

        entity1.setEmail("new@example.com");
        repository.save(entity1);

        assertThrows(OptimisticLockingFailureException.class, () -> {
            entity2.setEmail("new2@example.com");
            repository.save(entity2);
        });

        var updatedEntity = repository.findById(savedEntity.getId()).orElseThrow();
        assertEquals(1, (long) updatedEntity.getVersion());
        assertEquals("new@example.com", updatedEntity.getEmail());
    }

    private void assertEqualsUser(final UserEntity expectedEntity, final UserEntity actualEntity) {
        assertEquals(expectedEntity.getId(), actualEntity.getId());
        assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
        assertEquals(expectedEntity.getUsername(), actualEntity.getUsername());
        assertEquals(expectedEntity.getEmail(), actualEntity.getEmail());
        assertEquals(expectedEntity.getPassword(), actualEntity.getPassword());
    }


}
