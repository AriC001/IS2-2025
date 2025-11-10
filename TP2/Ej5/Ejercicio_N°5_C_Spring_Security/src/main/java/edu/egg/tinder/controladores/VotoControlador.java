package edu.egg.tinder.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.egg.tinder.entidades.Mascota;
import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.servicios.MascotaServicio;
import edu.egg.tinder.servicios.UsuarioServicio;
import edu.egg.tinder.servicios.VotoServicio;
import jakarta.servlet.http.HttpSession;
@Controller
@RequestMapping("/votar")
public class VotoControlador {
    
    @Autowired
    private MascotaServicio mascotaServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private VotoServicio votoServicio;

    @GetMapping("/mascotasvotar")
    public String mascotasVotar(){
        return "mascotasvotar";
    }

    // Paso 1: mostrar pantalla de votación
    @GetMapping("/{idMascota}")
    public String mostrarPantallaVoto(@PathVariable Long idMascota,
                                      HttpSession session,
                                      Model modelo) throws ErrorServicio {
        Usuario login = (Usuario) session.getAttribute("usuariosession");

        // Mascota que va a recibir el voto
        Mascota mascotaAVotar = mascotaServicio.buscarMascota(idMascota);

        // Mascotas del usuario logueado (activas)
        List<Mascota> mascotasUsuario = mascotaServicio.listarMascotas(login.getId());

        modelo.addAttribute("mascotaAVotar", mascotaAVotar);
        modelo.addAttribute("mascotasUsuario", mascotasUsuario);

        return "confirmar-voto"; // confirm-voto.html
    }

    // Paso 2: procesar voto
    @PostMapping("/confirmar-voto")
    public String confirmarVoto(@RequestParam("idMascotaVotada") Long idMascotaVotada,
                                @RequestParam("idMascotaVotante") Long idMascotaVotante,
                                HttpSession session,
                                 RedirectAttributes redirectAttributes) throws ErrorServicio {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        try {
            votoServicio.votar(login.getId(), idMascotaVotante, idMascotaVotada);
            redirectAttributes.addFlashAttribute("exitoMensaje", "¡Voto registrado correctamente!");
            return "redirect:/mascotas/votar-mascotas";
        } catch (ErrorServicio e) {
            // Pasamos el mensaje de error a la vista
            redirectAttributes.addFlashAttribute("errorMensaje", e.getMessage());
            return "redirect:/votar/" + idMascotaVotada; // aquí poné la página donde se muestra el formulario
        }
    }

    @PostMapping("/responder")
    public String responder(@RequestParam Long id,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            votoServicio.responder(usuario.getId(), id);
            redirectAttributes.addFlashAttribute("exito", "Has respondido al voto.");
        } catch (ErrorServicio e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/mascotas/votos";
    }
}
