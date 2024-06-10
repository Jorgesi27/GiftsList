package com.example.application.presentacion;

import com.example.application.data.Allegado;
import com.example.application.negocio.AllegadoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "allegados")
@PageTitle("Gestionar Allegados")
@AnonymousAllowed
public class AllegadoView extends VerticalLayout {

    private AllegadoService allegadoService;
    private Grid<Allegado> grid = new Grid<>(Allegado.class);
    private TextField nombre = new TextField("Nombre");
    private TextField apellidos = new TextField("Apellidos");
    private Button saveButton = new Button("Guardar");
    private Button deleteButton = new Button("Borrar");

    @Autowired
    public AllegadoView(AllegadoService allegadoService){

        this.allegadoService = allegadoService;

        grid.setColumns("nombre", "apellidos");
        grid.setItems(allegadoService.findAll());

        FormLayout formLayout = new FormLayout();
        formLayout.add(nombre, apellidos, saveButton, deleteButton);

        saveButton.addClickListener(e -> saveAllegado());
        deleteButton.addClickListener(e -> deleteAllegado());

        add(new Span("Gestionar Allegados"), grid, formLayout);

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                populateForm(event.getValue());
            }
        });
    }

    private void saveAllegado(){
        Allegado allegado = new Allegado();
        allegado.setNombre(nombre.getValue());
        allegado.setApellidos(apellidos.getValue());
        allegadoService.save(allegado);
        refreshGrid();
        cleanForm();
    }

    private void deleteAllegado(){
        Allegado selected = grid.asSingleSelect().getValue();
        if (selected != null){
            allegadoService.deleteById(selected.getId());
            refreshGrid();
            cleanForm();
        }
    }

    private void refreshGrid(){ grid.setItems(allegadoService.findAll()); }

    private void cleanForm(){
        nombre.clear();
        apellidos.clear();
    }

    private void populateForm(Allegado allegado){
        nombre.setValue(allegado.getNombre());
        apellidos.setValue(allegado.getApellidos());
    }
}