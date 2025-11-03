package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @ModelAttribute("loggedUser")
    public String loggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getUsername();
        } else if (principal instanceof OAuth2User) {
            return ((OAuth2User) principal).getAttribute("name");
        } else if (principal instanceof String) {
            return (String) principal;
        }
        return null;
    }

    /**
     * Expose the application's {@link Usuario} as `usuariosession` in the model and
     * also ensure it's present in the HttpSession for existing code that reads
     * session attributes directly.
     * Supports both form login (User) and OAuth2 login (OAuth2User).
     */
    @ModelAttribute("usuariosession")
    public Usuario usuariosession(HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            session.removeAttribute("usuariosession");
            return null;
        }

        // Intentar obtener username desde diferentes tipos de principal
        String username = null;
        Object principal = auth.getPrincipal();
        
        if (principal instanceof User) {
            // Login por formulario
            username = ((User) principal).getUsername();
            System.out.println("GlobalControllerAdvice: Form login user=" + username);
        } else if (principal instanceof OAuth2User) {
            // Login por OAuth2 (GitHub, etc.)
            OAuth2User oauth2User = (OAuth2User) principal;
            username = oauth2User.getAttribute("name");
            System.out.println("GlobalControllerAdvice: OAuth2 user name=" + username);
        } else if (principal instanceof String) {
            // Fallback para anonymousUser u otros casos
            username = (String) principal;
            System.out.println("GlobalControllerAdvice: String principal=" + username);
        }

        if (username == null || username.equals("anonymousUser")) {
            session.removeAttribute("usuariosession");
            return null;
        }

        try {
            Usuario u = usuarioServicio.buscarUsuarioPorNombre(username);
            if (u != null) {
                // keep HttpSession in sync for controllers that still read it
                session.setAttribute("usuariosession", u);
                System.out.println("GlobalControllerAdvice: Usuario encontrado y guardado en sesión: " + u.getNombreUsuario());
            } else {
                System.err.println("GlobalControllerAdvice: No se encontró usuario en BD para username=" + username);
            }
            return u;
        } catch (Exception e) {
            System.err.println("GlobalControllerAdvice: Error buscando usuario: " + e.getMessage());
            e.printStackTrace();
            // if lookup fails, clear any stale session attribute and return null
            session.removeAttribute("usuariosession");
            return null;
        }
    }
}