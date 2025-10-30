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

      // Obtener el nombreUsuario del usuario autenticado
      String nombreUsuario = authentication.getName();

      try {
        // Buscar el usuario completo en la base de datos
        Usuario usuario = usuarioService.buscarUsuarioPorNombre(nombreUsuario);

      // Guardar el usuario en la sesión HTTP (si existe)
      if (usuario != null) {
        HttpSession session = request.getSession();
        session.setAttribute("usuariosession", usuario);
      }

      // Redirigir según rol: ADMIN -> /portal/admin, EMPLEADO -> /portal/empleado, SOCIO -> /portal/socio
      String targetUrl = "/";
      if (authentication.getAuthorities() != null) {
        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isEmpleado = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLEADO"));
        boolean isSocio = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_SOCIO"));

        if (isAdmin) {
          targetUrl = "/portal/admin";
        } else if (isEmpleado) {
          targetUrl = "/portal/empleado";
        } else if (isSocio) {
          targetUrl = "/portal/socio";
        }
      }

      response.sendRedirect(targetUrl);
        
      } catch (ErrorServicio e) {
        e.printStackTrace();
        response.sendRedirect("/login?error");
      }
    }
}
