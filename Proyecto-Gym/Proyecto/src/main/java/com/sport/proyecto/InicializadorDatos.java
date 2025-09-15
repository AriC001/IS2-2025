package com.sport.proyecto;

import com.sport.proyecto.entidades.Empleado;
import com.sport.proyecto.enums.tipoEmpleado;
import com.sport.proyecto.repositorios.PersonaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InicializadorDatos implements CommandLineRunner {
    @Autowired
    private PersonaRepositorio personaRepositorio;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Buscamos si ya existe un empleado con ID = 1 (el admin)
        boolean adminExiste = personaRepositorio.findById(1L).isPresent();

        if (!adminExiste) {
            // Creamos un Empleado admin
            Empleado admin = Empleado.builder()
                    .nombre("Admin")
                    .apellido("Admin")
                    .email("admin@admin.com")
                    .clave("admin123") // idealmente cifrada
                    .tipoEmpleado(tipoEmpleado.ADMIN)// si tenés un enum tipoEmpleado
                    .eliminado(false)
                    .build();

            personaRepositorio.save(admin);

            System.out.println("Admin creado con ID = 1");
        } else {
            System.out.println("El admin ya existe.");
        }
    }
}
