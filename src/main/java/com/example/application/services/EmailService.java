package com.example.application.services;

import com.example.application.domain.Usuario;

public interface EmailService {

    boolean sendRegistrationEmail(Usuario user);
}