package com.example.application.presentacion;

import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@Route("login")
@AnonymousAllowed
public class LoginView extends Composite<LoginOverlay> {

    private final AuthenticationManager authenticationManager;

    public LoginView(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;

        LoginOverlay loginOverlay = getContent();
        loginOverlay.setTitle("App de Listas de Regalos");
        loginOverlay.setDescription("Crea listas de regalos para tus allegados");
        loginOverlay.setOpened(true);

        loginOverlay.addLoginListener(event -> {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    event.getUsername(),
                    event.getPassword()
            );
            try {
                Authentication authenticated = authenticationManager.authenticate(authentication);
                if (authenticated.isAuthenticated()) {
                    UI.getCurrent().navigate(MainView.class);
                } else {
                    loginOverlay.setError(true);
                    loginOverlay.setDescription("Credenciales incorrectas");
                }
            } catch (BadCredentialsException e) {
                loginOverlay.setError(true);
                loginOverlay.setDescription("Credenciales incorrectas");
            }
        });
    }
}