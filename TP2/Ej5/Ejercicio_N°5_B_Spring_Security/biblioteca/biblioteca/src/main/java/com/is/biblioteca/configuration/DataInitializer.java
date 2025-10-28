package com.is.biblioteca.configuration;

import java.util.UUID;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.is.biblioteca.business.domain.entity.Usuario;
import com.is.biblioteca.business.domain.enumeration.Rol;
import com.is.biblioteca.business.persistence.repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existe un usuario admin
        Optional<Usuario> opt = usuarioRepository.buscarUsuarioPorEmail("admin@gmail.com");
        Usuario adminExistente = opt.orElse(null);

        if (adminExistente == null) {
            // Crear usuario ADMIN
            Usuario admin = new Usuario();
            admin.setId(UUID.randomUUID().toString());
            admin.setNombre("admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol(Rol.ADMIN);
            admin.setEliminado(false);
            
            usuarioRepository.save(admin);
            
            System.out.println("========================================");
            System.out.println("Usuario ADMIN creado exitosamente!");
            System.out.println("Email: admin@gmail.com");
            System.out.println("Password: admin123");
            System.out.println("========================================");
        } else {
            System.out.println("========================================");
            System.out.println("Usuario ADMIN ya existe en la base de datos");
            System.out.println("Email: admin@gmail.com");
            System.out.println("========================================");
        }
    }
}
