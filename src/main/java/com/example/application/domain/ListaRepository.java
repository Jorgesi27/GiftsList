package com.example.application.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListaRepository extends JpaRepository<Lista, UUID> {
    void deleteById(UUID id);
    Optional<Lista> findById(UUID id);
    List<Lista> findByUsuario(Usuario usuario);
}
