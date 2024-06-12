package com.example.application.presentacion;

import com.example.application.domain.Usuario;
import com.example.application.services.UserManagementService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout {

    private final UserManagementService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginView(UserManagementService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Password");

        Button loginButton = new Button("Login", event -> {
            String email = emailField.getValue();
            String password = passwordField.getValue();

            if (authenticate(email, password)) {
                Notification.show("Login exitoso");
                getUI().ifPresent(ui -> ui.navigate("main"));  // Redirige a la vista principal después del login exitoso
            } else {
                Notification.show("Credenciales incorrectas");
            }
        });

        Button registerButton = new Button("Register", event -> {
            getUI().ifPresent(ui -> ui.navigate("userregistration")); // Redirige a la vista de registro
        });

        add(emailField, passwordField, loginButton, registerButton);
    }

    private boolean authenticate(String email, String password) {
        // Buscar al usuario por su correo electrónico en la base de datos
        Optional<Usuario> userOptional = Optional.ofNullable(userService.loadUserByUsername(email));

        // Verificar si se encontró al usuario y si la contraseña coincide
        return userOptional.isPresent() &&
                passwordEncoder.matches(password, userOptional.get().getPassword());
    }
}

