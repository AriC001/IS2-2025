package com.sport.proyecto.config.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Borra todas las cookies visibles y invalida la sesión al hacer logout.
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        // Invalidate session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Remove all cookies available in the request
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                Cookie toDelete = new Cookie(cookie.getName(), "");
                toDelete.setPath(cookie.getPath() == null ? "/" : cookie.getPath());
                toDelete.setMaxAge(0);
                // For modern browsers, set SameSite if needed via header (not standardized in Servlet API)
                response.addCookie(toDelete);
            }
        }

        // Also explicitly remove JSESSIONID just in case
        Cookie js = new Cookie("JSESSIONID", "");
        js.setPath("/");
        js.setMaxAge(0);
        response.addCookie(js);

        // Redirect to login page with logout flag
        response.sendRedirect("/login?logout");
    }

}
