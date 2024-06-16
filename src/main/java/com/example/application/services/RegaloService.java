package com.example.application.services;

import com.example.application.domain.Regalo;
import com.example.application.domain.RegaloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegaloService implements IRegaloService{

    @Autowired
    private RegaloRepository regaloRepository;

    @Override
    public Regalo save(Regalo regalo) {
        return regaloRepository.save(regalo);
    }

    @Override
    public Optional<Regalo> findById(Long id) {
        return regaloRepository.findById(id);
    }

    @Override
    public List<Regalo> findAll() {
        return regaloRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        regaloRepository.deleteById(id);
    }

    @Override
    public void deleteByListaId(Long listaId) {
        regaloRepository.deleteByListaId(listaId);
    }

    @Override
    public List<Regalo> findByListaId(Long listaId) {
        return regaloRepository.findByListaId(listaId);
    }

    public boolean hasRegalos(Long allegado) {
        List<Regalo> regalos = regaloRepository.findByAllegadoId(allegado);
        return !regalos.isEmpty();
    }
}
