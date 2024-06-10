package com.example.application.presentacion;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;


@Route(value = "crear-lista")
@PageTitle("Crea Lista de Regalos")
@AnonymousAllowed
public class CrearListaView extends VerticalLayout{

}
