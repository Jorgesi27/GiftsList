package com.example.application.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface ListaRepository extends JpaRepository<Lista, Long> {
    void deleteById(Long id);
    Optional<Lista> findById(Long id);
    List<Lista> findByUsuario(Usuario usuario);
    Optional<Lista> findByNombreAndUsuario(String nombre, Usuario usuario);
}
