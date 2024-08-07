package com.example.application.services;

import com.example.application.domain.Allegado;
import com.example.application.domain.Usuario;
import java.util.List;
import java.util.Optional;


public interface IAllegadoService {

    Allegado save(Allegado allegado);
    Optional<Allegado> findById(Long id);
    List<Allegado> findAll();
    void deleteById(Long id);
    List<Allegado> findAllByUsuario(Usuario usuario);
}
