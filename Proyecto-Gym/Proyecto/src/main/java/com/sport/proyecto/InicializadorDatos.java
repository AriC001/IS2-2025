package com.sport.proyecto;

import com.sport.proyecto.entidades.*;
import com.sport.proyecto.enums.tipoDocumento;
import com.sport.proyecto.enums.tipoEmpleado;
import com.sport.proyecto.enums.Rol;
import com.sport.proyecto.repositorios.*;
import com.sport.proyecto.servicios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

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
    @Autowired
    private ValorCuotaRepositorio valorCuotaRepositorio;
    @Autowired
    private SocioRepositorio socioRepositorio;
    @Autowired
    private PersonaServicio personaServicio;
    @Autowired
    private CuotaMensualServicio cuotaMensualServicio;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Buscamos si ya existe un empleado con ID = 1 (el admin)
        // ------------------- ADMIN ---------------------
        Optional<Usuario> usuarioAdminExiste = usuarioRepositorio.findByUsername("admin");
        if (!usuarioAdminExiste.isPresent()) {
            Usuario usuarioAdmin = usuarioServicio.crearUsuario("admin", "admin123", Rol.ADMIN);
            // Crear la persona asociada al admin si tu sistema lo requiere
            Persona adminPersona = Empleado.builder()
                    .nombre("Admin")
                    .apellido("Sistema")
                    .email("admin@sistema.com")
                    .clave("admin123") // o encriptar si querés
                    .usuario(usuarioAdmin)
                    .eliminado(false)
                    .tipoEmpleado(tipoEmpleado.ADMINISTRATIVO)
                    .build();
            personaRepositorio.save(adminPersona);
            System.out.println("Admin creado con ID = " + usuarioAdmin.getId());
        }

        // ================== DATOS GEOGRÁFICOS =====================
        // Solo insertar si no existen
        if (paisRepositorio.findByName("Argentina") == null) {
            // CARGA DE DATOS GEOGRÁFICOS DE PRUEBA

            // PAÍS
            Pais argentina = Pais.builder().nombre("Argentina").eliminado(false).build();
            paisRepositorio.save(argentina);

            // PROVINCIAS
            Provincia mendoza = Provincia.builder().nombre("Mendoza").pais(argentina).eliminado(false).build();
            Provincia cordoba = Provincia.builder().nombre("Cordoba").pais(argentina).eliminado(false).build();
            provinciaRepositorio.save(mendoza);
            provinciaRepositorio.save(cordoba);

            // DEPARTAMENTOS
            Departamento guaymallen = Departamento.builder().nombre("Guaymallén").provincia(mendoza).eliminado(false).build();
            Departamento godoyCruz = Departamento.builder().nombre("Godoy Cruz").provincia(mendoza).eliminado(false).build();
            Departamento capitalCba = Departamento.builder().nombre("Capital").provincia(cordoba).eliminado(false).build();
            Departamento rioCuarto = Departamento.builder().nombre("Río Cuarto").provincia(cordoba).eliminado(false).build();
            departamentoRepositorio.save(guaymallen);
            departamentoRepositorio.save(godoyCruz);
            departamentoRepositorio.save(capitalCba);
            departamentoRepositorio.save(rioCuarto);

            // LOCALIDADES
            // Mendoza - Guaymallén
            Localidad villaNueva = Localidad.builder().nombre("Villa Nueva").codigoPostal("5521").departamento(guaymallen).eliminado(false).build();
            Localidad dorrego = Localidad.builder().nombre("Dorrego").codigoPostal("5519").departamento(guaymallen).eliminado(false).build();
            Localidad bermejo = Localidad.builder().nombre("Bermejo").codigoPostal("5523").departamento(guaymallen).eliminado(false).build();
            localidadRepositorio.save(villaNueva);
            localidadRepositorio.save(dorrego);
            localidadRepositorio.save(bermejo);
            // Mendoza - Godoy Cruz
            Localidad godoyCruzLoc = Localidad.builder().nombre("Godoy Cruz Centro").codigoPostal("5501").departamento(godoyCruz).eliminado(false).build();
            Localidad sanFrancisco = Localidad.builder().nombre("San Francisco del Monte").codigoPostal("5503").departamento(godoyCruz).eliminado(false).build();
            Localidad villaHipodromo = Localidad.builder().nombre("Villa Hipódromo").codigoPostal("5505").departamento(godoyCruz).eliminado(false).build();
            localidadRepositorio.save(godoyCruzLoc);
            localidadRepositorio.save(sanFrancisco);
            localidadRepositorio.save(villaHipodromo);
            // Córdoba - Capital
            Localidad nuevaCordoba = Localidad.builder().nombre("Nueva Córdoba").codigoPostal("5000").departamento(capitalCba).eliminado(false).build();
            Localidad alberdi = Localidad.builder().nombre("Alberdi").codigoPostal("5001").departamento(capitalCba).eliminado(false).build();
            Localidad generalPaz = Localidad.builder().nombre("General Paz").codigoPostal("5002").departamento(capitalCba).eliminado(false).build();
            localidadRepositorio.save(nuevaCordoba);
            localidadRepositorio.save(alberdi);
            localidadRepositorio.save(generalPaz);
            // Córdoba - Río Cuarto
            Localidad rioCuartoLoc = Localidad.builder().nombre("Río Cuarto Centro").codigoPostal("5800").departamento(rioCuarto).eliminado(false).build();
            Localidad lasHigueras = Localidad.builder().nombre("Las Higueras").codigoPostal("5803").departamento(rioCuarto).eliminado(false).build();
            Localidad holmberg = Localidad.builder().nombre("Holmberg").codigoPostal("5805").departamento(rioCuarto).eliminado(false).build();
            localidadRepositorio.save(rioCuartoLoc);
            localidadRepositorio.save(lasHigueras);
            localidadRepositorio.save(holmberg);


        }

        //Creamos El primero Valor de CUOTA
        if(!valorCuotaRepositorio.obtenerPrimerValorCuota().isPresent()){
            ValorCuota v = ValorCuota.builder().fechaDesde(LocalDate.now()).fechaHasta(LocalDate.now().plusMonths(3)).valor(100L).eliminado(false).build();
            valorCuotaRepositorio.save(v);
        }
        //creamos un socio
        if(socioRepositorio.findAllActiveSocios().size() == 0){
            Persona p = personaServicio.registro("A","C","ac@gmail.com","123456","123456",false,null);
            cuotaMensualServicio.generarCuotasMensuales();
        }

        // ------------------- USUARIOS DE PRUEBA ---------------------
        Optional<Usuario> usuarioSocio1Existe = usuarioRepositorio.findByUsername("Juan Massacesi");
        if (!usuarioSocio1Existe.isPresent()) {
            Usuario usuarioSocio1 = usuarioServicio.crearUsuario("Juan Massacesi", "socio123", Rol.SOCIO);
            Persona socio1 = Socio.builder()
                    .nombre("Juan")
                    .apellido("Massacesi")
                    .email("juanimassacesi17@gmail.com")
                    .clave("socio123")
                    .usuario(usuarioSocio1)
                    .eliminado(false)
                    .build();
            personaRepositorio.save(socio1);
        }
        Optional<Usuario> usuarioSocio2Existe = usuarioRepositorio.findByUsername("Ana García");
        if (!usuarioSocio2Existe.isPresent()) {
            Usuario usuarioSocio2 = usuarioServicio.crearUsuario("Ana García", "socio456", Rol.SOCIO);
            Persona socio2 = Socio.builder()
                    .nombre("Ana")
                    .apellido("García")
                    .email("ana.garcia@mail.com")
                    .clave("socio456")
                    .usuario(usuarioSocio2)
                    .eliminado(false)
                    .build();
            personaRepositorio.save(socio2);
        }
        Optional<Usuario> usuarioEmpleadoExiste = usuarioRepositorio.findByUsername("Carlos López");
        if (!usuarioEmpleadoExiste.isPresent()) {
            Usuario usuarioEmpleado = usuarioServicio.crearUsuario("Carlos López", "empleado123", Rol.EMPLEADO);
            Persona empleado1 = Empleado.builder()
                    .nombre("Carlos")
                    .apellido("López")
                    .email("carlos.lopez@mail.com")
                    .clave("empleado123")
                    .usuario(usuarioEmpleado)
                    .eliminado(false)
                    .tipoEmpleado(tipoEmpleado.PROFESOR)
                    .build();
            personaRepositorio.save(empleado1);
        }
    }
}
