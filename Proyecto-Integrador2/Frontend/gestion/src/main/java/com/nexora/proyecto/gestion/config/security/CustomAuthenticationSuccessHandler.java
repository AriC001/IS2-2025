package com.nexora.proyecto.gestion.config.security;

import java.io.IOException;

import org.springframework.context.annotation.Lazy;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.nexora.proyecto.gestion.dto.UsuarioDTO;
import com.nexora.proyecto.gestion.business.logic.service.UsuarioService;

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

      // Obtener el nombreUsuario del usuario autenticado
      String nombreUsuario = authentication.getName();
      System.out.println("CustomAuthenticationSuccessHandler: nombreUsuario=" + nombreUsuario);
      System.out.println("CustomAuthenticationSuccessHandler: authentication.getPrincipal()=" + authentication.getPrincipal().getClass().getName());

      try {
        // Buscar el usuario completo en la base de datos
        Optional<UsuarioDTO> usuarioOpt = usuarioService.findByUsername(nombreUsuario);

        if (usuarioOpt.isEmpty()) {
          System.err.println("ERROR: No se encontró usuario en BD con nombre: " + nombreUsuario);
          response.sendRedirect("/auth/login?error=usuario_no_encontrado");
          return;
        }

        UsuarioDTO usuario = usuarioOpt.get();
        System.out.println("Usuario encontrado en BD: " + usuario.getNombreUsuario() + " (rol: " + usuario.getRol() + ")");

        // Guardar el usuario en la sesión HTTP
        HttpSession session = request.getSession();
        session.setAttribute("usuariosession", usuario);
        System.out.println("Usuario guardado en sesión HTTP");

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

        System.out.println("Redirigiendo a: " + targetUrl);
        response.sendRedirect(targetUrl);
        
      } catch (Exception e) {
        System.err.println("Exception en CustomAuthenticationSuccessHandler: " + e.getMessage());
        e.printStackTrace();
        response.sendRedirect("/auth/login?error");
      }
    }
}
