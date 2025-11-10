package com.practica.ej2consumer.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class JwtSessionTokenProvider {
    private JwtSessionTokenProvider() {}

    public static String getTokenFromSession() {
        var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return null;
        HttpSession session = attrs.getRequest().getSession(false);
        if (session == null) return null;
        Object t = session.getAttribute("jwtToken");
        return t != null ? t.toString() : null;
    }
}