package com.example.application.user.unit;

import com.example.application.domain.Usuario;
import com.example.application.services.EmailService;
import com.example.application.user.ObjectMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EmailFakeServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void shouldSendRegistrationEmail() {
        // Given
        Usuario testUser = ObjectMother.createTestUser();

        // When
        boolean result = emailService.sendRegistrationEmail(testUser);

        // Then
        assertThat(result).isTrue();
    }
}
