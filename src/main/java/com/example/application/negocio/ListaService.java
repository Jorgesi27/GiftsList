package com.example.application.negocio;

import com.example.application.data.Lista;
import com.example.application.data.ListaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ListaService implements IListaService{

    @Autowired
    private ListaRepository listaRepository;

    @Override
    public Lista save(Lista lista) {
        return listaRepository.save(lista);
    }

    @Override
    public Optional<Lista> findById(UUID id) {
        return listaRepository.findById(id);
    }

    @Override
    public List<Lista> findAll() {
        return listaRepository.findAll();
    }

    @Override
    public void deleteById(UUID id) {
        listaRepository.deleteById(id);
    }
}
