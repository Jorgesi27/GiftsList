package com.example.application.services;

import com.example.application.domain.Lista;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IListaService {

    Lista save(Lista lista);
    Optional<Lista> findById(UUID id);
    List<Lista> findAll();
    void deleteById(UUID id);
    void deleteListaAndRegalos(UUID id);
}
