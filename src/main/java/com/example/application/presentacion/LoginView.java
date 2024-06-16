package com.example.application.presentacion;

import com.example.application.security.AuthenticatedUser;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@PageTitle("Iniciar Sesión")
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;

    public LoginView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        setAction("/login");

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Gifts List IW");
        i18n.getHeader().setDescription("Inicie sesión usando email y contraseña.");

        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setUsername("Email");
        i18nForm.setForgotPassword("¿No tienes cuenta todavía?");
        i18n.setForm(i18nForm);

        setI18n(i18n);
        setForgotPasswordButtonVisible(true);
        setOpened(true);

        addForgotPasswordListener(event -> {
            getUI().ifPresent(ui -> ui.navigate("userregistration"));
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            // Already logged in
            setOpened(false);
            event.forwardTo("");
        }

        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
}
