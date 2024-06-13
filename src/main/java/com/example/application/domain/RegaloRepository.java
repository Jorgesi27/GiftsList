package com.example.application.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RegaloRepository extends JpaRepository<Regalo, UUID> {
    void deleteByListaId(UUID id);
    List<Regalo> findByListaId(UUID id);
}
