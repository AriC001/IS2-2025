package com.is.biblioteca.configuration.security;

import java.io.IOException;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.is.biblioteca.business.domain.entity.Usuario;
import com.is.biblioteca.business.logic.error.ErrorServiceException;
import com.is.biblioteca.business.logic.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioService usuarioService;

    public CustomAuthenticationSuccessHandler(@Lazy UsuarioService usuarioService) {
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
          
        // Redirigir a /regresoPage que maneja la lógica según el rol
        response.sendRedirect("/regresoPage");
          
      } catch (ErrorServiceException e) {
        e.printStackTrace();
        response.sendRedirect("/usuario/login?error");
      }
    }
}
