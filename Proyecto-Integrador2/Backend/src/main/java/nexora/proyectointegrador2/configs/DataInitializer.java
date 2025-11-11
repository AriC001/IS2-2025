package nexora.proyectointegrador2.configs;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import nexora.proyectointegrador2.business.domain.entity.Alquiler;
import nexora.proyectointegrador2.business.domain.entity.CaracteristicaVehiculo;
import nexora.proyectointegrador2.business.domain.entity.Cliente;
import nexora.proyectointegrador2.business.domain.entity.CostoVehiculo;
import nexora.proyectointegrador2.business.domain.entity.Departamento;
import nexora.proyectointegrador2.business.domain.entity.Direccion;
import nexora.proyectointegrador2.business.domain.entity.Empleado;
import nexora.proyectointegrador2.business.domain.entity.Empresa;
import nexora.proyectointegrador2.business.domain.entity.Imagen;
import nexora.proyectointegrador2.business.domain.entity.Localidad;
import nexora.proyectointegrador2.business.domain.entity.Nacionalidad;
import nexora.proyectointegrador2.business.domain.entity.Pais;
import nexora.proyectointegrador2.business.domain.entity.Provincia;
import nexora.proyectointegrador2.business.domain.entity.Usuario;
import nexora.proyectointegrador2.business.domain.entity.Vehiculo;
import nexora.proyectointegrador2.business.enums.EstadoVehiculo;
import nexora.proyectointegrador2.business.enums.RolUsuario;
import nexora.proyectointegrador2.business.enums.TipoDocumentacion;
import nexora.proyectointegrador2.business.enums.TipoEmpleado;
import nexora.proyectointegrador2.business.enums.TipoImagen;
import nexora.proyectointegrador2.business.logic.service.AlquilerService;
import nexora.proyectointegrador2.business.logic.service.CaracteristicaVehiculoService;
import nexora.proyectointegrador2.business.logic.service.ClienteService;
import nexora.proyectointegrador2.business.logic.service.CostoVehiculoService;
import nexora.proyectointegrador2.business.logic.service.DepartamentoService;
import nexora.proyectointegrador2.business.logic.service.DireccionService;
import nexora.proyectointegrador2.business.logic.service.EmpleadoService;
import nexora.proyectointegrador2.business.logic.service.EmpresaService;
import nexora.proyectointegrador2.business.logic.service.ImagenService;
import nexora.proyectointegrador2.business.logic.service.LocalidadService;
import nexora.proyectointegrador2.business.logic.service.NacionalidadService;
import nexora.proyectointegrador2.business.logic.service.PaisService;
import nexora.proyectointegrador2.business.logic.service.ProvinciaService;
import nexora.proyectointegrador2.business.logic.service.VehiculoService;
import nexora.proyectointegrador2.business.persistence.repository.AlquilerRepository;
import nexora.proyectointegrador2.business.persistence.repository.ClienteRepository;
import nexora.proyectointegrador2.business.persistence.repository.DepartamentoRepository;
import nexora.proyectointegrador2.business.persistence.repository.DireccionRepository;
import nexora.proyectointegrador2.business.persistence.repository.EmpleadoRepository;
import nexora.proyectointegrador2.business.persistence.repository.EmpresaRepository;
import nexora.proyectointegrador2.business.persistence.repository.LocalidadRepository;
import nexora.proyectointegrador2.business.persistence.repository.NacionalidadRepository;
import nexora.proyectointegrador2.business.persistence.repository.PaisRepository;
import nexora.proyectointegrador2.business.persistence.repository.ProvinciaRepository;
import nexora.proyectointegrador2.business.persistence.repository.UsuarioRepository;
import nexora.proyectointegrador2.business.persistence.repository.VehiculoRepository;

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
                                        LocalidadRepository localidadRepository,
                                        DireccionService direccionService,
                                        DireccionRepository direccionRepository,
                                        NacionalidadService nacionalidadService,
                                        NacionalidadRepository nacionalidadRepository,
                                        EmpleadoService empleadoService,
                                        EmpleadoRepository empleadoRepository,
                                        ClienteService clienteService,
                                        ClienteRepository clienteRepository,
                                        VehiculoService vehiculoService,
                                        VehiculoRepository vehiculoRepository,
                                        CaracteristicaVehiculoService caracteristicaVehiculoService,
                                        CostoVehiculoService costoVehiculoService,
                                        ImagenService imagenService,
                                        EmpresaService empresaService,
                                        EmpresaRepository empresaRepository,
                                        AlquilerService alquilerService,
                                        AlquilerRepository alquilerRepository) {
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

      //5.Crear un auto si no existe
      /*if(!vehiculoRepository.existsById("1")){
        CostoVehiculo cost1 = CostoVehiculo.builder()
                .costo(Double.valueOf("5000"))
                // fechaDesde: ahora
                .fechaDesde(Date.from(Instant.now()))
                // fechaHasta: 30 días desde ahora (ajustable según necesidad)
                .fechaHasta(Date.from(Instant.now().plus(30, ChronoUnit.DAYS))).build();
        costoVehiculoRepository.save(cost1);
        CaracteristicaVehiculo char1 = CaracteristicaVehiculo.builder()
                .anio(2000)
                .cantidadAsiento(5)
                .cantidadPuerta(4)
                .cantidadTotalVehiculo(2)
                
                .costoVehiculo(cost1)
                .marca("BMW")
                .modelo("1").build();
        caracteristicaVehiculoRepository.save(char1);
        Vehiculo v = Vehiculo.builder()
          .patente("AA123AA")
          .caracteristicaVehiculo(char1)
          .estadoVehiculo(EstadoVehiculo.DISPONIBLE)
          .build();

        // Guardar el vehículo creado
        vehiculoRepository.save(v);
      }*/
      
      // Inicializar datos de prueba
      initDatosPrueba(direccionService, direccionRepository, nacionalidadService, nacionalidadRepository,
                     empleadoService, empleadoRepository, clienteService, clienteRepository,
                     vehiculoService, vehiculoRepository, caracteristicaVehiculoService, costoVehiculoService,
                     imagenService, empresaService, empresaRepository, alquilerService, alquilerRepository,
                     localidadRepository, usuarioRepository, passwordEncoder);
      
    };
  }

  private void initDatosGeograficos( PaisService paisService, 
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

  private void initDatosPrueba(DireccionService direccionService,
                               DireccionRepository direccionRepository,
                               NacionalidadService nacionalidadService,
                               NacionalidadRepository nacionalidadRepository,
                               EmpleadoService empleadoService,
                               EmpleadoRepository empleadoRepository,
                               ClienteService clienteService,
                               ClienteRepository clienteRepository,
                               VehiculoService vehiculoService,
                               VehiculoRepository vehiculoRepository,
                               CaracteristicaVehiculoService caracteristicaVehiculoService,
                               CostoVehiculoService costoVehiculoService,
                               ImagenService imagenService,
                               EmpresaService empresaService,
                               EmpresaRepository empresaRepository,
                               AlquilerService alquilerService,
                               AlquilerRepository alquilerRepository,
                               LocalidadRepository localidadRepository,
                               UsuarioRepository usuarioRepository,
                               PasswordEncoder passwordEncoder) {
    try {
      logger.info("🔄 Iniciando carga de datos de prueba...");

      // 1. Crear Nacionalidad Argentina
      Nacionalidad argentina = nacionalidadRepository.findByNombreAndEliminadoFalse("Argentina")
          .orElse(null);
      if (argentina == null) {
        argentina = Nacionalidad.builder()
            .nombre("Argentina")
            .build();
        argentina.setEliminado(false);
        argentina = nacionalidadService.save(argentina);
        logger.info("✅ Nacionalidad 'Argentina' creada exitosamente");
      } else {
        logger.info("✓ Nacionalidad 'Argentina' ya existe");
      }

      // 2. Crear 2 Direcciones compartidas
      Localidad godoycruz = localidadRepository.findByNombreAndEliminadoFalse("Godoy Cruz")
          .orElseThrow(() -> new Exception("Localidad Godoy Cruz no encontrada"));
      Localidad lasHeras = localidadRepository.findByNombreAndEliminadoFalse("Las Heras")
          .orElseThrow(() -> new Exception("Localidad Las Heras no encontrada"));

      Direccion direccion1 = direccionRepository.findAll().stream()
          .filter(d -> !d.isEliminado() && "San Martín".equals(d.getCalle()) && "1234".equals(d.getNumero()))
          .findFirst()
          .orElse(null);
      
      if (direccion1 == null) {
        direccion1 = Direccion.builder()
            .calle("San Martín")
            .numero("1234")
            .barrio("Centro")
            .localidad(godoycruz)
            .build();
        direccion1.setEliminado(false);
        direccion1 = direccionService.save(direccion1);
        logger.info("✅ Dirección 1 'San Martín 1234' creada exitosamente");
      } else {
        logger.info("✓ Dirección 1 ya existe");
      }

      Direccion direccion2 = direccionRepository.findAll().stream()
          .filter(d -> !d.isEliminado() && "Belgrano".equals(d.getCalle()) && "567".equals(d.getNumero()))
          .findFirst()
          .orElse(null);
      
      if (direccion2 == null) {
        direccion2 = Direccion.builder()
            .calle("Belgrano")
            .numero("567")
            .barrio("El Plumerillo")
            .localidad(lasHeras)
            .build();
        direccion2.setEliminado(false);
        direccion2 = direccionService.save(direccion2);
        logger.info("✅ Dirección 2 'Belgrano 567' creada exitosamente");
      } else {
        logger.info("✓ Dirección 2 ya existe");
      }

      // 3. Crear 2 Empleados
      if (empleadoRepository.findByNumeroDocumentoAndEliminadoFalse("30123456").isEmpty()) {
        Usuario usuarioEmpleado1 = usuarioRepository.findByNombreUsuario("empleado1").orElse(null);
        if (usuarioEmpleado1 == null) {
          usuarioEmpleado1 = Usuario.builder()
              .nombreUsuario("empleado1")
              .clave(passwordEncoder.encode("empleado123"))
              .rol(RolUsuario.JEFE)
              .build();
          usuarioEmpleado1.setEliminado(false);
          usuarioEmpleado1 = usuarioRepository.save(usuarioEmpleado1);
        }

        Empleado empleado1 = new Empleado();
        empleado1.setNombre("Carlos");
        empleado1.setApellido("Rodríguez");
        empleado1.setFechaNacimiento(new Date(90, 0, 15)); // 15/01/1990
        empleado1.setTipoDocumento(TipoDocumentacion.DNI);
        empleado1.setNumeroDocumento("30123456");
        empleado1.setTipoEmpleado(TipoEmpleado.JEFE);
        empleado1.setUsuario(usuarioEmpleado1);
        empleado1.setDireccion(direccion1);
        empleado1.setEliminado(false);
        empleadoService.save(empleado1);
        logger.info("✅ Empleado 1 'Carlos Rodríguez' (JEFE) creado exitosamente");
      } else {
        logger.info("✓ Empleado 1 ya existe");
      }

      if (empleadoRepository.findByNumeroDocumentoAndEliminadoFalse("31234567").isEmpty()) {
        Usuario usuarioEmpleado2 = usuarioRepository.findByNombreUsuario("empleado2").orElse(null);
        if (usuarioEmpleado2 == null) {
          usuarioEmpleado2 = Usuario.builder()
              .nombreUsuario("empleado2")
              .clave(passwordEncoder.encode("empleado123"))
              .rol(RolUsuario.ADMINISTRATIVO)
              .build();
          usuarioEmpleado2.setEliminado(false);
          usuarioEmpleado2 = usuarioRepository.save(usuarioEmpleado2);
        }

        Empleado empleado2 = new Empleado();
        empleado2.setNombre("María");
        empleado2.setApellido("González");
        empleado2.setFechaNacimiento(new Date(92, 5, 20)); // 20/06/1992
        empleado2.setTipoDocumento(TipoDocumentacion.DNI);
        empleado2.setNumeroDocumento("31234567");
        empleado2.setTipoEmpleado(TipoEmpleado.ADMINISTRATIVO);
        empleado2.setUsuario(usuarioEmpleado2);
        empleado2.setDireccion(direccion2);
        empleado2.setEliminado(false);
        empleadoService.save(empleado2);
        logger.info("✅ Empleado 2 'María González' (ADMINISTRATIVO) creado exitosamente");
      } else {
        logger.info("✓ Empleado 2 ya existe");
      }

      // 4. Crear 2 Clientes
      Cliente cliente1 = null;
      if (clienteRepository.findByNumeroDocumentoAndEliminadoFalse("32345678").isEmpty()) {
        cliente1 = new Cliente();
        cliente1.setNombre("Juan");
        cliente1.setApellido("Pérez");
        cliente1.setFechaNacimiento(new Date(88, 2, 10)); // 10/03/1988
        cliente1.setTipoDocumento(TipoDocumentacion.DNI);
        cliente1.setNumeroDocumento("32345678");
        cliente1.setDireccionEstadia("Hotel Plaza, Habitación 305");
        cliente1.setNacionalidad(argentina);
        cliente1.setDireccion(direccion1);
        cliente1.setEliminado(false);
        cliente1 = clienteService.save(cliente1);
        logger.info("✅ Cliente 1 'Juan Pérez' creado exitosamente");
      } else {
        cliente1 = clienteRepository.findByNumeroDocumentoAndEliminadoFalse("32345678").get();
        logger.info("✓ Cliente 1 ya existe");
      }

      Cliente cliente2 = null;
      if (clienteRepository.findByNumeroDocumentoAndEliminadoFalse("33456789").isEmpty()) {
        cliente2 = new Cliente();
        cliente2.setNombre("Ana");
        cliente2.setApellido("Martínez");
        cliente2.setFechaNacimiento(new Date(95, 7, 25)); // 25/08/1995
        cliente2.setTipoDocumento(TipoDocumentacion.DNI);
        cliente2.setNumeroDocumento("33456789");
        cliente2.setDireccionEstadia("Hostel Mendoza, Cama 12");
        cliente2.setNacionalidad(argentina);
        cliente2.setDireccion(direccion2);
        cliente2.setEliminado(false);
        cliente2 = clienteService.save(cliente2);
        logger.info("✅ Cliente 2 'Ana Martínez' creado exitosamente");
      } else {
        cliente2 = clienteRepository.findByNumeroDocumentoAndEliminadoFalse("33456789").get();
        logger.info("✓ Cliente 2 ya existe");
      }

      // 5. Crear 2 Vehículos con características y costos
      Vehiculo vehiculo1 = null;
      if (vehiculoRepository.findByPatenteAndEliminadoFalse("AB123CD").isEmpty()) {
        // Crear imagen de vehículo 1
        byte[] imagenBytes1 = "imagen-vehiculo-1-placeholder".getBytes();
        Imagen imagenVehiculo1 = Imagen.builder()
            .nombre("vehiculo1.jpg")
            .mime("image/jpeg")
            .contenido(imagenBytes1)
            .tipoImagen(TipoImagen.VEHICULO)
            .build();
        imagenVehiculo1.setEliminado(false);
        imagenVehiculo1 = imagenService.save(imagenVehiculo1);

        // Crear costo de vehículo 1
        CostoVehiculo costo1 = CostoVehiculo.builder()
            .fechaDesde(new Date(125, 0, 1)) // 01/01/2025
            .fechaHasta(new Date(125, 11, 31)) // 31/12/2025
            .costo(5000.0)
            .build();
        costo1.setEliminado(false);
        costo1 = costoVehiculoService.save(costo1);

    // Crear característica de vehículo 1
    CaracteristicaVehiculo caracteristica1 = CaracteristicaVehiculo.builder()
      .marca("Toyota")
      .modelo("Corolla")
      .anio(2020)
      .cantidadPuerta(4)
      .cantidadAsiento(5)
      
      .imagenVehiculo(imagenVehiculo1)
      .costoVehiculo(costo1)
      .build();
        caracteristica1.setEliminado(false);
        caracteristica1 = caracteristicaVehiculoService.save(caracteristica1);

        // Crear vehículo 1
        vehiculo1 = Vehiculo.builder()
            .patente("AB123CD")
            .estadoVehiculo(EstadoVehiculo.DISPONIBLE)
            .caracteristicaVehiculo(caracteristica1)
            .build();
        vehiculo1.setEliminado(false);
        vehiculo1 = vehiculoService.save(vehiculo1);
        logger.info("✅ Vehículo 1 'Toyota Corolla AB123CD' creado exitosamente");
      } else {
        vehiculo1 = vehiculoRepository.findByPatenteAndEliminadoFalse("AB123CD").get();
        logger.info("✓ Vehículo 1 ya existe");
      }

      Vehiculo vehiculo2 = null;
      if (vehiculoRepository.findByPatenteAndEliminadoFalse("EF456GH").isEmpty()) {
        // Crear imagen de vehículo 2
        byte[] imagenBytes2 = "imagen-vehiculo-2-placeholder".getBytes();
        Imagen imagenVehiculo2 = Imagen.builder()
            .nombre("vehiculo2.jpg")
            .mime("image/jpeg")
            .contenido(imagenBytes2)
            .tipoImagen(TipoImagen.VEHICULO)
            .build();
        imagenVehiculo2.setEliminado(false);
        imagenVehiculo2 = imagenService.save(imagenVehiculo2);

        // Crear costo de vehículo 2
        CostoVehiculo costo2 = CostoVehiculo.builder()
            .fechaDesde(new Date(125, 0, 1)) // 01/01/2025
            .fechaHasta(new Date(125, 11, 31)) // 31/12/2025
            .costo(7000.0)
            .build();
        costo2.setEliminado(false);
        costo2 = costoVehiculoService.save(costo2);

    // Crear característica de vehículo 2
    CaracteristicaVehiculo caracteristica2 = CaracteristicaVehiculo.builder()
      .marca("Honda")
      .modelo("Civic")
      .anio(2021)
      .cantidadPuerta(4)
      .cantidadAsiento(5)
      
      .imagenVehiculo(imagenVehiculo2)
      .costoVehiculo(costo2)
      .build();
        caracteristica2.setEliminado(false);
        caracteristica2 = caracteristicaVehiculoService.save(caracteristica2);

        // Crear vehículo 2
        vehiculo2 = Vehiculo.builder()
            .patente("EF456GH")
            .estadoVehiculo(EstadoVehiculo.DISPONIBLE)
            .caracteristicaVehiculo(caracteristica2)
            .build();
        vehiculo2.setEliminado(false);
        vehiculo2 = vehiculoService.save(vehiculo2);
        logger.info("✅ Vehículo 2 'Honda Civic EF456GH' creado exitosamente");
      } else {
        vehiculo2 = vehiculoRepository.findByPatenteAndEliminadoFalse("EF456GH").get();
        logger.info("✓ Vehículo 2 ya existe");
      }

      // 6. Crear 2 Empresas
      if (empresaRepository.findByEmailAndEliminadoFalse("contacto@rentacar1.com").isEmpty()) {
        Empresa empresa1 = Empresa.builder()
            .nombre("RentaCar Mendoza")
            .telefono("261-4567890")
            .email("contacto@rentacar1.com")
            .direccion(direccion1)
            .build();
        empresa1.setEliminado(false);
        empresaService.save(empresa1);
        logger.info("✅ Empresa 1 'RentaCar Mendoza' creada exitosamente");
      } else {
        logger.info("✓ Empresa 1 ya existe");
      }

      if (empresaRepository.findByEmailAndEliminadoFalse("info@autosmendoza.com").isEmpty()) {
        Empresa empresa2 = Empresa.builder()
            .nombre("Autos Mendoza S.A.")
            .telefono("261-7654321")
            .email("info@autosmendoza.com")
            .direccion(direccion2)
            .build();
        empresa2.setEliminado(false);
        empresaService.save(empresa2);
        logger.info("✅ Empresa 2 'Autos Mendoza S.A.' creada exitosamente");
      } else {
        logger.info("✓ Empresa 2 ya existe");
      }

      // 7. Crear 2 Alquileres
      if (alquilerRepository.count() == 0) {
        Alquiler alquiler1 = Alquiler.builder()
            .fechaDesde(new Date(125, 0, 10)) // 10/01/2025
            .fechaHasta(new Date(125, 0, 15)) // 15/01/2025
            .cliente(cliente1)
            .vehiculo(vehiculo1)
            .build();
        alquiler1.setEliminado(false);
        alquilerService.save(alquiler1);
        logger.info("✅ Alquiler 1 creado exitosamente (Juan Pérez - Toyota Corolla)");

        Alquiler alquiler2 = Alquiler.builder()
            .fechaDesde(new Date(125, 1, 1)) // 01/02/2025
            .fechaHasta(new Date(125, 1, 7)) // 07/02/2025
            .cliente(cliente2)
            .vehiculo(vehiculo2)
            .build();
        alquiler2.setEliminado(false);
        alquilerService.save(alquiler2);
        logger.info("✅ Alquiler 2 creado exitosamente (Ana Martínez - Honda Civic)");
      } else {
        logger.info("✓ Alquileres ya existen");
      }

      logger.info("✅ Datos de prueba inicializados correctamente");

    } catch (Exception e) {
      logger.error("❌ Error al inicializar datos de prueba: {}", e.getMessage());
      e.printStackTrace();
    }
  }

}
