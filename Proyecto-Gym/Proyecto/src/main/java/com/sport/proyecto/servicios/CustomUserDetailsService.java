package com.sport.proyecto.servicios;


import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.repositorios.UsuarioRepositorio;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // ahí le pude meter la autenticación. Se pueden logear los usuarios en general (se loguean con nombre de usuario y clave)
    // ahora tengo que ver la parte de autorización

    @Autowired
    private UsuarioRepositorio usuarioRepository; 
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario =new Usuario();
        Optional<Usuario> opt = usuarioRepository.findByUsername(email);
        if(opt.isPresent()){
            usuario = opt.get();
        }else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        // Aquí puedes agregar la lógica para obtener los roles del usuario
        GrantedAuthority p = new SimpleGrantedAuthority(usuario.getRol().toString());


        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getNombreUsuario())
                .password(usuario.getClave()) // ¡ya está encriptado!
                .roles(p.toString())
                .build();
    }

    @Transactional
    public Usuario buscarUsuarioPorNombreUsuario(String nombreUsuario) {
        Usuario usuario = usuarioRepository.findByUsername(nombreUsuario).orElse(null);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return usuario;
    }
}
