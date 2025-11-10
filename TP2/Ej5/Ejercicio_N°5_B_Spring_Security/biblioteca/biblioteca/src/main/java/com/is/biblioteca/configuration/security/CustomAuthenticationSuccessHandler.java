package com.is.biblioteca.configuration.security;

import java.io.IOException;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.core.user.OAuth2User;

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
      System.out.println("HOLAAA 0");
      try {
        Object principal = authentication.getPrincipal();
        Usuario usuario = null;

        // Si viene de OAuth2, intentamos obtener el localUserId (lo pusimos en mappedAttrs)
        if (principal instanceof OAuth2User) {
          OAuth2User oauth = (OAuth2User) principal;
          Object localIdObj = oauth.getAttribute("localUserId");
          if (localIdObj != null) {
            String localId = String.valueOf(localIdObj);
            usuario = usuarioService.buscarUsuario(localId);
          } else {
            // Fallback: intentar con el email proveniente del proveedor (puede ser sintético)
            String email = oauth.getAttribute("email");
            if (email != null) {
              usuario = usuarioService.buscarUsuarioPorEmail(email);
            }
          }
        }

        // Si no es OAuth2 o no encontramos usuario aún, usar el nombre del Authentication (form-login)
        if (usuario == null) {
          String principalName = authentication.getName();
          usuario = usuarioService.buscarUsuarioPorEmail(principalName);
        }

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
