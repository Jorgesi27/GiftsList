package com.example.application.presentacion;

import com.example.application.domain.Lista;
import com.example.application.services.ListaService;
import com.vaadin.flow.component.AttachEvent;
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
    private final Button editButton = new Button("Editar Lista");
    //private final Button deleteButton = new Button("Borrar Lista");

    @Autowired
    public ListaView(ListaService listaService) {
        this.listaService = listaService;

        grid.setColumns("nombre", "coste", "estado");
        refreshGridData();

        createButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("crear-lista")));
        editButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("editar-lista")));
        //deleteButton.addClickListener(e -> deleteLista());

        add(new Span("Listas de Regalos"), grid, createButton, editButton/*, deleteButton*/);
    }

    /*private void deleteLista() {
        Lista selectedLista = grid.asSingleSelect().getValue();
        if (selectedLista != null) {
            listaService.deleteListaAndRegalos(selectedLista.getId());
            refreshGridData();
        }
    }*/

    private void refreshGridData() {
        grid.setItems(listaService.findAll());
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        refreshGridData();
    }
}
