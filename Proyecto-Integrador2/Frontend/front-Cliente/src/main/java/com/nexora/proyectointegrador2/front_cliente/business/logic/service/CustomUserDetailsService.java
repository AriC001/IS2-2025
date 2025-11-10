package com.nexora.proyectointegrador2.front_cliente.business.logic.service;


// Reworked to use UsuarioDTO via UsuarioService (API/DAO)
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.User;
import com.nexora.proyectointegrador2.front_cliente.dto.UsuarioDTO;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    // ahí le pude meter la autenticación. Se pueden logear los usuarios en general (se loguean con nombre de usuario y clave)
    // ahora tengo que ver la parte de autorización

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        Optional<UsuarioDTO> opt = usuarioService.findByUsername(nombreUsuario);
        UsuarioDTO usuario = opt.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Aquí puedes agregar la lógica para obtener los roles del usuario
        GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + (usuario.getRol() != null ? usuario.getRol().toString() : "USUARIO"));

        return User
                .withUsername(usuario.getNombreUsuario())
                .password(usuario.getClave() != null ? usuario.getClave() : "") // la clave debe existir y estar encriptada
                .authorities(p)
                .build();
    }

    public UsuarioDTO buscarUsuarioPorNombreUsuario(String nombreUsuario) {
        return usuarioService.findByUsername(nombreUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
