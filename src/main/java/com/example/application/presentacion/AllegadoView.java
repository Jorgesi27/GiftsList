package com.example.application.presentacion;

import com.example.application.domain.Allegado;
import com.example.application.domain.Usuario;
import com.example.application.security.AuthenticatedUser;
import com.example.application.services.AllegadoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Route(value = "allegados")
@PageTitle("Gestionar Allegados")
@PermitAll
public class AllegadoView extends VerticalLayout {

    private final AllegadoService allegadoService;
    private final AuthenticatedUser authenticatedUser;

    private Grid<Allegado> grid = new Grid<>(Allegado.class);
    private TextField nombre = new TextField("Nombre");
    private TextField apellidos = new TextField("Apellidos");
    private Button saveButton = new Button("Guardar");
    private Button deleteButton = new Button("Borrar");

    @Autowired
    public AllegadoView(AllegadoService allegadoService, AuthenticatedUser authenticatedUser) {
        this.allegadoService = allegadoService;
        this.authenticatedUser = authenticatedUser;

        grid.setColumns("nombre", "apellidos");
        refreshGrid();

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

    private void saveAllegado() {
        Optional<Usuario> optionalUsuario = authenticatedUser.get();
        if (optionalUsuario.isPresent()) {
            Usuario usuarioLogueado = optionalUsuario.get();

            // Crear una nueva instancia de Allegado y establecer los valores desde los campos de texto
            Allegado allegado = new Allegado();
            allegado.setNombre(nombre.getValue());
            allegado.setApellidos(apellidos.getValue());
            allegado.setUsuario(usuarioLogueado);

            // Guardar el nuevo allegado utilizando el servicio
            allegadoService.save(allegado);

            // Actualizar y limpiar la interfaz gráfica
            refreshGrid();
            cleanForm();
        } else {
            System.err.println("No se pudo obtener el usuario logueado para guardar el allegado.");
        }
    }

    private void deleteAllegado() {
        Allegado selected = grid.asSingleSelect().getValue();
        if (selected != null) {
            allegadoService.deleteById(selected.getId());
            refreshGrid();
            cleanForm();
        }
    }

    private void refreshGrid() {
        Optional<Usuario> optionalUsuario = authenticatedUser.get();
        if (optionalUsuario.isPresent()) {
            Usuario usuarioLogueado = optionalUsuario.get();
            grid.setItems(allegadoService.findAllByUsuario(usuarioLogueado));
        } else {
            System.err.println("No se pudo obtener el usuario logueado para refrescar el grid.");
        }
    }

    private void cleanForm() {
        nombre.clear();
        apellidos.clear();
    }

    private void populateForm(Allegado allegado) {
        nombre.setValue(allegado.getNombre());
        apellidos.setValue(allegado.getApellidos());
    }
}