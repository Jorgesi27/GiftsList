package com.example.application.services;

import com.example.application.domain.Regalo;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRegaloService {

    Regalo save(Regalo regalo);
    Optional<Regalo> findById(UUID id);
    List<Regalo> findAll();
    void deleteById(UUID id);
}
