package com.example.application.user.unit;

import com.example.application.domain.Usuario;
import com.example.application.domain.UsuarioRepository;
import com.example.application.user.ObjectMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository userRepository;
/*
    @Test
    public void shouldNotFindANotExistingUser() {

        // Given
        // a random user Id
        UUID userId = UUID.randomUUID();

        // When invoking the method
        Optional<Usuario> foundUser = userRepository.findById(userId);

        // Then
        assertThat(foundUser.isPresent()).isFalse();
    }
*/
    @Test
    public void shouldFindAnExistingUser() {

        // Given
        // a certain user
        Usuario testUser = ObjectMother.createTestUser();
        // stored in the repository
        userRepository.save(testUser);

        // When invoking the method findById
        Optional<Usuario> foundUser = userRepository.findById(testUser.getId());

        // Then
        assertThat(foundUser.get()).isEqualTo(testUser);

    }
}