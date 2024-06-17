package com.example.application.services;

import com.example.application.domain.Regalo;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRegaloService {

    Regalo save(Regalo regalo);
    Optional<Regalo> findById(Long id);
    List<Regalo> findAll();
    void deleteById(Long id);
    void deleteByListaId(Long listaId);
    List<Regalo> findByListaId(Long listaId);
    boolean hasRegalos(Long allegadoId);
    public List<Regalo> findByListaIdAndAllegadoNotNull(Long listaId);
}
