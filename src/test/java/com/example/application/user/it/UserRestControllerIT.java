package com.example.application.user.it;

import com.example.application.domain.Usuario;
import com.example.application.services.UserManagementService;
import com.example.application.user.ObjectMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserRestControllerIT {

    @Autowired
    private MockMvc server;

    @MockBean
    private UserManagementService userManagementService;

    @Test
    public void shouldReturnListOfUsers() {

        // Given
        // a certain user
        Usuario testUser = ObjectMother.createTestUser();

        // and the service is stubbed for the method loadActiveUsers
        given(userManagementService.loadActiveUsers()).willReturn(List.of(testUser));

        // When
        // Call the HTTP API
        String input = "/api/users";

        // When make a HTTP API Rest invocation and assertion
        try {
            server.perform(get(input).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].email", is(testUser.getUsername())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
