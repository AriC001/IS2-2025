package com.sport.proyecto.servicios;


import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getEmail())
                .password(usuario.getPassword()) // ¡encriptado!
                .roles("USER") // o los que correspondan
                .build();
    }
}
