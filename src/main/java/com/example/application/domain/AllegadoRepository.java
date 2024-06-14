package com.example.application.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AllegadoRepository extends JpaRepository<Allegado, UUID> {
    List<Allegado> findByUsuario(Usuario usuario);
}
