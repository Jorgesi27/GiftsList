package com.example.application.services;

import com.example.application.domain.*;
import com.example.application.security.AuthenticatedUser;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;


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

    @Override
    public Lista save(Lista lista) {
        Usuario usuario = authenticatedUser.get().orElseThrow(() -> new IllegalStateException("No se encontró ningún usuario logueado."));

        Optional<Lista> existingLista = listaRepository.findByNombreAndUsuario(lista.getNombre(), usuario);
        if (existingLista.isPresent() && !existingLista.get().getId().equals(lista.getId())) {
            throw new IllegalArgumentException("Ya existe una lista con el mismo nombre para este usuario.");
        }

        lista.setUsuario(usuario);
        return listaRepository.save(lista);
    }

    @Override
    public Optional<Lista> findByNombreAndUsuario(String nombreLista, Usuario usuario) {
        return listaRepository.findByNombreAndUsuario(nombreLista, usuario);
    }

    @Override
    public Optional<Lista> findById(Long id) {
        return listaRepository.findById(id);
    }

    @Override
    public List<Lista> findAll() {
        return listaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        listaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<Lista> findAllByUsuario(Usuario usuario) {
        List<Lista> listas = listaRepository.findByUsuario(usuario);
        listas.forEach(lista -> Hibernate.initialize(lista.getRegalos()));
        return listas;
    }

    @Override
    @Transactional
    public void deleteListaAndRegalos(Long id) {
        regaloRepository.deleteByListaId(id);
        List<Regalo> regalosConAllegados = regaloRepository.findByListaIdAndAllegadoNotNull(id);
        for (Regalo regalo : regalosConAllegados) {
            regalo.setAllegado(null);
        }
        regaloRepository.saveAll(regalosConAllegados);
        listaRepository.deleteById(id);
    }
}
