package com.example.application.services;

import com.example.application.domain.Lista;
import com.example.application.domain.ListaRepository;
import com.example.application.domain.RegaloRepository;
import com.example.application.domain.Usuario;
import com.example.application.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ListaService implements IListaService{

    private final ListaRepository listaRepository;
    private final RegaloRepository regaloRepository;
    private final AuthenticatedUser authenticatedUser;

    @Autowired
    public ListaService(ListaRepository listaRepository, RegaloRepository regaloRepository, AuthenticatedUser authenticatedUser){
        this.listaRepository = listaRepository;
        this.regaloRepository = regaloRepository;
        this.authenticatedUser = authenticatedUser;
    }

    @Transactional
    public Lista save(Lista lista) {

        Usuario usuario = authenticatedUser.get().orElseThrow(() -> new IllegalStateException("No se encontró ningún usuario logueado."));
        lista.setUsuario(usuario);
        return listaRepository.save(lista);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Lista> findById(UUID id) {
        return listaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lista> findAll() {
        return listaRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        listaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteListaAndRegalos(UUID id) {
        regaloRepository.deleteByListaId(id);
        listaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Lista> findAllByUsuario() {
        Usuario usuario = authenticatedUser.get().orElseThrow(() -> new IllegalStateException("No se encontró ningún usuario logueado."));
        return listaRepository.findByUsuario(usuario);
    }
}
