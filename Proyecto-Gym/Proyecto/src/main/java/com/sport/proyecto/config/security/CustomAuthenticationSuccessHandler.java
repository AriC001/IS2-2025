package com.sport.proyecto.config.security;

import java.io.IOException;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.servicios.UsuarioServicio;

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
        Usuario usuario = usuarioService.buscarUsuarioPorNombre(email);
            
          // Guardar el usuario en la sesión HTTP
          HttpSession session = request.getSession();
          session.setAttribute("usuariosession", usuario);
            
          // Redirigir a /regresoPage que maneja la lógica según el rol
          response.sendRedirect("/regresoPage");
        
      } catch (ErrorServicio e) {
        e.printStackTrace();
        response.sendRedirect("/usuario/login?error");
      }
    }
}
