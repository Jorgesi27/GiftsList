package com.example.application.presentacion;

import com.example.application.services.UserManagementService;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serial;

@PageTitle("Activación de la Cuenta")
@Route(value = "useractivation")
@Component
@Scope("prototype")
@AnonymousAllowed
public class ActivationView extends VerticalLayout {

    @Serial
    private static final long serialVersionUID = 123456789L;

    private final UserManagementService service;
    private final H1 title;
    private final TextField email;
    private final TextField secretCode;
    private final Button activate;
    private final H4 status;

    public ActivationView(UserManagementService service) {
        this.service = service;

        title = new H1("Activa tu cuenta");
        email = new TextField("Tu email");
        email.setId("email");

        secretCode = new TextField("Tu código secreto");
        secretCode.setId("secretCode");

        status = new H4();
        status.setId("status");

        activate = new Button("Activar");
        activate.setId("activate");

        status.setVisible(false);

        setMargin(true);

        add(title, new HorizontalLayout(email, secretCode), activate, status);

        activate.addClickListener(e -> onActivateButtonClick());
    }

    /**
     * Handler
     */
    public void onActivateButtonClick() {

        status.setVisible(true);

        if (service.activateUser(email.getValue(), secretCode.getValue())) {
            status.setText("Enhorabuena! Su cuenta fue activada, esperamos que disfrute de nuestra web.");
            add(new RouterLink("Iniciar Sesión", MainView.class));


        } else {
            status.setText("Ups. La cuenta no pudo ser activada.");
        }
    }

    public void setEmail(String email) {
        this.email.setValue(email);

    }

    public void setSecretCode(String secretCode) {
        this.secretCode.setValue(secretCode);
    }

    public String getStatus() {
        return status.getText();
    }
}