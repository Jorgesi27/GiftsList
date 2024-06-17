package com.example.application.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface RegaloRepository extends JpaRepository<Regalo, Long> {
    void deleteByListaId(Long listaId);
    List<Regalo> findByListaId(Long listaId);
    List<Regalo> findByAllegadoId(Long allegadoId);
    List<Regalo> findByListaIdAndAllegadoNotNull(Long id);
}
