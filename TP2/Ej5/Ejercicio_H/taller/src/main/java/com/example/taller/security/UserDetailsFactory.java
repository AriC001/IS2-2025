package com.example.taller.security;

import com.example.taller.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * Factory creacional para crear instancias de UserDetails a partir de Usuario.
 */
public class UserDetailsFactory {

    public static UserDetails create(Usuario usuario) {
        if (usuario == null) return null;

        // Spring Security's hasRole/hasAnyRole helpers add the "ROLE_" prefix internally.
        // To be consistent, grant authorities using the conventional "ROLE_" prefix.
        String roleName = usuario.getRol() != null ? usuario.getRol().toString() : "USER";
        GrantedAuthority role = new SimpleGrantedAuthority("ROLE_" + roleName);
        return new User(usuario.getNombreUsuario(), usuario.getClave(), List.of(role));
    }
}
