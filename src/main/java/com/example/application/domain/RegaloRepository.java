package com.example.application.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface RegaloRepository extends JpaRepository<Regalo, UUID> {
    void deleteByListaId(UUID listaId);
}
