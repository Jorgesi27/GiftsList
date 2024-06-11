package com.example.application.security;

import com.example.application.domain.Usuario;
import com.example.application.domain.UsuarioRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.Optional;

@Component
public class AuthenticatedUser {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(AuthenticationContext authenticationContext, UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationContext = authenticationContext;
    }

    @Transactional
    public Optional<Usuario> get() {
        return authenticationContext.getAuthenticatedUser(Usuario.class)
                .map(userDetails -> usuarioRepository.findByEmail(userDetails.getEmail()).get());


    }

    public void logout() {
        authenticationContext.logout();
    }
}