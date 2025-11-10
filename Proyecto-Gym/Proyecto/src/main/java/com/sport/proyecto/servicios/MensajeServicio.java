package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Mensaje;
import com.sport.proyecto.entidades.Persona;
import com.sport.proyecto.entidades.Socio;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.enums.tipoMensaje;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.MensajeRepositorio;
import com.sport.proyecto.repositorios.PaisRepositorio;
import com.sport.proyecto.repositorios.PersonaRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MensajeServicio {

    // Para enviar correos
    @Autowired
    private JavaMailSender mailSender;

    // Thymeleaf para plantillas HTML (si querés usar plantillas)
    @Autowired(required = false)
    private SpringTemplateEngine templateEngine;

    @Autowired
    private MensajeRepositorio repositorioMensaje;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private PersonaServicio personaServicio;

    // Zona horaria
    private final ZoneId ZONE = ZoneId.of("America/Argentina/Mendoza");

    // Busqueda
    @Transactional
    public List<Mensaje> listarMensaje() throws ErrorServicio {
        try{
            return repositorioMensaje.findAll();
        }catch (Exception e){
            e.printStackTrace();
            throw new ErrorServicio("Error al listar los mensajes");
        }
    }

    @Transactional
    public List<Mensaje> listarMensajeActivo() throws ErrorServicio {
        try{
            return repositorioMensaje.findAllActives();
        }catch (Exception e){
            e.printStackTrace();
            throw new ErrorServicio("Error al listar los mensajes activos");
        }
    }

    @Transactional
    public Mensaje buscarMensaje(String id) throws ErrorServicio {
        try{
            return repositorioMensaje.findById(id).get();
        }catch (Exception e){
            e.printStackTrace();
            throw new ErrorServicio("Error al buscar el mensaje");
        }
    }

    // Alta
    @Transactional
    public void crearMensaje(String titulo, String texto, tipoMensaje tipo_mensaje, String idUsuario) throws ErrorServicio {
        try{
            Mensaje mensaje = new Mensaje();
            mensaje.setTitulo(titulo);
            mensaje.setTexto(texto);
            mensaje.setTipoMensaje(tipo_mensaje);
            mensaje.setEliminado(false);
            mensaje.setUsuario(usuarioServicio.buscarUsuario(idUsuario));
            repositorioMensaje.save(mensaje);
        }catch (Exception e){
            e.printStackTrace();
            throw new ErrorServicio("Error al crear el mensaje");
        }
    }

    // Modificacion

    @Transactional
    public void modificarMensaje(String id, String titulo, String texto, tipoMensaje tipo_mensaje, String idUsuario) throws ErrorServicio {
        try{
            Optional<Mensaje> respuesta = repositorioMensaje.findById(id);
            if(respuesta.isPresent()){
                Mensaje mensaje = respuesta.get();
                mensaje.setTitulo(titulo);
                mensaje.setTexto(texto);
                mensaje.setTipoMensaje(tipo_mensaje);
                mensaje.setUsuario(usuarioServicio.buscarUsuario(idUsuario));
                repositorioMensaje.save(mensaje);
            }else{
                throw new ErrorServicio("No se encontro el mensaje solicitado");
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ErrorServicio("Error al modificar el mensaje");
        }
    }

    // Baja

    @Transactional
    public void eliminarMensaje(String id) throws ErrorServicio {
        try{
            Optional<Mensaje> respuesta = repositorioMensaje.findById(id);
            if(respuesta.isPresent()){
                Mensaje mensaje = respuesta.get();
                mensaje.setEliminado(true);
                repositorioMensaje.save(mensaje);
            }else{
                throw new ErrorServicio("No se encontro el mensaje solicitado");
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ErrorServicio("Error al eliminar el mensaje");
        }
    }

    // Validacion

    private void validar(String titulo, String texto, tipoMensaje tipo_mensaje, String idUsuario) throws ErrorServicio {
        if(titulo == null || titulo.isEmpty()){
            throw new ErrorServicio("El titulo no puede ser nulo o vacio");
        }
        if(texto == null || texto.isEmpty()){
            throw new ErrorServicio("El texto no puede ser nulo o vacio");
        }
        if(tipo_mensaje == null){
            throw new ErrorServicio("El tipo de mensaje no puede ser nulo");
        }
        if(idUsuario == null || idUsuario.isEmpty()){
            throw new ErrorServicio("El id del usuario no puede ser nulo o vacio");
        }
    }

    // --- Metodo para enviar mensaje por email ---

    @Transactional
    public void enviarMensaje(String idMensaje) throws ErrorServicio {
        try {
            Mensaje mensaje = buscarMensaje(idMensaje);
            if (mensaje == null) {
                throw new ErrorServicio("No se encontró el mensaje solicitado");
            }

            Usuario destinatario = mensaje.getUsuario();
            if (destinatario == null) {
                throw new ErrorServicio("El mensaje no tiene un usuario asociado");
            }

            // Obtener email desde Persona asociada al Usuario

            String emailDestinatario = null;

            Persona personaOpt = personaServicio.buscarPorUsuario(destinatario.getId());
            if (personaOpt != null) {
                emailDestinatario = personaOpt.getEmail();
            }
            if (personaOpt == null) {
                throw new ErrorServicio("No se encontró una persona asociada al usuario destinatario");
            }


            if (emailDestinatario == null || emailDestinatario.trim().isEmpty()) {
                throw new ErrorServicio("No se encontró un email válido para el destinatario");
            }

            String asunto = mensaje.getTitulo() != null ? mensaje.getTitulo() : "Mensaje del sistema";
            String cuerpo = mensaje.getTexto() != null ? mensaje.getTexto() : "";

            enviarTexto(emailDestinatario, asunto, cuerpo);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServicio("Error al enviar el mensaje: " + e.getMessage());
        }
    }

    @Transactional
    @Scheduled(cron = "0 14 8 * * *", zone = "America/Argentina/Mendoza") // todos los días a las 8:14 AM
    public void enviarSaludosCumpleanos() {
        LocalDate hoy = LocalDate.now();
        int dia = hoy.getDayOfMonth();
        int mes = hoy.getMonthValue();

        List<Persona> cumpleaneros = personaServicio.buscarPorDiaYMes(dia, mes);

        for (Persona p : cumpleaneros) {
            if (p.getEmail() == null || p.getEmail().isBlank()) {
                continue; // sin mail, no se envía
            }

            // asunto = nombre y apellido
            String subject = p.getNombre() + " " + p.getApellido();

            // cuerpo del mensaje
            String body = "¡Feliz cumpleaños " + p.getNombre() + "! 🎉\n\n"
              + "Todo el equipo de SPORT te desea un gran día lleno de salud, "
              + "energía y muchos entrenamientos motivadores.\n\n"
              + "¡Abrazo fuerte!\nSPORT";

            enviarTexto(p.getEmail(), subject, body);
        }
    }

    // --- helper para envío ---
    private void enviarTexto(String to, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        // opcional: setFrom
        msg.setFrom("gimnasiosport001@gmail.com");
        mailSender.send(msg);
    }

}
