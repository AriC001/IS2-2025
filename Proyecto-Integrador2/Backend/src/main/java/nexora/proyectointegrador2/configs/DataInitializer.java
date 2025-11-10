package nexora.proyectointegrador2.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import nexora.proyectointegrador2.business.domain.entity.Departamento;
import nexora.proyectointegrador2.business.domain.entity.Localidad;
import nexora.proyectointegrador2.business.domain.entity.Pais;
import nexora.proyectointegrador2.business.domain.entity.Provincia;
import nexora.proyectointegrador2.business.domain.entity.Usuario;
import nexora.proyectointegrador2.business.enums.RolUsuario;
import nexora.proyectointegrador2.business.logic.service.DepartamentoService;
import nexora.proyectointegrador2.business.logic.service.LocalidadService;
import nexora.proyectointegrador2.business.logic.service.PaisService;
import nexora.proyectointegrador2.business.logic.service.ProvinciaService;
import nexora.proyectointegrador2.business.persistence.repository.DepartamentoRepository;
import nexora.proyectointegrador2.business.persistence.repository.LocalidadRepository;
import nexora.proyectointegrador2.business.persistence.repository.PaisRepository;
import nexora.proyectointegrador2.business.persistence.repository.ProvinciaRepository;
import nexora.proyectointegrador2.business.persistence.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

  private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

  @Bean
  public CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, 
                                        PasswordEncoder passwordEncoder,
                                        PaisService paisService,
                                        ProvinciaService provinciaService,
                                        DepartamentoService departamentoService,
                                        LocalidadService localidadService,
                                        PaisRepository paisRepository,
                                        ProvinciaRepository provinciaRepository,
                                        DepartamentoRepository departamentoRepository,
                                        LocalidadRepository localidadRepository) {
    return args -> {
      // Verificar si ya existe un usuario admin
      if (usuarioRepository.findByNombreUsuario("admin").isEmpty()) {
        Usuario admin = Usuario.builder()
            .nombreUsuario("admin")
            .clave(passwordEncoder.encode("admin123"))
            .rol(RolUsuario.JEFE)
            .build();
        admin.setEliminado(false);
        
        usuarioRepository.save(admin);
        logger.info("✅ Usuario JEFE creado exitosamente");
        logger.info("   Usuario: admin");
        logger.info("   Contraseña: admin123");
        logger.info("   Rol: JEFE");
      } else {
        logger.info("✓ Usuario admin ya existe en la base de datos");
      }

      // Crear usuario administrativo si no existe
      if (usuarioRepository.findByNombreUsuario("operador").isEmpty()) {
        Usuario operador = Usuario.builder()
            .nombreUsuario("operador")
            .clave(passwordEncoder.encode("operador123"))
            .rol(RolUsuario.ADMINISTRATIVO)
            .build();
        operador.setEliminado(false);
        
        usuarioRepository.save(operador);
        logger.info("✅ Usuario ADMINISTRATIVO creado exitosamente");
        logger.info("   Usuario: operador");
        logger.info("   Contraseña: operador123");
        logger.info("   Rol: ADMINISTRATIVO");
      } else {
        logger.info("✓ Usuario operador ya existe en la base de datos");
      }

      if (usuarioRepository.findByNombreUsuario("cliente").isEmpty()) {
        Usuario cliente = Usuario.builder()
                .nombreUsuario("cliente")
                .clave(passwordEncoder.encode("cliente123"))
                .rol(RolUsuario.CLIENTE)
                .build();
        cliente.setEliminado(false);

        usuarioRepository.save(cliente);
        logger.info("✅ Usuario CLIENTE creado exitosamente");
        logger.info("   Usuario: cliente");
        logger.info("   Contraseña: cliente123");
        logger.info("   Rol: CLIENTE");
      } else {
        logger.info("✓ Usuario cliente ya existe en la base de datos");
      }
      
      // Inicializar datos geográficos de Mendoza, Argentina
      initDatosGeograficos(paisService, provinciaService, departamentoService, localidadService,
                          paisRepository, provinciaRepository, departamentoRepository, localidadRepository);
      
    };
  }

  private void initDatosGeograficos(PaisService paisService, 
                                     ProvinciaService provinciaService,
                                     DepartamentoService departamentoService,
                                     LocalidadService localidadService,
                                     PaisRepository paisRepository,
                                     ProvinciaRepository provinciaRepository,
                                     DepartamentoRepository departamentoRepository,
                                     LocalidadRepository localidadRepository) {
    try {
      // 1. Crear País: Argentina
      Pais argentina = paisRepository.findByNombreAndEliminadoFalse("Argentina")
          .orElse(null);
      
      if (argentina == null) {
        argentina = Pais.builder()
            .nombre("Argentina")
            .build();
        argentina.setEliminado(false);
        argentina = paisService.save(argentina);
        logger.info("✅ País 'Argentina' creado exitosamente");
      } else {
        logger.info("✓ País 'Argentina' ya existe en la base de datos");
      }

      // 2. Crear Provincia: Mendoza
      Provincia mendoza = provinciaRepository.findByNombreAndEliminadoFalse("Mendoza")
          .orElse(null);
      
      if (mendoza == null) {
        mendoza = Provincia.builder()
            .nombre("Mendoza")
            .pais(argentina)
            .build();
        mendoza.setEliminado(false);
        mendoza = provinciaService.save(mendoza);
        logger.info("✅ Provincia 'Mendoza' creada exitosamente");
      } else {
        logger.info("✓ Provincia 'Mendoza' ya existe en la base de datos");
      }

      // 3. Crear Departamentos de Mendoza
      String[] departamentosMendoza = {
          "Capital", "Godoy Cruz", "Las Heras", "Guaymallén", 
          "Luján de Cuyo", "Maipú", "San Martín", "Rivadavia"
      };

      for (String nombreDepto : departamentosMendoza) {
        Departamento depto = departamentoRepository
            .findByNombreAndEliminadoFalse(nombreDepto)
            .orElse(null);
        
        if (depto == null) {
          depto = Departamento.builder()
              .nombre(nombreDepto)
              .provincia(mendoza)
              .build();
          depto.setEliminado(false);
          depto = departamentoService.save(depto);
          logger.info("✅ Departamento '{}' creado exitosamente", nombreDepto);
        } else {
          logger.info("✓ Departamento '{}' ya existe en la base de datos", nombreDepto);
        }

        // 4. Crear Localidades según el departamento
        crearLocalidadesPorDepartamento(depto, localidadService, localidadRepository);
      }

      logger.info("✅ Datos geográficos de Mendoza, Argentina inicializados correctamente");
      
    } catch (Exception e) {
      logger.error("❌ Error al inicializar datos geográficos: {}", e.getMessage());
      e.printStackTrace();
    }
  }

  private void crearLocalidadesPorDepartamento(Departamento departamento, 
                                               LocalidadService localidadService,
                                               LocalidadRepository localidadRepository) {
    String nombreDepto = departamento.getNombre();
    
    try {
      switch (nombreDepto) {
        case "Capital":
          crearLocalidadSiNoExiste("Ciudad de Mendoza", "M5500", departamento, localidadService, localidadRepository);
          crearLocalidadSiNoExiste("San José", "M5501", departamento, localidadService, localidadRepository);
          break;
          
        case "Godoy Cruz":
          crearLocalidadSiNoExiste("Godoy Cruz", "M5501", departamento, localidadService, localidadRepository);
          crearLocalidadSiNoExiste("Villa Hipódromo", "M5501", departamento, localidadService, localidadRepository);
          break;
          
        case "Las Heras":
          crearLocalidadSiNoExiste("Las Heras", "M5539", departamento, localidadService, localidadRepository);
          crearLocalidadSiNoExiste("El Plumerillo", "M5539", departamento, localidadService, localidadRepository);
          crearLocalidadSiNoExiste("El Borbollón", "M5539", departamento, localidadService, localidadRepository);
          break;
          
        case "Guaymallén":
          crearLocalidadSiNoExiste("Villa Nueva", "M5521", departamento, localidadService, localidadRepository);
          crearLocalidadSiNoExiste("San José", "M5521", departamento, localidadService, localidadRepository);
          crearLocalidadSiNoExiste("Rodeo del Medio", "M5521", departamento, localidadService, localidadRepository);
          crearLocalidadSiNoExiste("Villa Tulumaya", "M5521", departamento, localidadService, localidadRepository);
          break;
          
        case "Luján de Cuyo":
          crearLocalidadSiNoExiste("Luján de Cuyo", "M5507", departamento, localidadService, localidadRepository);
          crearLocalidadSiNoExiste("Chacras de Coria", "M5507", departamento, localidadService, localidadRepository);
          crearLocalidadSiNoExiste("Vistalba", "M5507", departamento, localidadService, localidadRepository);
          crearLocalidadSiNoExiste("Perdriel", "M5507", departamento, localidadService, localidadRepository);
          break;
          
        case "Maipú":
          crearLocalidadSiNoExiste("Maipú", "M5515", departamento, localidadService, localidadRepository);
          crearLocalidadSiNoExiste("Rodeo del Medio", "M5515", departamento, localidadService, localidadRepository);
          crearLocalidadSiNoExiste("Coquimbito", "M5515", departamento, localidadService, localidadRepository);
          crearLocalidadSiNoExiste("Lunlunta", "M5515", departamento, localidadService, localidadRepository);
          break;
          
        case "San Martín":
          crearLocalidadSiNoExiste("San Martín", "M5570", departamento, localidadService, localidadRepository);
          crearLocalidadSiNoExiste("La Colonia", "M5570", departamento, localidadService, localidadRepository);
          crearLocalidadSiNoExiste("Montecaseros", "M5570", departamento, localidadService, localidadRepository);
          break;
          
        case "Rivadavia":
          crearLocalidadSiNoExiste("Rivadavia", "M5579", departamento, localidadService, localidadRepository);
          crearLocalidadSiNoExiste("La Central", "M5579", departamento, localidadService, localidadRepository);
          crearLocalidadSiNoExiste("Andrade", "M5579", departamento, localidadService, localidadRepository);
          break;
      }
    } catch (Exception e) {
      logger.error("Error al crear localidades para el departamento {}: {}", nombreDepto, e.getMessage());
    }
  }

  private void crearLocalidadSiNoExiste(String nombre, String codigoPostal, 
                                        Departamento departamento, 
                                        LocalidadService localidadService,
                                        LocalidadRepository localidadRepository) {
    try {
      Localidad localidad = localidadRepository
          .findByNombreAndEliminadoFalse(nombre)
          .orElse(null);
      
      if (localidad == null) {
        localidad = Localidad.builder()
            .nombre(nombre)
            .codigoPostal(codigoPostal)
            .departamento(departamento)
            .build();
        localidad.setEliminado(false);
        localidadService.save(localidad);
        logger.debug("✅ Localidad '{}' (CP: {}) creada en departamento '{}'", 
            nombre, codigoPostal, departamento.getNombre());
      }
    } catch (Exception e) {
      logger.warn("⚠ No se pudo crear la localidad '{}': {}", nombre, e.getMessage());
    }
  }

}
