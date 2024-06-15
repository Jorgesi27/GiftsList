package com.example.application.views.main;

import com.example.application.MainLayout;
import com.example.application.security.AuthenticatedUser;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Home")
@Route(value = "", layout = MainLayout.class)
@PermitAll
public class MainView extends VerticalLayout {

    private final AuthenticatedUser authenticatedUser;

    @Autowired
    public MainView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        String name = authenticatedUser.getUsername();
        H1 welcomeMessage = new H1("Bienvenido a Gifts List IW, " + name);
        Image image = new Image("images/96rs_whs7_230503.jpg", "Imagen de regalo");
        image.setWidth("300px");

        setAlignItems(Alignment.CENTER);
        add(welcomeMessage, image);
        setMargin(true);
    }
}
