package com.mireiagonzalez.urbanito.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmailReturnsUserWhenEmailExists() {
        // Arrange
        User user = new User();
        user.setName("John");
        user.setEmail("john@test.com");
        user.setPasswordHash("a-very-strong-hashed-password");
        user.setRole(UserRole.CITIZEN);

        userRepository.save(user);

        // Act
        Optional<User> result = userRepository.findByEmail("john@test.com");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john@test.com");
        assertThat(result.get().getName()).isEqualTo("John");
    }

    @Test
    void existsByEmailReturnsTrueWhenEmailExists() {
        // Arrange
        User user = new User();
        user.setName("Admin User");
        user.setEmail("admin@test.com");
        user.setPasswordHash("strong-hashed-password");
        user.setRole(UserRole.ADMIN);

        userRepository.save(user);

        // Act
        boolean exists = userRepository.existsByEmail("admin@test.com");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmailReturnsFalseWhenEmailDoesNotExist() {
        // Act
        boolean exists = userRepository.existsByEmail("missing@test.com");

        // Assert
        assertThat(exists).isFalse();
    }
}