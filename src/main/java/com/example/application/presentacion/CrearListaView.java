package com.example.application.presentacion;

import com.example.application.MainLayout;
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

@Route(value = "crear-lista", layout = MainLayout.class)
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
        regalosGrid.removeAllColumns();

        regalosGrid.addColumn(Regalo::getNombre).setHeader("Nombre del Regalo");
        regalosGrid.addColumn(regalo -> {
            Allegado allegado = regalo.getAllegado();
            return allegado != null ? allegado.getNombre() + " " + allegado.getApellidos() : "";
        }).setHeader("Allegado");
        regalosGrid.addColumn(Regalo::getPrecio).setHeader("Precio");
        regalosGrid.addColumn(Regalo::getUrl).setHeader("URL");
        regalosGrid.addColumn(Regalo::getEstado).setHeader("Estado");

        regalosGrid.setItems(regalosList);
    }

    private void configureForm() {
        selectAllegado.setLabel("Selecciona un Allegado");
        refreshSelectAllegado();

        selectAllegado.setItemLabelGenerator(Allegado::getNombre);

        addRegaloButton.addClickListener(e -> addRegalo());
    }

    private void refreshSelectAllegado() {
        Optional<Usuario> optionalUsuario = authenticatedUser.get();
        if (optionalUsuario.isPresent()) {
            Usuario usuarioLogueado = optionalUsuario.get();
            List<Allegado> allegadosUsuario = allegadoService.findAllByUsuario(usuarioLogueado);
            selectAllegado.setItems(allegadosUsuario);
        } else {
            selectAllegado.setItems();
            System.err.println("No se pudo obtener el usuario logueado para refrescar el select de allegados.");
        }
    }

    private FormLayout createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(listaNombre, selectAllegado, nombreRegalo, precioRegalo, urlRegalo, addRegaloButton);
        return formLayout;
    }

    private void addRegalo() {
        if (selectAllegado.getValue() == null || !isAllegadoUsuario(selectAllegado.getValue())) {
            Notification.show("Selecciona un allegado válido");
            return;
        }

        String nombre = nombreRegalo.getValue();
        String precioStr = precioRegalo.getValue();

        if (nombre.isEmpty()) {
            Notification.show("El nombre del regalo no puede estar vacío");
            return;
        }

        Double precio = null;
        try {
            if (!precioStr.isEmpty()) {
                precio = Double.valueOf(precioStr);
            }
        } catch (NumberFormatException e) {
            Notification.show("El precio debe ser un número válido");
            return;
        }

        Regalo regalo = new Regalo();
        regalo.setNombre(nombre);
        regalo.setPrecio(precio);
        regalo.setUrl(urlRegalo.getValue());
        regalo.setEstado(EstadoRegalo.POR_COMPRAR);
        regalo.setAllegado(selectAllegado.getValue());

        regalosList.add(regalo);
        regalosGrid.setItems(regalosList);

        nombreRegalo.clear();
        precioRegalo.clear();
        urlRegalo.clear();
        selectAllegado.clear();
    }

    private boolean isAllegadoUsuario(Allegado allegado) {
        Optional<Usuario> optionalUsuario = authenticatedUser.get();
        if (optionalUsuario.isPresent()) {
            Usuario usuarioLogueado = optionalUsuario.get();
            return allegado.getUsuario().equals(usuarioLogueado);
        }
        return false;
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
        lista.setUsuario(usuario);
        lista.setEstado(EstadoLista.PENDIENTE);

        double costoTotal = regalosList.stream()
                .mapToDouble(regalo -> regalo.getPrecio() != null ? regalo.getPrecio() : 0.0)
                .sum();
        lista.setCoste(costoTotal);
        lista = listaService.save(lista);

        for (Regalo regalo : regalosList) {
            regalo.setLista(lista);
            regaloService.save(regalo);
        }

        listaNombre.clear();
        regalosList.clear();
        regalosGrid.setItems(regalosList);

        Notification.show("Lista de regalos guardada exitosamente.");
        getUI().ifPresent(ui -> ui.navigate(ListaView.class));
    }
}