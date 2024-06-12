package com.example.application.services;

import com.example.application.domain.Usuario;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

@Service
public class EmailRealService implements EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.email}")
    private String defaultMail;

    @Value("${server.port}")
    private int serverPort;

    public EmailRealService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private String getServerUrl() {

        // Generate the server URL
        String serverUrl = "http://";
        serverUrl += InetAddress.getLoopbackAddress().getHostAddress();
        serverUrl += ":" + serverPort + "/";
        return serverUrl;
    }

    @Override
    public boolean sendRegistrationEmail(Usuario user) {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String subject = "Bienvenido";
        String body = "Por favor active su cuenta para disfrutar de los servicios de Gifts Lists. "
                + "Ir a " + getServerUrl() + "useractivation "
                + "e introduce tu email y el siguiente código: "
                + user.getRegisterCode();

        try {
            helper.setFrom(defaultMail);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(body);
            this.mailSender.send(message);
        } catch (MailException | MessagingException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}

