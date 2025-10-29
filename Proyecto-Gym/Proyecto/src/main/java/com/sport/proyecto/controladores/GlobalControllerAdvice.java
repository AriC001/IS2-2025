package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @ModelAttribute("loggedUser")
    public String loggedUser(@AuthenticationPrincipal User user) {
        return user != null ? user.getUsername() : null;
    }

    /**
     * Expose the application's {@link Usuario} as `usuariosession` in the model and
     * also ensure it's present in the HttpSession for existing code that reads
     * session attributes directly.
     */
    @ModelAttribute("usuariosession")
    public Usuario usuariosession(@AuthenticationPrincipal User user, HttpSession session) {
        if (user == null) {
            // no authenticated user
            session.removeAttribute("usuariosession");
            return null;
        }

        String username = user.getUsername();
        try {
            Usuario u = usuarioServicio.buscarUsuarioPorNombre(username);
            if (u != null) {
                // keep HttpSession in sync for controllers that still read it
                session.setAttribute("usuariosession", u);
            }
            return u;
        } catch (Exception e) {
            // if lookup fails, clear any stale session attribute and return null
            session.removeAttribute("usuariosession");
            return null;
        }
    }
}