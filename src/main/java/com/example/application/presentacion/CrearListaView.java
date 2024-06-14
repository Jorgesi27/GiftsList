package com.example.application.presentacion;

import com.example.application.domain.*;
import com.example.application.enums.EstadoLista;
import com.example.application.enums.EstadoRegalo;
import com.example.application.security.AuthenticatedUser;
import com.example.application.services.AllegadoService;
import com.example.application.services.ListaService;
import com.example.application.services.RegaloService;
import com.example.application.services.UserManagementService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Route(value = "crear-lista")
@PageTitle("Crea Lista de Regalos")
@PermitAll
public class CrearListaView extends VerticalLayout{

    private final AllegadoService allegadoService;
    private final ListaService listaService;
    private final RegaloService regaloService;
    private final UserManagementService usuarioService;
    private final AuthenticatedUser authenticatedUser;

    private TextField listaNombre = new TextField("Nombre de la Lista");
    private Select<Allegado> selectAllegado = new Select<>();
    private TextField nombreRegalo = new TextField("Nombre del regalo");
    private TextField precioRegalo = new TextField("Precio del regalo");
    private TextField urlRegalo = new TextField("URL del regalo");
    private Button addRegaloButton = new Button("Agregar Regalo");
    private Grid<Regalo> regalosGrid = new Grid<>(Regalo.class);
    private List<Regalo> regalosList = new ArrayList<>();
    private Button saveButton = new Button("Guardar Lista");

    @Autowired
    public CrearListaView(AllegadoService allegadoService, ListaService listaService, RegaloService regaloService, UserManagementService usuarioService, AuthenticatedUser authenticatedUser) {
        this.allegadoService = allegadoService;
        this.listaService = listaService;
        this.regaloService = regaloService;
        this.usuarioService = usuarioService;
        this.authenticatedUser = authenticatedUser;

        configureRegalosGrid();
        configureForm();

        saveButton.addClickListener(e -> saveLista());

        add(new H1("Crear Lista de Regalos"), createFormLayout(), regalosGrid, saveButton);
    }

    private void configureRegalosGrid() {
        regalosGrid.addColumn(Regalo::getNombre).setHeader("Nombre del Regalo");
        regalosGrid.addColumn(Regalo::getEstado).setHeader("Estado");
        regalosGrid.addColumn(Regalo::getPrecio).setHeader("Precio");
        regalosGrid.addColumn(Regalo::getUrl).setHeader("URL");
        regalosGrid.addColumn(regalo -> regalo.getAllegado() != null ? regalo.getAllegado().getNombre() : "").setHeader("Allegado");
        regalosGrid.setItems(regalosList);
    }

    private void configureForm() {
        selectAllegado.setLabel("Selecciona un Allegado");
        selectAllegado.setItems(allegadoService.findAll());
        selectAllegado.setItemLabelGenerator(Allegado::getNombre);

        addRegaloButton.addClickListener(e -> addRegalo());
    }

    private FormLayout createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(listaNombre, selectAllegado, nombreRegalo, precioRegalo, urlRegalo, addRegaloButton);
        return formLayout;
    }

    private void addRegalo() {
        if (selectAllegado.getValue() == null) {
            Notification.show("Selecciona un allegado");
            return;
        }

        Regalo regalo = new Regalo();
        regalo.setNombre(nombreRegalo.getValue());
        regalo.setPrecio(precioRegalo.getValue().isEmpty() ? null : Double.valueOf(precioRegalo.getValue()));
        regalo.setUrl(urlRegalo.getValue());
        regalo.setEstado(EstadoRegalo.POR_COMPRAR); // Establecer el estado del regalo como POR_COMPRAR
        regalo.setAllegado(selectAllegado.getValue()); // Asignar el allegado seleccionado al regalo

        regalosList.add(regalo);
        regalosGrid.setItems(regalosList);

        nombreRegalo.clear();
        precioRegalo.clear();
        urlRegalo.clear();
        selectAllegado.clear();
    }

    private void saveLista() {
        if (listaNombre.getValue().isEmpty()) {
            Notification.show("El nombre de la lista no puede estar vacío");
            return;
        }

        Optional<Usuario> optionalUsuario = authenticatedUser.get();
        if (optionalUsuario.isEmpty()) {
            Notification.show("No se pudo obtener el usuario autenticado");
            return;
        }

        Usuario usuario = optionalUsuario.get();

        Lista lista = new Lista();
        lista.setNombre(listaNombre.getValue());
        lista.setUsuario(usuario); // Asignar el usuario autenticado a la lista
        lista.setEstado(EstadoLista.PENDIENTE); // Establecer el estado de la lista como PENDIENTE
        double costoTotal = regalosList.stream()
                .mapToDouble(regalo -> regalo.getPrecio() != null ? regalo.getPrecio() : 0.0)
                .sum();
        lista.setCoste(costoTotal);
        lista = listaService.save(lista); // Guardar la lista y obtener el objeto actualizado

        for (Regalo regalo : regalosList) {
            regalo.setLista(lista); // Asignar la lista al regalo
            regaloService.save(regalo); // Guardar el regalo
        }

        listaNombre.clear();
        regalosList.clear();
        regalosGrid.setItems(regalosList);

        Notification.show("Lista de regalos guardada exitosamente.");
    }
}