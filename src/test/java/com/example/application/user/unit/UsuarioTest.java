package com.example.application.user.unit;

import com.example.application.domain.Usuario;
import com.example.application.user.ObjectMother;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UsuarioTest {

    @Test
    public void shouldProvideUsername() {

        // Given
        // a certain user (not stored on the database)
        Usuario testUser = ObjectMother.createTestUser("paco@example.com");

        // When
        // I invoke getUsername method
        String username = testUser.getUsername();

        // Then the result is equals to the provided username
        assertThat(username.equals("paco@example.com")).isTrue();

    }

}
