package com.example.application.user.it;

import com.example.application.domain.Usuario;
import com.example.application.services.UserManagementService;
import com.example.application.user.ObjectMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional(propagation = Propagation.REQUIRES_NEW) // después de cada test se hace un rollback de la base de datos
public class UserManagementServiceIT {

    Usuario testUser;
    @Autowired
    private UserManagementService userManagementService;

    @Test
    public void shouldNotActivateANoExistingUser() {

        // Given
        // a certain user (not stored on the database)
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