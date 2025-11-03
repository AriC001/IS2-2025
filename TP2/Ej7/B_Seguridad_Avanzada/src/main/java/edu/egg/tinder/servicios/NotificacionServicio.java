package edu.egg.tinder.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class NotificacionServicio {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Transactional
    @Async
    public void enviarMail(String cuerpo, String titulo, String destinatario) {
        
        if (mailSender == null) {
            System.out.println("⚠️ JavaMailSender no configurado. Email no enviado a: " + destinatario);
            System.out.println("📧 Título: " + titulo);
            System.out.println("📝 Cuerpo: " + cuerpo);
            return;
        }

        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(destinatario);    
            mensaje.setFrom("noreply@tinder-mascota.com");
            mensaje.setSubject(titulo);
            mensaje.setText(cuerpo);

            mailSender.send(mensaje);
            System.out.println("✅ Email enviado exitosamente a: " + destinatario);
            
        } catch (Exception e) {
            // Si falla el envío, solo registrar el error sin romper la aplicación
            System.err.println("❌ Error al enviar email a: " + destinatario);
            System.err.println("Error: " + e.getMessage());
            System.out.println("📧 Título: " + titulo);
            System.out.println("📝 Cuerpo: " + cuerpo);
        }

    }
}
