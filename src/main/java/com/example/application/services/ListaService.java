package com.example.application.services;

import com.example.application.domain.Lista;
import com.example.application.domain.ListaRepository;
import com.example.application.domain.RegaloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ListaService implements IListaService{

    @Autowired
    private ListaRepository listaRepository;
    private final RegaloRepository regaloRepository;

    public ListaService(RegaloRepository regaloRepository) {
        this.regaloRepository = regaloRepository;
    }

    @Override
    @Transactional
    public Lista save(Lista lista) {
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
}
