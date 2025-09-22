package com.sport.proyecto;

import com.sport.proyecto.entidades.*;
import com.sport.proyecto.enums.tipoEmpleado;
import com.sport.proyecto.enums.Rol;
import com.sport.proyecto.repositorios.PersonaRepositorio;
import com.sport.proyecto.repositorios.UsuarioRepositorio;
import com.sport.proyecto.repositorios.PaisRepositorio;
import com.sport.proyecto.repositorios.ProvinciaRepositorio;
import com.sport.proyecto.repositorios.DepartamentoRepositorio;
import com.sport.proyecto.repositorios.LocalidadRepositorio;
import com.sport.proyecto.servicios.PaisServicio;
import com.sport.proyecto.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class InicializadorDatos implements CommandLineRunner {
    @Autowired
    private PersonaRepositorio personaRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private PaisRepositorio paisRepositorio;
    @Autowired
    private ProvinciaRepositorio provinciaRepositorio;
    @Autowired
    private DepartamentoRepositorio departamentoRepositorio;
    @Autowired
    private LocalidadRepositorio localidadRepositorio;
    @Autowired
    private PaisServicio paisServicio;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Buscamos si ya existe un empleado con ID = 1 (el admin)
        Usuario usuarioAdminExiste = usuarioRepositorio.findByUsername("admin");

        if (usuarioAdminExiste == null) {
            Usuario usuarioAdmin = usuarioServicio.crearUsuario("admin", "admin123", Rol.EMPLEADO_ADMINISTRATIVO);
            usuarioRepositorio.save(usuarioAdmin);
            System.out.println("Admin creado");
        } else {
            System.out.println("El admin ya existe.");
        }
        // ================== DATOS GEOGRÁFICOS =====================
        // Solo insertar si no existen
        if (paisRepositorio.findByName("Argentina") == null) {
            // PAÍS
            Pais argentina = Pais.builder().id(UUID.randomUUID().toString()).nombre("Argentina").eliminado(false).build();
            paisRepositorio.save(argentina);
            // PROVINCIAS
            Provincia mendoza = Provincia.builder().id(UUID.randomUUID().toString()).nombre("Mendoza").pais(argentina).eliminado(false).build();
            Provincia cordoba = Provincia.builder().id(UUID.randomUUID().toString()).nombre("Cordoba").pais(argentina).eliminado(false).build();
            provinciaRepositorio.save(mendoza);
            provinciaRepositorio.save(cordoba);
            // DEPARTAMENTOS
            Departamento guaymallen = Departamento.builder().id(UUID.randomUUID().toString()).nombre("Guaymallén").provincia(mendoza).eliminado(false).build();
            Departamento godoyCruz = Departamento.builder().id(UUID.randomUUID().toString()).nombre("Godoy Cruz").provincia(mendoza).eliminado(false).build();
            Departamento capitalCba = Departamento.builder().id(UUID.randomUUID().toString()).nombre("Capital").provincia(cordoba).eliminado(false).build();
            Departamento rioCuarto = Departamento.builder().id(UUID.randomUUID().toString()).nombre("Río Cuarto").provincia(cordoba).eliminado(false).build();
            departamentoRepositorio.save(guaymallen);
            departamentoRepositorio.save(godoyCruz);
            departamentoRepositorio.save(capitalCba);
            departamentoRepositorio.save(rioCuarto);
            // LOCALIDADES
            Localidad villaNueva = Localidad.builder().id(UUID.randomUUID().toString()).nombre("Villa Nueva").codigoPostal("5521").departamento(guaymallen).eliminado(false).build();
            Localidad dorrego = Localidad.builder().id(UUID.randomUUID().toString()).nombre("Dorrego").codigoPostal("5519").departamento(guaymallen).eliminado(false).build();
            Localidad bermejo = Localidad.builder().id(UUID.randomUUID().toString()).nombre("Bermejo").codigoPostal("5523").departamento(guaymallen).eliminado(false).build();
            localidadRepositorio.save(villaNueva);
            localidadRepositorio.save(dorrego);
            localidadRepositorio.save(bermejo);
            Localidad godoyCruzLoc = Localidad.builder().id(UUID.randomUUID().toString()).nombre("Godoy Cruz Centro").codigoPostal("5501").departamento(godoyCruz).eliminado(false).build();
            Localidad sanFrancisco = Localidad.builder().id(UUID.randomUUID().toString()).nombre("San Francisco del Monte").codigoPostal("5503").departamento(godoyCruz).eliminado(false).build();
            Localidad villaHipodromo = Localidad.builder().id(UUID.randomUUID().toString()).nombre("Villa Hipódromo").codigoPostal("5505").departamento(godoyCruz).eliminado(false).build();
            localidadRepositorio.save(godoyCruzLoc);
            localidadRepositorio.save(sanFrancisco);
            localidadRepositorio.save(villaHipodromo);
            Localidad nuevaCordoba = Localidad.builder().id(UUID.randomUUID().toString()).nombre("Nueva Córdoba").codigoPostal("5000").departamento(capitalCba).eliminado(false).build();
            Localidad alberdi = Localidad.builder().id(UUID.randomUUID().toString()).nombre("Alberdi").codigoPostal("5001").departamento(capitalCba).eliminado(false).build();
            Localidad generalPaz = Localidad.builder().id(UUID.randomUUID().toString()).nombre("General Paz").codigoPostal("5002").departamento(capitalCba).eliminado(false).build();
            localidadRepositorio.save(nuevaCordoba);
            localidadRepositorio.save(alberdi);
            localidadRepositorio.save(generalPaz);
            Localidad rioCuartoLoc = Localidad.builder().id(UUID.randomUUID().toString()).nombre("Río Cuarto Centro").codigoPostal("5800").departamento(rioCuarto).eliminado(false).build();
            Localidad lasHigueras = Localidad.builder().id(UUID.randomUUID().toString()).nombre("Las Higueras").codigoPostal("5803").departamento(rioCuarto).eliminado(false).build();
            Localidad holmberg = Localidad.builder().id(UUID.randomUUID().toString()).nombre("Holmberg").codigoPostal("5805").departamento(rioCuarto).eliminado(false).build();
            localidadRepositorio.save(rioCuartoLoc);
            localidadRepositorio.save(lasHigueras);
            localidadRepositorio.save(holmberg);
        }
    }
}
