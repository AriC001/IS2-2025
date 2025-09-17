package com.sport.proyecto;

import com.sport.proyecto.entidades.Empleado;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.enums.tipoEmpleado;
import com.sport.proyecto.enums.Rol;
import com.sport.proyecto.repositorios.PersonaRepositorio;
import com.sport.proyecto.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InicializadorDatos implements CommandLineRunner {
    @Autowired
    private PersonaRepositorio personaRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Buscamos si ya existe un empleado con ID = 1 (el admin)
        boolean usuarioAdminExiste = usuarioRepositorio.findById(1L).isPresent();

        if (!usuarioAdminExiste) {
            // Creamos un Empleado admin
            // Le asignamos un usuario
            Usuario usuarioAdmin = Usuario.builder()
                    .nombreUsuario("admin")
                    .clave("admin123") // idealmente cifrada
                    .rol(Rol.ADMIN) // si tenés un enum Rol
                    .eliminado(false)
                    .build();

            usuarioRepositorio.save(usuarioAdmin);

            System.out.println("Admin creado con ID = 1");
        } else {
            System.out.println("El admin ya existe.");
        }
    }
}
