package com.example.application.user;

import com.example.application.domain.Usuario;
import com.example.application.services.EmailService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class EmailFakeService implements EmailService {

    @Override
    public boolean sendRegistrationEmail(Usuario user) {

        String subject = "Bienvenido";
        String body = "Por favor, active su cuenta. "
                + "Vaya a http://localhost:8080/useractivation "
                + "e introduzca el siguiente código: "
                + user.getRegisterCode();

        try {
            System.out.println("Desde: GiftsList (testing)");
            System.out.println("A: " + user.getEmail());
            System.out.println("Asunto: " + subject);
            System.out.println("Cuerpo: " + body);

            int secondsToSleep = 5;
            Thread.sleep(secondsToSleep * 1000);
            System.out.println("Simulación de email hecha!");
            return true;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
