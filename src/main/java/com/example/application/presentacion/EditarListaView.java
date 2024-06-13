package com.example.application.presentacion;

import com.example.application.MainLayout;
import com.example.application.domain.*;
import com.example.application.enums.EstadoLista;
import com.example.application.enums.EstadoRegalo;
import com.example.application.services.AllegadoService;
import com.example.application.services.ListaService;
import com.example.application.services.RegaloService;
import com.example.application.services.UserManagementService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Route(value = "editar-lista/:id", layout = MainLayout.class)
@PageTitle("Editar Lista de Regalos")
@AnonymousAllowed
public class EditarListaView extends VerticalLayout implements BeforeEnterObserver {

    private final AllegadoService allegadoService;
    private final ListaService listaService;
    private final RegaloService regaloService;
    private final UserManagementService usuarioService;

    private TextField listaNombre = new TextField("Nombre de la Lista");

    private Select<Allegado> selectAllegado = new Select<>();
    private TextField nombreRegalo = new TextField("Nombre del regalo");
    private TextField precioRegalo = new TextField("Precio del regalo");
    private TextField urlRegalo = new TextField("URL del regalo");
    private Button addRegaloButton = new Button("Agregar Regalo");

    private Grid<Regalo> regalosGrid = new Grid<>(Regalo.class);
    private List<Regalo> regalosList = new ArrayList<>();

    private Button saveButton = new Button("Guardar Cambios");

    private UUID id;
    private Lista lista;

    @Autowired
    public EditarListaView(AllegadoService allegadoService, ListaService listaService, RegaloService regaloService, UserManagementService usuarioService) {
        this.allegadoService = allegadoService;
        this.listaService = listaService;
        this.regaloService = regaloService;
        this.usuarioService = usuarioService;

        configureRegalosGrid();
        configureForm();

        saveButton.addClickListener(e -> saveLista());

        add(new H1("Editar Lista de Regalos"), createFormLayout(), regalosGrid, saveButton);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String idStr = event.getRouteParameters().get("id").orElse(null);
        if (idStr != null) {
            try {
                id = UUID.fromString(idStr);
                cargarDatosLista(id);
            } catch (IllegalArgumentException e) {
                Notification.show("ID de lista no válido.");
            }
        } else {
            Notification.show("ID de lista no proporcionado.");
        }
    }

    private void cargarDatosLista(UUID id) {
        Optional<Lista> optionalLista = listaService.findById(id);
        if (optionalLista.isPresent()) {
            lista = optionalLista.get();
            listaNombre.setValue(lista.getNombre());
            regalosList = regaloService.findByListaId(id);
            regalosGrid.setItems(regalosList);
        } else {
            Notification.show("Lista no encontrada.");
        }
    }

    private void configureRegalosGrid() {
        Binder<Regalo> binder = new Binder<>(Regalo.class);
        Editor<Regalo> editor = regalosGrid.getEditor();
        editor.setBinder(binder);

        Grid.Column<Regalo> nombreColumn = regalosGrid.addColumn(Regalo::getNombre).setHeader("Nombre del Regalo");
        Grid.Column<Regalo> estadoColumn = regalosGrid.addColumn(Regalo::getEstado).setHeader("Estado");
        Grid.Column<Regalo> precioColumn = regalosGrid.addColumn(Regalo::getPrecio).setHeader("Precio");
        Grid.Column<Regalo> urlColumn = regalosGrid.addColumn(Regalo::getUrl).setHeader("URL");
        Grid.Column<Regalo> allegadoColumn = regalosGrid.addColumn(regalo -> regalo.getAllegado().getNombre()).setHeader("Allegado");

        TextField nombreField = new TextField();
        binder.forField(nombreField).bind(Regalo::getNombre, Regalo::setNombre);
        nombreColumn.setEditorComponent(nombreField);

        Select<EstadoRegalo> estadoField = new Select<>();
        binder.forField(estadoField).bind(Regalo::getEstado, Regalo::setEstado);
        estadoColumn.setEditorComponent(estadoField);

        TextField precioField = new TextField();
        binder.forField(precioField).bind(regalo -> regalo.getPrecio() != null ? regalo.getPrecio().toString() : "", (regalo, precio) -> regalo.setPrecio(precio.isEmpty() ? null : Double.valueOf(precio)));
        precioColumn.setEditorComponent(precioField);

        TextField urlField = new TextField();
        binder.forField(urlField).bind(Regalo::getUrl, Regalo::setUrl);
        urlColumn.setEditorComponent(urlField);

        Select<Allegado> allegadoField = new Select<>();
        allegadoField.setItems(allegadoService.findAll());
        allegadoField.setItemLabelGenerator(Allegado::getNombre);
        binder.forField(allegadoField).bind(Regalo::getAllegado, Regalo::setAllegado);
        allegadoColumn.setEditorComponent(allegadoField);

        regalosGrid.getEditor().setBuffered(true);

        regalosGrid.getEditor().addCloseListener(event -> regalosGrid.getDataProvider().refreshItem(event.getItem()));
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
        regalo.setAllegado(selectAllegado.getValue());
        regalo.setEstado(EstadoRegalo.POR_COMPRAR); // Establecer el estado del regalo como POR_COMPRAR

        regalosList.add(regalo);
        regalosGrid.setItems(regalosList);

        nombreRegalo.clear();
        precioRegalo.clear();
        urlRegalo.clear();
        selectAllegado.clear();
    }

    private void saveLista() {
        if (lista == null) {
            Notification.show("No se ha cargado ninguna lista para editar.");
            return;
        }

        // Verificar que el nombre de la lista no esté vacío
        if (listaNombre.getValue().isEmpty()) {
            Notification.show("El nombre de la lista no puede estar vacío");
            return;
        }

        lista.setNombre(listaNombre.getValue());
        lista.setUsuario(obtenerUsuarioAutenticado());
        lista.setEstado(EstadoLista.PENDIENTE); // Establecer el estado de la lista como PENDIENTE

        // Calcular el costo total de los regalos
        double costoTotal = regalosList.stream()
                .mapToDouble(regalo -> regalo.getPrecio() != null ? regalo.getPrecio() : 0.0)
                .sum();
        lista.setCoste(costoTotal);

        // Guardar la lista actualizada
        lista = listaService.save(lista);

        // Asignar la lista a cada regalo y guardarlos
        for (Regalo regalo : regalosList) {
            regalo.setLista(lista);
            regaloService.save(regalo);
        }

        Notification.show("Lista de regalos actualizada exitosamente.");
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String email = ((UserDetails) principal).getUsername();
                return usuarioService.loadUserByUsername(email);
            }
        }
        return null;
    }
}