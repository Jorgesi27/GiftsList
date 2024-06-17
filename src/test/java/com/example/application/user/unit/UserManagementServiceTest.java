package com.example.application.user.unit;

import com.example.application.domain.Usuario;
import com.example.application.domain.UsuarioRepository;
import com.example.application.services.UserManagementService;
import com.example.application.user.ObjectMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class UserManagementServiceTest {

    @Autowired
    private UserManagementService userManagementService;

    @MockBean
    private UsuarioRepository userRepository;

    @Test
    public void shouldNotActivateANoExistingUser() {

        // Given a certain user (not stored on the database)
        Usuario testUser = ObjectMother.createTestUser();

        // When invoking the method ActivateUser
        boolean result = userManagementService.activateUser(testUser.getEmail(), testUser.getRegisterCode());

        // Then the result method is false
        assertThat(result).isFalse();

        // When invoking the method FindActiveUsers
        List<Usuario> returnedUsers = userManagementService.loadActiveUsers();

        // Then the result does not include the user
        assertThat(returnedUsers.contains(testUser)).isFalse();
    }


    @Test
    public void shouldActivateAnExistingUser() {

        // Given
        // a certain user
        Usuario testUser = ObjectMother.createTestUser();

        // the repo methods are stubbed
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(testUser));
        given(userRepository.findByActiveTrue()).willReturn(List.of(testUser));


        // who is registered
        userManagementService.registerUser(testUser);

        // When invoking the method ActivateUser
        boolean result = userManagementService.activateUser(testUser.getEmail(), testUser.getRegisterCode());

        // Then the result method is true
        assertThat(result).isTrue();

        // When invoking the method FindActive
        List<Usuario> returnedUsers = userManagementService.loadActiveUsers();

        // Then the result includes the user
        assertThat(returnedUsers.contains(testUser)).isTrue();

    }
}
