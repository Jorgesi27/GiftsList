package com.example.application.services;

import com.example.application.domain.Regalo;
import com.example.application.domain.RegaloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RegaloService implements IRegaloService{

    @Autowired
    private RegaloRepository regaloRepository;

    @Override
    public Regalo save(Regalo regalo) {
        return regaloRepository.save(regalo);
    }

    @Override
    public Optional<Regalo> findById(UUID id) {
        return regaloRepository.findById(id);
    }

    @Override
    public List<Regalo> findAll() {
        return regaloRepository.findAll();
    }

    @Override
    public void deleteById(UUID id) {
        regaloRepository.deleteById(id);
    }
}