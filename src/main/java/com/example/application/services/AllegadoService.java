package com.example.application.services;

import com.example.application.domain.Allegado;
import com.example.application.domain.AllegadoRepository;
import com.example.application.domain.Usuario;
import com.example.application.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AllegadoService implements IAllegadoService{

    private AllegadoRepository allegadoRepository;
    private final AuthenticatedUser authenticatedUser;

    @Autowired
    public AllegadoService(AllegadoRepository allegadoRepository, AuthenticatedUser authenticatedUser) {
        this.allegadoRepository = allegadoRepository;
        this.authenticatedUser = authenticatedUser;
    }

    @Override
    public Allegado save(Allegado allegado) {
        Optional<Usuario> optionalUsuario = authenticatedUser.get();
        if (optionalUsuario.isPresent()) {
            Usuario usuarioLogueado = optionalUsuario.get();
            allegado.setUsuario(usuarioLogueado);
            return allegadoRepository.save(allegado);
        } else {
            throw new IllegalStateException("No se encontró ningún usuario logueado.");
        }
    }

    @Override
    public Optional<Allegado> findById(Long id) {
        return allegadoRepository.findById(id);
    }

    @Override
    public List<Allegado> findAll() {
        return allegadoRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        allegadoRepository.deleteById(id);
    }

    @Override
    public List<Allegado> findAllByUsuario(Usuario usuario) {
        return allegadoRepository.findByUsuario(usuario);
    }
}
