package com.example.application.presentacion;

import com.example.application.domain.Usuario;
import com.example.application.services.UserManagementService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.io.Serial;

@PageTitle("Registrate User")
@Route(value = "userregistration")
@AnonymousAllowed
public class RegistrationView extends VerticalLayout {

    @Serial
    private static final long serialVersionUID = 123456789L;


    private final UserManagementService service;

    private final H1 title;

    private final EmailField email;
    private final PasswordField password;
    private final PasswordField password2;


    private final Button register;
    private final H4 status;

    private final BeanValidationBinder<Usuario> binder;

    public RegistrationView(UserManagementService service) {
        this.service = service;

        title = new H1("Registro de Usuario");

        email = new EmailField("Email");
        email.setId("email");

        password = new PasswordField("Contraseña");
        password.setId("password");

        password2 = new PasswordField("Repita su contraseña");
        password2.setId("password2");

        register = new Button("Registro");
        register.setId("register");

        status = new H4();
        status.setId("status");
        status.setVisible(false);

        setMargin(true);

        add(title,email, password, password2, register, status);

        register.addClickListener(e -> onRegisterButtonClick());

        binder = new BeanValidationBinder<>(Usuario.class);
        binder.bindInstanceFields(this);

        binder.setBean(new Usuario());
    }

    /**
     * Handler
     */
    public void onRegisterButtonClick() {
        if (binder.validate().isOk() && password.getValue().equals(password2.getValue())) {
            Notification.show("Botoncito.");
            if (service.registerUser(binder.getBean())) {
                Notification.show("Boton.");
                status.setText("Genial!, Por favor revise su email.");
                status.setVisible(true);
                binder.setBean(new Usuario());
                password2.setValue("");
            } else {
                Notification.show("El email ya está en uso.");

            }


        } else {
            Notification.show("Por favor, Rellene los campos.");
        }

    }
}
