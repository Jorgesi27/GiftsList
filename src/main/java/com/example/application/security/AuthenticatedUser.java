package com.example.application.security;

import com.example.application.domain.Usuario;
import com.example.application.domain.UsuarioRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Component
public class AuthenticatedUser {

    private final UsuarioRepository userRepository;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(AuthenticationContext authenticationContext, UsuarioRepository userRepository) {
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
    }

    @Transactional
    public Optional<Usuario> get() {
        Optional<Usuario> authenticatedUser = authenticationContext.getAuthenticatedUser(Usuario.class)
                .flatMap(userDetails -> userRepository.findByEmail(userDetails.getUsername()));

        return authenticatedUser;
    }

    @Transactional
    public String getUsername() {
        return get().map(Usuario::getNombre).orElseThrow(() -> new IllegalStateException("No user logged in"));
    }

    public void logout() {
        authenticationContext.logout();
    }

}