package com.example.application.presentacion;

import com.example.application.MainLayout;
import com.example.application.domain.Allegado;
import com.example.application.domain.Lista;
import com.example.application.domain.Regalo;
import com.example.application.domain.Usuario;
import com.example.application.enums.EstadoLista;
import com.example.application.enums.EstadoRegalo;
import com.example.application.security.AuthenticatedUser;
import com.example.application.services.AllegadoService;
import com.example.application.services.ListaService;
import com.example.application.services.RegaloService;
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
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Route(value = "editar-lista/:id", layout = MainLayout.class)
@PageTitle("Editar Lista de Regalos")
@PermitAll
public class EditarListaView extends VerticalLayout implements BeforeEnterObserver {

    private final AllegadoService allegadoService;
    private final ListaService listaService;
    private final RegaloService regaloService;
    private final AuthenticatedUser authenticatedUser;

    private TextField listaNombre = new TextField("Nombre de la Lista");

    private Select<Allegado> selectAllegado = new Select<>();
    private TextField nombreRegalo = new TextField("Nombre del regalo");
    private TextField precioRegalo = new TextField("Precio del regalo");
    private TextField urlRegalo = new TextField("URL del regalo");
    private Button addRegaloButton = new Button("Agregar Regalo");

    private Grid<Regalo> regalosGrid = new Grid<>(Regalo.class);
    private List<Regalo> regalosList = new ArrayList<>();

    private Button saveButton = new Button("Guardar Cambios");

    private Long id;
    private Lista lista;

    @Autowired
    public EditarListaView(AllegadoService allegadoService, ListaService listaService, RegaloService regaloService, AuthenticatedUser authenticatedUser) {
        this.allegadoService = allegadoService;
        this.listaService = listaService;
        this.regaloService = regaloService;
        this.authenticatedUser = authenticatedUser;

        configureRegalosGrid();
        configureForm();

        saveButton.addClickListener(e -> saveLista());

        add(new H1("Editar Lista de Regalos"), createFormLayout(), regalosGrid, saveButton);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> idParameter = event.getRouteParameters().get("id");
        if (idParameter.isPresent()) {
            try {
                id = Long.valueOf(idParameter.get());
                cargarDatosLista(id);
            } catch (IllegalArgumentException e) {
                Notification.show("ID de lista no válido.");
                event.forwardTo(ListaView.class);
            }
        } else {
            Notification.show("ID de lista no proporcionado.");
            event.forwardTo(ListaView.class);
        }
    }

    private void cargarDatosLista(Long id) {
        Optional<Lista> optionalLista = listaService.findById(id);
        if (optionalLista.isPresent()) {
            lista = optionalLista.get();

            Optional<Usuario> optionalUsuario = authenticatedUser.get();
            if (optionalUsuario.isEmpty() || !lista.getUsuario().equals(optionalUsuario.get())) {
                Notification.show("No tiene permisos para editar esta lista.");
                getUI().ifPresent(ui -> ui.navigate(ListaView.class));
                return;
            }

            listaNombre.setValue(lista.getNombre());

            regalosList.clear();
            regalosList.addAll(regaloService.findByListaId(id));
            regalosGrid.setItems(regalosList);
        } else {
            Notification.show("Lista no encontrada.");
            getUI().ifPresent(ui -> ui.navigate(ListaView.class));
        }
    }

    private void configureRegalosGrid() {
        Binder<Regalo> binder = new Binder<>(Regalo.class);
        Editor<Regalo> editor = regalosGrid.getEditor();
        editor.setBinder(binder);

        regalosGrid.removeAllColumns();

        Grid.Column<Regalo> nombreColumn = regalosGrid.addColumn(Regalo::getNombre)
                .setHeader("Nombre del Regalo");
        Grid.Column<Regalo> allegadoColumn = regalosGrid.addColumn(regalo -> regalo.getAllegado() != null ? regalo.getAllegado().getNombre() : "")
                .setHeader("Allegado");
        Grid.Column<Regalo> precioColumn = regalosGrid.addColumn(Regalo::getPrecio)
                .setHeader("Precio");
        Grid.Column<Regalo> urlColumn = regalosGrid.addColumn(Regalo::getUrl)
                .setHeader("URL");
        Grid.Column<Regalo> estadoColumn = regalosGrid.addColumn(Regalo::getEstado)
                .setHeader("Estado");
        Grid.Column<Regalo> accionesColumn = regalosGrid.addComponentColumn(regalo -> {
            Button eliminarButton = new Button("Eliminar");
            eliminarButton.addClickListener(e -> eliminarRegalo(regalo));
            return eliminarButton;
        }).setHeader("Acciones");

        Select<EstadoRegalo> estadoField = new Select<>();
        estadoField.setItems(EstadoRegalo.values());
        binder.forField(estadoField)
                .bind(Regalo::getEstado, Regalo::setEstado);
        estadoColumn.setEditorComponent(estadoField);

        regalosGrid.addItemDoubleClickListener(event -> {
            if (editor.isOpen()) {
                editor.cancel();
            }
            editor.editItem(event.getItem());
        });

        editor.addSaveListener(event -> {
            Regalo regaloEditado = event.getItem();
            regaloService.save(regaloEditado);
            actualizarCosteLista();
            actualizarEstadoLista();
        });

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
        regalo.setEstado(EstadoRegalo.POR_COMPRAR);

        regalo.setLista(lista);
        regalo = regaloService.save(regalo);
        regalosList.add(regalo);
        regalosGrid.setItems(regalosList);

        nombreRegalo.clear();
        precioRegalo.clear();
        urlRegalo.clear();
        selectAllegado.clear();

        actualizarCosteLista();
        actualizarEstadoLista();
    }

    private void saveLista() {
        if (lista == null) {
            Notification.show("No se ha cargado ninguna lista para editar.");
            return;
        }

        if (listaNombre.getValue().isEmpty()) {
            Notification.show("El nombre de la lista no puede estar vacío");
            return;
        }

        Optional<Usuario> optionalUsuario = authenticatedUser.get();
        if (optionalUsuario.isEmpty() || !lista.getUsuario().equals(optionalUsuario.get())) {
            Notification.show("No tiene permisos para editar esta lista.");
            getUI().ifPresent(ui -> ui.navigate(ListaView.class));
            return;
        }

        if (regalosList.isEmpty()) {
            Notification.show("No se puede guardar una lista sin regalos.");
            return;
        }

        lista.setNombre(listaNombre.getValue());
        lista.setUsuario(obtenerUsuarioAutenticado());

        for (Regalo regalo : regalosList) {
            regaloService.save(regalo);
        }

        actualizarEstadoLista();
        lista = listaService.save(lista);

        Notification.show("Lista de regalos actualizada exitosamente.");
        getUI().ifPresent(ui -> ui.navigate(ListaView.class));
    }

    private void actualizarCosteLista() {
        double costoTotal = regalosList.stream()
                .mapToDouble(regalo -> regalo.getPrecio() != null ? regalo.getPrecio() : 0.0)
                .sum();
        lista.setCoste(costoTotal);
        listaService.save(lista);
    }

    private Usuario obtenerUsuarioAutenticado() {
        Optional<Usuario> optionalUsuario = authenticatedUser.get();
        return optionalUsuario.orElse(null);
    }

    private void eliminarRegalo(Regalo regalo) {
        regalosList.remove(regalo);
        regalosGrid.setItems(regalosList);
        regaloService.deleteById(regalo.getId());
        actualizarCosteLista();
        actualizarEstadoLista();
    }

    private void actualizarEstadoLista() {
        boolean todosRecibidos = regalosList.stream()
                .filter(regalo -> regalo.getLista().getId().equals(lista.getId())) // Filtrar por la lista actual
                .allMatch(regalo -> regalo.getEstado() == EstadoRegalo.RECIBIDO);

        if (todosRecibidos) {
            lista.setEstado(EstadoLista.CERRADA);
        } else {
            lista.setEstado(EstadoLista.PENDIENTE);
        }

        listaService.save(lista);
    }
}