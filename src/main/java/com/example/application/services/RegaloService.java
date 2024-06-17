package com.example.application.services;

import com.example.application.domain.Regalo;
import com.example.application.domain.RegaloRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class RegaloService implements IRegaloService{

    @Autowired
    private RegaloRepository regaloRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Regalo> findByListaIdAndAllegadoNotNull(Long listaId) {
        TypedQuery<Regalo> query = entityManager.createQuery(
                "SELECT r FROM Regalo r WHERE r.lista.id = :listaId AND r.allegado IS NOT NULL",
                Regalo.class
        );
        query.setParameter("listaId", listaId);
        return query.getResultList();
    }

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
