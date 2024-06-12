package com.example.application.security;

import com.example.application.domain.Usuario;
import com.example.application.domain.UsuarioRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Component
public class AuthenticatedUser {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationContext authenticationContext;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticatedUser.class);

    public AuthenticatedUser(AuthenticationContext authenticationContext, UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationContext = authenticationContext;
    }

    @Transactional
    public Optional<Usuario> get() {
        Optional<Usuario> authenticatedUser = authenticationContext.getAuthenticatedUser(Usuario.class);
        LOGGER.info("Authenticated User: {}", authenticatedUser.orElse(null));
        return authenticatedUser.map(userDetails -> usuarioRepository.findByEmail(userDetails.getEmail()).get());

    }

    public void logout() {
        authenticationContext.logout();
    }
}