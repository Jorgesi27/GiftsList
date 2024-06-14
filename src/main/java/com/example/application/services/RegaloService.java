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
    @Transactional
    public Regalo save(Regalo regalo) {
        return regaloRepository.save(regalo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Regalo> findById(UUID id) {
        return regaloRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Regalo> findAll() {
        return regaloRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        regaloRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByListaId(UUID id) {
        regaloRepository.deleteByListaId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Regalo> findByListaId(UUID id) {
        return regaloRepository.findByListaId(id);
    }
}
