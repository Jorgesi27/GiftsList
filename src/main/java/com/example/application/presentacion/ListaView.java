package com.example.application.presentacion;

import com.example.application.data.Lista;
import com.example.application.negocio.ListaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "listas-regalos")
@PageTitle("Listas de Regalos")
@AnonymousAllowed
public class ListaView extends VerticalLayout {

    private final ListaService listaService;
    private final Grid<Lista> grid = new Grid<>(Lista.class);
    private final Button createButton = new Button("Crear Lista de Regalos");

    @Autowired
    public ListaView(ListaService listaService) {
        this.listaService = listaService;

        grid.setColumns("nombre", "costeTotal", "estado");
        grid.setItems(listaService.findAll());

        createButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("crear-lista")));

        add(new Span("Listas de Regalos"), grid, createButton);
    }
}
