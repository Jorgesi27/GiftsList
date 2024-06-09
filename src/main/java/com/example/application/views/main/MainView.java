package com.example.application.views.main;

import com.example.application.presentacion.AllegadoPresentacion;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;

@PageTitle("Main")
@Route(value = "")
@RouteAlias(value = "")
@PermitAll
public class MainView extends HorizontalLayout {

    private Button allegadoView;

    public MainView() {

        allegadoView = new Button("Gestionar Allegados", e -> {
            getUI().ifPresent(ui -> ui.navigate(AllegadoPresentacion.class));
        });

        setMargin(true);

        add(allegadoView);
    }

}
