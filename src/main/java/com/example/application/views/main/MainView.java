package com.example.application.views.main;

import com.example.application.presentacion.AllegadoView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.security.access.prepost.PreAuthorize;

@PageTitle("main")
@Route(value = "")
@RouteAlias(value = "")
@PreAuthorize("isAuthenticated()")
public class MainView extends HorizontalLayout {

    private Button allegadoView;

    public MainView() {

        allegadoView = new Button("Gestionar Allegados", e -> {
            getUI().ifPresent(ui -> ui.navigate(AllegadoView.class));
        });

        setMargin(true);

        add(allegadoView);
    }

}
