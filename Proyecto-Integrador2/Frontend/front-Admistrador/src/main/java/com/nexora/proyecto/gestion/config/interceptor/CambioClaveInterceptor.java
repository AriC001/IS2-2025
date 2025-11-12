package com.nexora.proyecto.gestion.config.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Interceptor que verifica si el usuario requiere cambio de contraseña.
 * Si requiere cambio, bloquea el acceso a todas las rutas excepto /auth/cambiar-clave.
 */
@Component
public class CambioClaveInterceptor implements HandlerInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(CambioClaveInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    HttpSession session = request.getSession(false);
    
    // Si no hay sesión, permitir el acceso (el login se encargará)
    if (session == null) {
      return true;
    }
    
    // Verificar si requiere cambio de contraseña
    Boolean requiereCambio = (Boolean) session.getAttribute("requiereCambioClave");
    
    if (requiereCambio != null && requiereCambio) {
      String requestPath = request.getRequestURI();
      
      // Permitir acceso solo a la página de cambio de contraseña y recursos estáticos
      if (requestPath.startsWith("/auth/cambiar-clave") || 
          requestPath.startsWith("/css/") ||
          requestPath.startsWith("/js/") ||
          requestPath.startsWith("/vendor/") ||
          requestPath.startsWith("/img/") ||
          requestPath.equals("/error")) {
        return true;
      }
      
      // Bloquear acceso a cualquier otra ruta
      logger.warn("⚠ Intento de acceso bloqueado para usuario {} que requiere cambio de contraseña. Ruta: {}", 
          session.getAttribute("nombreUsuario"), requestPath);
      response.sendRedirect("/auth/cambiar-clave");
      return false;
    }
    
    return true;
  }

}

