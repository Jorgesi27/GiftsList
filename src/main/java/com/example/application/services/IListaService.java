package com.example.application.services;

import com.example.application.domain.Lista;
import com.example.application.domain.Regalo;
import com.example.application.domain.Usuario;
import com.vaadin.flow.data.provider.DataProvider;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IListaService {

    Lista save(Lista lista);
    Optional<Lista> findById(Long id);
    List<Lista> findAll();
    void deleteById(Long id);
    void deleteListaAndRegalos(Long id);
    Optional<Lista> findByNombreAndUsuario(String nombreLista, Usuario usuario);
    List<Lista> findAllByUsuario(Usuario usuario);
}
