package edu.egg.tinder.configuration;

import java.io.IOException;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.servicios.UsuarioServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioServicio usuarioService;

    public CustomAuthenticationSuccessHandler(@Lazy UsuarioServicio usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
      // Obtener el email del usuario autenticado
      String email = authentication.getName();
      
      try {
        // Buscar el usuario completo en la base de datos
        Usuario usuario = usuarioService.buscarUsuarioPorEmail(email);
          
        // Guardar el usuario en la sesión HTTP
        HttpSession session = request.getSession();
        session.setAttribute("usuariosession", usuario);
          
        // Redirigir a /inicio que maneja la lógica según el rol
        response.sendRedirect("/inicio");

      } catch (ErrorServicio e) {
        // Manejar el error si el usuario no se encuentra
        response.sendRedirect("/login?error=true");
      }
    }
}
