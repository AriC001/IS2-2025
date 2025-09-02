package edu.egg.tinder.servicios;

import edu.egg.tinder.entidades.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificacionServicio {

    @Autowired
    private JavaMailSender mailSender;

    @Transactional
    @Async
    public void enviarMail(String body, String title, String mail) {
        return;
        /*SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(mail);
        mensaje.setFrom("noreply@tinder-mascota.com");
        mensaje.setSubject(title);
        mensaje.setText(body);

        mailSender.send(mensaje);*/

    }
}
