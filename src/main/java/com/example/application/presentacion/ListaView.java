package com.example.application.presentacion;

import com.example.application.MainLayout;
import com.example.application.domain.Lista;
import com.example.application.security.AuthenticatedUser;
import com.example.application.services.ListaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "listas-regalos", layout = MainLayout.class)
@PageTitle("Listas de Regalos")
@PermitAll
public class ListaView extends VerticalLayout {

    private final ListaService listaService;
    private final AuthenticatedUser authenticatedUser;

    private final Grid<Lista> grid = new Grid<>(Lista.class);
    private final Button createButton = new Button("Crear Lista de Regalos");
    private final Button editButton = new Button("Editar Lista");
    private final Button deleteButton = new Button("Eliminar Lista");

    @Autowired
    public ListaView(ListaService listaService, AuthenticatedUser authenticatedUser) {
        this.listaService = listaService;
        this.authenticatedUser = authenticatedUser;

        grid.setColumns("nombre", "coste", "estado");
        grid.asSingleSelect().addValueChangeListener(event -> updateButtonsEnabledState());
        refreshGridData();

        createButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("crear-lista")));
        editButton.addClickListener(e -> {
            Lista selectedLista = grid.asSingleSelect().getValue();
            if (selectedLista != null) {
                getUI().ifPresent(ui -> ui.navigate("editar-lista/" + selectedLista.getId()));
            } else {
                Notification.show("Seleccione una lista para editar.");
            }
        });

        deleteButton.setEnabled(false);
        deleteButton.addClickListener(e -> {
            Lista selectedLista = grid.asSingleSelect().getValue();
            if (selectedLista != null) {
                listaService.deleteListaAndRegalos(selectedLista.getId());
                refreshGridData();
                Notification.show("Lista eliminada correctamente.");
            } else {
                Notification.show("Seleccione una lista para eliminar.");
            }
        });

        add(new Span("Listas de Regalos"), grid, createButton, editButton, deleteButton);
    }

    private void refreshGridData() {
        authenticatedUser.get().ifPresent(usuario -> {
            List<Lista> listas = listaService.findAllByUsuario(usuario);
            grid.setItems(listas);
        });
    }

    private void updateButtonsEnabledState() {
        Lista selectedLista = grid.asSingleSelect().getValue();
        deleteButton.setEnabled(selectedLista != null);
    }
}
