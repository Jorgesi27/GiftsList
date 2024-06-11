package com.example.application.services;

import com.example.application.domain.Allegado;
import com.example.application.domain.AllegadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AllegadoService implements IAllegadoService{

    @Autowired
    private AllegadoRepository allegadoRepository;

    @Override
    public Allegado save(Allegado allegado) {
        return allegadoRepository.save(allegado);
    }

    @Override
    public Optional<Allegado> findById(UUID id) {
        return allegadoRepository.findById(id);
    }

    @Override
    public List<Allegado> findAll() {
        return allegadoRepository.findAll();
    }

    @Override
    public void deleteById(UUID id) {
        allegadoRepository.deleteById(id);
    }
}
