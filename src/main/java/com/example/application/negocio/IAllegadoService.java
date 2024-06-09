package com.example.application.negocio;

import com.example.application.data.Allegado;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAllegadoService {

    Allegado save(Allegado allegado);
    Optional<Allegado> findById(UUID id);
    List<Allegado> findAll();
    void deleteById(UUID id);
}
