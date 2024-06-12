package com.example.application.services;

import com.example.application.domain.Usuario;
import com.example.application.domain.UsuarioRepository;
import com.example.application.enums.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserManagementService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public UserManagementService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public boolean registerUser(Usuario user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegisterCode(UUID.randomUUID().toString().substring(0, 5));
        user.addRole(Rol.USER);

        try {
            usuarioRepository.save(user);
            emailService.sendRegistrationEmail(user);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public Usuario loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> user = usuarioRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("No user present with username: " + email);
        } else {
            return user.get();
        }
    }

    public boolean activateUser(String email, String registerCode) {

        Optional<Usuario> user = usuarioRepository.findByEmail(email);

        if (user.isPresent() && user.get().getRegisterCode().equals(registerCode)) {
            user.get().setActive(true);
            usuarioRepository.save(user.get());
            return true;

        } else {
            return false;
        }

    }

    public Optional<Usuario> loadUserById(UUID id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> loadActiveUsers() {
        return usuarioRepository.findByActiveTrue();
    }

    public void delete(Usuario testUser) {
        usuarioRepository.delete(testUser);
    }

    public int count() {
        return (int) usuarioRepository.count();
    }
}