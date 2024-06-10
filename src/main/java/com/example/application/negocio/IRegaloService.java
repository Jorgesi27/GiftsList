package com.example.application.negocio;

import com.example.application.data.Regalo;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRegaloService {

    Regalo save(Regalo regalo);
    Optional<Regalo> findById(UUID id);
    List<Regalo> findAll();
    void deleteById(UUID id);
}
