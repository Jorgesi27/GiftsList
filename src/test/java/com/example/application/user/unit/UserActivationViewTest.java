package com.example.application.user.unit;

import com.example.application.domain.Usuario;
import com.example.application.presentacion.ActivationView;
import com.example.application.services.UserManagementService;
import com.example.application.user.ObjectMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

@SpringBootTest
public class UserActivationViewTest {

    @Autowired
    private ActivationView userView;

    @MockBean
    private UserManagementService userManagementService;

    @Test
    public void shouldShowFailureMessageWhenUserIsNotActivated() {

        // Given
        // a certain user
        Usuario testUser = ObjectMother.createTestUser();

        // and the service is stubbed for the activateUser method
        given(userManagementService.activateUser(anyString(), anyString())).willReturn(false);

        // When
        // Set form values
        userView.setEmail(testUser.getEmail());
        userView.setSecretCode(testUser.getRegisterCode());

        // and invoking the method onActivateButtonClick
        userView.onActivateButtonClick();

        // Then
        verify(userManagementService, times(1)).activateUser(anyString(), anyString());
        // and
        assertThat(userView.getStatus().equals("Ups. La cuenta no pudo ser activada")).isFalse();
    }
/*
    @Test
    public void shouldShowSuccessMessageWhenUserIsActivated() {

        // Given
        // a certain user
        Usuario testUser = ObjectMother.createTestUser();

        // and the service is stubbed for the activateUser method
        given(userManagementService.activateUser(anyString(), anyString())).willReturn(true);

        // When
        // Set form values
        userView.setEmail(testUser.getEmail());
        userView.setSecretCode(testUser.getRegisterCode());

        // and invoking the method onActivateButtonClick
        userView.onActivateButtonClick();

        // Then
        verify(userManagementService, times(1)).activateUser(anyString(), anyString());
        // and
        assertThat(userView.getStatus().equals("Genial! La cuenta fue activada")).isTrue();
    }*/
}
