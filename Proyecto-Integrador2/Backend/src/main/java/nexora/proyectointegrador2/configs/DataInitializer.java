package nexora.proyectointegrador2.configs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import nexora.proyectointegrador2.business.domain.entity.ContactoCorreoElectronico;
import nexora.proyectointegrador2.business.domain.entity.ContactoTelefonico;
import nexora.proyectointegrador2.business.domain.entity.CostoVehiculo;
import nexora.proyectointegrador2.business.domain.entity.Departamento;
import nexora.proyectointegrador2.business.domain.entity.Direccion;
import nexora.proyectointegrador2.business.domain.entity.Empleado;
import nexora.proyectointegrador2.business.domain.entity.Empresa;
import nexora.proyectointegrador2.business.domain.entity.Imagen;
import nexora.proyectointegrador2.business.domain.entity.Localidad;
import nexora.proyectointegrador2.business.domain.entity.Nacionalidad;
import nexora.proyectointegrador2.business.domain.entity.Pais;
import nexora.proyectointegrador2.business.domain.entity.Persona;
import nexora.proyectointegrador2.business.domain.entity.Provincia;
import nexora.proyectointegrador2.business.domain.entity.Usuario;
import nexora.proyectointegrador2.business.domain.entity.Vehiculo;
import nexora.proyectointegrador2.business.enums.EstadoVehiculo;
import nexora.proyectointegrador2.business.enums.RolUsuario;
import nexora.proyectointegrador2.business.enums.TipoContacto;
import nexora.proyectointegrador2.business.enums.TipoDocumentacion;
import nexora.proyectointegrador2.business.enums.TipoEmpleado;
import nexora.proyectointegrador2.business.enums.TipoImagen;
import nexora.proyectointegrador2.business.enums.TipoTelefono;
import nexora.proyectointegrador2.business.logic.service.AlquilerService;
import nexora.proyectointegrador2.business.logic.service.CaracteristicaVehiculoService;
import nexora.proyectointegrador2.business.logic.service.ClienteService;
import nexora.proyectointegrador2.business.logic.service.ContactoCorreoElectronicoService;
import nexora.proyectointegrador2.business.logic.service.ContactoTelefonicoService;
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
                                        AlquilerRepository alquilerRepository,
                                        ContactoTelefonicoService contactoTelefonicoService,
                                        ContactoCorreoElectronicoService contactoCorreoElectronicoService) {
    return args -> {
      // Crear usuarios del sistema
      Usuario admin = usuarioRepository.findByNombreUsuario("admin").orElse(null);
      if (admin == null) {
        admin = Usuario.builder()
            .nombreUsuario("admin")
            .clave(passwordEncoder.encode("admin123"))
            .rol(RolUsuario.JEFE)
            .build();
        admin.setEliminado(false);
        admin = usuarioRepository.save(admin);
      }

      Usuario operador = usuarioRepository.findByNombreUsuario("operador").orElse(null);
      if (operador == null) {
        operador = Usuario.builder()
            .nombreUsuario("operador")
            .clave(passwordEncoder.encode("operador123"))
            .rol(RolUsuario.ADMINISTRATIVO)
            .build();
        operador.setEliminado(false);
        operador = usuarioRepository.save(operador);
      }

      Usuario clienteUsuario = usuarioRepository.findByNombreUsuario("cliente").orElse(null);
      if (clienteUsuario == null) {
        clienteUsuario = Usuario.builder()
                .nombreUsuario("cliente")
                .clave(passwordEncoder.encode("cliente123"))
                .rol(RolUsuario.CLIENTE)
                .build();
        clienteUsuario.setEliminado(false);
        clienteUsuario = usuarioRepository.save(clienteUsuario);
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
      
      // Crear empleados para usuarios admin y operador si no existen
      crearEmpleadosParaUsuariosSistema(empleadoService, empleadoRepository, direccionService, 
                                        direccionRepository, localidadRepository, admin, operador,
                                        contactoTelefonicoService, contactoCorreoElectronicoService);
      
      // Inicializar datos de prueba (esto crea direcciones y nacionalidad que necesitamos)
      initDatosPrueba(direccionService, direccionRepository, nacionalidadService, nacionalidadRepository,
                     empleadoService, empleadoRepository, clienteService, clienteRepository,
                     vehiculoService, vehiculoRepository, caracteristicaVehiculoService, costoVehiculoService,
                     imagenService, empresaService, empresaRepository, alquilerService, alquilerRepository,
                     localidadRepository, usuarioRepository, passwordEncoder,
                     contactoTelefonicoService, contactoCorreoElectronicoService);
      
      // Crear cliente para usuario "cliente" si no existe (después de inicializar datos de prueba)
      crearClienteParaUsuarioSistema(clienteService, clienteRepository, direccionService,
                                     direccionRepository, localidadRepository, nacionalidadService,
                                     nacionalidadRepository, clienteUsuario,
                                     contactoTelefonicoService, contactoCorreoElectronicoService);
      
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
      }

      Provincia mendoza = provinciaRepository.findByNombreAndEliminadoFalse("Mendoza")
          .orElse(null);
      
      if (mendoza == null) {
        mendoza = Provincia.builder()
            .nombre("Mendoza")
            .pais(argentina)
            .build();
        mendoza.setEliminado(false);
        mendoza = provinciaService.save(mendoza);
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
        }

        crearLocalidadesPorDepartamento(depto, localidadService, localidadRepository);
      }
      
    } catch (Exception e) {
      logger.error("Error al inicializar datos geográficos: {}", e.getMessage());
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
      }
    } catch (Exception e) {
      logger.warn("No se pudo crear la localidad '{}': {}", nombre, e.getMessage());
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
                               PasswordEncoder passwordEncoder,
                               ContactoTelefonicoService contactoTelefonicoService,
                               ContactoCorreoElectronicoService contactoCorreoElectronicoService) {
    try {
      Nacionalidad argentina = nacionalidadRepository.findByNombreAndEliminadoFalse("Argentina")
          .orElse(null);
      if (argentina == null) {
        argentina = Nacionalidad.builder()
            .nombre("Argentina")
            .build();
        argentina.setEliminado(false);
        argentina = nacionalidadService.save(argentina);
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
            .manzanaPiso("Piso 3")
            .casaDepartamento("Depto 5B")
            .referencia("Cerca del supermercado, esquina con calle Mitre")
            .localidad(godoycruz)
            .build();
        direccion1.setEliminado(false);
        direccion1 = direccionService.save(direccion1);
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
            .manzanaPiso("Manzana 12")
            .casaDepartamento("Casa 8")
            .referencia("Frente a la plaza principal")
            .localidad(lasHeras)
            .build();
        direccion2.setEliminado(false);
        direccion2 = direccionService.save(direccion2);
      }

      // 3. Crear 2 Empleados
      if (empleadoRepository.findByNumeroDocumentoAndEliminadoFalse("30123456").isEmpty()) {
        Usuario usuarioEmpleado1 = usuarioRepository.findByNombreUsuario("empleado1").orElse(null);
        if (usuarioEmpleado1 == null) {
          usuarioEmpleado1 = Usuario.builder()
              .nombreUsuario("empleado1")
              .clave(passwordEncoder.encode("mycar"))
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
        empleado1 = empleadoService.save(empleado1);
        crearContactosParaPersona(empleado1, "+54 9 261 1234567", "carlos.rodriguez@mycar.com", 
                                 contactoTelefonicoService, contactoCorreoElectronicoService);
      }

      if (empleadoRepository.findByNumeroDocumentoAndEliminadoFalse("31234567").isEmpty()) {
        Usuario usuarioEmpleado2 = usuarioRepository.findByNombreUsuario("empleado2").orElse(null);
        if (usuarioEmpleado2 == null) {
          usuarioEmpleado2 = Usuario.builder()
              .nombreUsuario("empleado2")
              .clave(passwordEncoder.encode("mycar"))
              .rol(RolUsuario.ADMINISTRATIVO)
              .build();
          usuarioEmpleado2.setEliminado(false);
          usuarioEmpleado2 = usuarioRepository.save(usuarioEmpleado2);
        }

        Empleado empleado2 = new Empleado();
        empleado2.setNombre("María");
        empleado2.setApellido("González");
        empleado2.setFechaNacimiento(new Date(92, 5, 20));
        empleado2.setTipoDocumento(TipoDocumentacion.DNI);
        empleado2.setNumeroDocumento("31234567");
        empleado2.setTipoEmpleado(TipoEmpleado.ADMINISTRATIVO);
        empleado2.setUsuario(usuarioEmpleado2);
        empleado2.setDireccion(direccion2);
        empleado2.setEliminado(false);
        empleado2 = empleadoService.save(empleado2);
        crearContactosParaPersona(empleado2, "+54 9 261 2345678", "maria.gonzalez@mycar.com", 
                                 contactoTelefonicoService, contactoCorreoElectronicoService);
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
        crearContactosParaPersona(cliente1, "+54 9 261 3456789", "juan.perez@email.com", 
                                 contactoTelefonicoService, contactoCorreoElectronicoService);
      } else {
        cliente1 = clienteRepository.findByNumeroDocumentoAndEliminadoFalse("32345678").get();
      }

      Cliente cliente2 = null;
      if (clienteRepository.findByNumeroDocumentoAndEliminadoFalse("33456789").isEmpty()) {
        cliente2 = new Cliente();
        cliente2.setNombre("Ana");
        cliente2.setApellido("Martínez");
        cliente2.setFechaNacimiento(new Date(95, 7, 25));
        cliente2.setTipoDocumento(TipoDocumentacion.DNI);
        cliente2.setNumeroDocumento("33456789");
        cliente2.setDireccionEstadia("Hostel Mendoza, Cama 12");
        cliente2.setNacionalidad(argentina);
        cliente2.setDireccion(direccion2);
        cliente2.setEliminado(false);
        cliente2 = clienteService.save(cliente2);
        crearContactosParaPersona(cliente2, "+54 9 261 4567890", "ana.martinez@email.com", 
                                 contactoTelefonicoService, contactoCorreoElectronicoService);
      } else {
        cliente2 = clienteRepository.findByNumeroDocumentoAndEliminadoFalse("33456789").get();
      }

      // 5. Crear 2 Vehículos con características y costos
      Vehiculo vehiculo1 = null;
      if (vehiculoRepository.findByPatenteAndEliminadoFalse("AB123CD").isEmpty()) {
        // Crear imagen de vehículo 1 (Toyota Corolla)
        byte[] imagenBytes1 = leerImagen("2020-toyota-corolla-xse.jpg");
        Imagen imagenVehiculo1 = Imagen.builder()
            .nombre("2020-toyota-corolla-xse.jpg")
            .mime("image/jpg")
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
      } else {
        vehiculo1 = vehiculoRepository.findByPatenteAndEliminadoFalse("AB123CD").get();
      }

      Vehiculo vehiculo2 = null;
      if (vehiculoRepository.findByPatenteAndEliminadoFalse("EF456GH").isEmpty()) {
        // Crear imagen de vehículo 2 (Honda Civic)
        byte[] imagenBytes2 = leerImagen("2021-honda-civic-sdn.jpg");
        Imagen imagenVehiculo2 = Imagen.builder()
            .nombre("2021-honda-civic-sdn.jpg")
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
      } else {
        vehiculo2 = vehiculoRepository.findByPatenteAndEliminadoFalse("EF456GH").get();
      }

      if (empresaRepository.findByEmailAndEliminadoFalse("contacto@mycar1.com").isEmpty()) {
        Empresa empresa1 = Empresa.builder()
            .nombre("MyCar Mendoza")
            .telefono("261-4567890")
            .email("contacto@mycar1.com")
            .direccion(direccion1)
            .build();
        empresa1.setEliminado(false);
        empresaService.save(empresa1);
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
      }

      if (alquilerRepository.count() == 0) {
        Alquiler alquiler1 = Alquiler.builder()
            .fechaDesde(new Date(125, 0, 10))
            .fechaHasta(new Date(125, 0, 15))
            .cliente(cliente1)
            .vehiculo(vehiculo1)
            .build();
        alquiler1.setEliminado(false);
        alquilerService.save(alquiler1);

        Alquiler alquiler2 = Alquiler.builder()
            .fechaDesde(new Date(125, 1, 1))
            .fechaHasta(new Date(125, 1, 7))
            .cliente(cliente2)
            .vehiculo(vehiculo2)
            .build();
        alquiler2.setEliminado(false);
        alquilerService.save(alquiler2);
      }

    } catch (Exception e) {
      logger.error("Error al inicializar datos de prueba: {}", e.getMessage());
    }
  }

  /**
   * Crea un cliente para el usuario "cliente" del sistema si no existe.
   */
  private void crearClienteParaUsuarioSistema(ClienteService clienteService,
                                             ClienteRepository clienteRepository,
                                             DireccionService direccionService,
                                             DireccionRepository direccionRepository,
                                             LocalidadRepository localidadRepository,
                                             NacionalidadService nacionalidadService,
                                             NacionalidadRepository nacionalidadRepository,
                                             Usuario clienteUsuario,
                                             ContactoTelefonicoService contactoTelefonicoService,
                                             ContactoCorreoElectronicoService contactoCorreoElectronicoService) {
    try {
      // Obtener nacionalidad Argentina
      Nacionalidad argentina = nacionalidadRepository.findByNombreAndEliminadoFalse("Argentina")
          .orElse(null);
      
      if (argentina == null) {
        argentina = Nacionalidad.builder()
            .nombre("Argentina")
            .build();
        argentina.setEliminado(false);
        argentina = nacionalidadService.save(argentina);
      }

      // Obtener o crear una dirección para el cliente del sistema
      Localidad ciudadMendoza = localidadRepository.findByNombreAndEliminadoFalse("Ciudad de Mendoza")
          .orElse(null);
      
      Direccion direccionCliente = null;
      if (ciudadMendoza != null) {
        direccionCliente = direccionRepository.findAll().stream()
            .filter(d -> !d.isEliminado() && ciudadMendoza.equals(d.getLocalidad()))
            .findFirst()
            .orElse(null);
        
        if (direccionCliente == null) {
          direccionCliente = Direccion.builder()
              .calle("Av. San Martín")
              .numero("1500")
              .barrio("Centro")
              .localidad(ciudadMendoza)
              .build();
          direccionCliente.setEliminado(false);
          direccionCliente = direccionService.save(direccionCliente);
        }
      }

      // Crear cliente para usuario "cliente" si no existe
      // Verificar primero si ya existe un cliente asociado al usuario
      boolean clienteAsociadoExiste = clienteUsuario != null && clienteRepository.findAll().stream()
          .anyMatch(c -> !c.isEliminado() && clienteUsuario.equals(c.getUsuario()));
      
      // Verificar si ya existe un cliente con el número de documento "20222222"
      boolean clientePorDocumentoExiste = clienteRepository.findByNumeroDocumentoAndEliminadoFalse("20222222").isPresent();
      
      if (clienteUsuario != null && !clienteAsociadoExiste && !clientePorDocumentoExiste) {
        Cliente clienteSistema = new Cliente();
        clienteSistema.setNombre("Cliente");
        clienteSistema.setApellido("Sistema");
        clienteSistema.setFechaNacimiento(new Date(90, 2, 20)); // 20/03/1990
        clienteSistema.setTipoDocumento(TipoDocumentacion.DNI);
        clienteSistema.setNumeroDocumento("20222222");
        clienteSistema.setDireccionEstadia("Hotel Central, Habitación 101");
        clienteSistema.setNacionalidad(argentina);
        clienteSistema.setUsuario(clienteUsuario);
        if (direccionCliente != null) {
          clienteSistema.setDireccion(direccionCliente);
        }
        clienteSistema.setEliminado(false);
        Cliente clienteGuardado = clienteService.save(clienteSistema);
        crearContactosParaPersona(clienteGuardado, "+54 9 261 5678901", "cliente.sistema@email.com", 
                                 contactoTelefonicoService, contactoCorreoElectronicoService);
      }
    } catch (Exception e) {
      logger.error("Error al crear cliente para usuario del sistema: {}", e.getMessage());
    }
  }

  /**
   * Crea empleados para los usuarios del sistema (admin y operador) si no existen.
   */
  private void crearEmpleadosParaUsuariosSistema(EmpleadoService empleadoService,
                                                 EmpleadoRepository empleadoRepository,
                                                 DireccionService direccionService,
                                                 DireccionRepository direccionRepository,
                                                 LocalidadRepository localidadRepository,
                                                 Usuario admin,
                                                 Usuario operador,
                                                 ContactoTelefonicoService contactoTelefonicoService,
                                                 ContactoCorreoElectronicoService contactoCorreoElectronicoService) {
    try {
      // Obtener o crear una dirección para los empleados del sistema
      Localidad ciudadMendoza = localidadRepository.findByNombreAndEliminadoFalse("Ciudad de Mendoza")
          .orElse(null);
      
      Direccion direccionSistema = null;
      if (ciudadMendoza != null) {
        direccionSistema = direccionRepository.findAll().stream()
            .filter(d -> !d.isEliminado() && ciudadMendoza.equals(d.getLocalidad()))
            .findFirst()
            .orElse(null);
        
        if (direccionSistema == null) {
          direccionSistema = Direccion.builder()
              .calle("Av. San Martín")
              .numero("1000")
              .barrio("Centro")
              .localidad(ciudadMendoza)
              .build();
          direccionSistema.setEliminado(false);
          direccionSistema = direccionService.save(direccionSistema);
        }
      }

      // Crear empleado para admin (JEFE) si no existe
      if (admin != null && empleadoRepository.findAll().stream()
          .noneMatch(e -> !e.isEliminado() && admin.equals(e.getUsuario()))) {
        Empleado empleadoAdmin = new Empleado();
        empleadoAdmin.setNombre("Administrador");
        empleadoAdmin.setApellido("Sistema");
        empleadoAdmin.setFechaNacimiento(new Date(80, 0, 1)); // 01/01/1980
        empleadoAdmin.setTipoDocumento(TipoDocumentacion.DNI);
        empleadoAdmin.setNumeroDocumento("20000000");
        empleadoAdmin.setTipoEmpleado(TipoEmpleado.JEFE);
        empleadoAdmin.setUsuario(admin);
        if (direccionSistema != null) {
          empleadoAdmin.setDireccion(direccionSistema);
        }
        empleadoAdmin.setEliminado(false);
        Empleado empleadoAdminGuardado = empleadoService.save(empleadoAdmin);
        crearContactosParaPersona(empleadoAdminGuardado, "+54 9 261 0000001", "admin@mycar.com", 
                                 contactoTelefonicoService, contactoCorreoElectronicoService);
      }

      if (operador != null && empleadoRepository.findAll().stream()
          .noneMatch(e -> !e.isEliminado() && operador.equals(e.getUsuario()))) {
        Empleado empleadoOperador = new Empleado();
        empleadoOperador.setNombre("Operador");
        empleadoOperador.setApellido("Sistema");
        empleadoOperador.setFechaNacimiento(new Date(85, 5, 15));
        empleadoOperador.setTipoDocumento(TipoDocumentacion.DNI);
        empleadoOperador.setNumeroDocumento("20111111");
        empleadoOperador.setTipoEmpleado(TipoEmpleado.ADMINISTRATIVO);
        empleadoOperador.setUsuario(operador);
        if (direccionSistema != null) {
          empleadoOperador.setDireccion(direccionSistema);
        }
        empleadoOperador.setEliminado(false);
        Empleado empleadoOperadorGuardado = empleadoService.save(empleadoOperador);
        crearContactosParaPersona(empleadoOperadorGuardado, "+54 9 261 0000002", "operador@mycar.com", 
                                 contactoTelefonicoService, contactoCorreoElectronicoService);
      }
    } catch (Exception e) {
      logger.error("Error al crear empleados para usuarios del sistema: {}", e.getMessage());
    }
  }

  /**
   * Crea contactos telefónico y de correo electrónico para una persona.
   */
  private void crearContactosParaPersona(Persona persona, String telefono, String email,
                                         ContactoTelefonicoService contactoTelefonicoService,
                                         ContactoCorreoElectronicoService contactoCorreoElectronicoService) {
    try {
      if (persona == null || persona.getId() == null) {
        return;
      }

      // Crear contacto telefónico celular (personal o laboral alternando)
      TipoContacto tipoContactoTelefono = (persona instanceof Empleado) ? TipoContacto.LABORAL : TipoContacto.PERSONAL;
      
      ContactoTelefonico contactoTelefonico = ContactoTelefonico.builder()
          .telefono(telefono)
          .tipoTelefono(TipoTelefono.CELULAR)
          .build();
      contactoTelefonico.setTipoContacto(tipoContactoTelefono);
      contactoTelefonico.setPersona(persona);
      contactoTelefonico.setEliminado(false);
      contactoTelefonicoService.save(contactoTelefonico);

      // Crear contacto de correo electrónico (personal o laboral alternando)
      TipoContacto tipoContactoEmail = (persona instanceof Empleado) ? TipoContacto.LABORAL : TipoContacto.PERSONAL;
      
      ContactoCorreoElectronico contactoCorreo = ContactoCorreoElectronico.builder()
          .email(email)
          .build();
      contactoCorreo.setTipoContacto(tipoContactoEmail);
      contactoCorreo.setPersona(persona);
      contactoCorreo.setEliminado(false);
      contactoCorreoElectronicoService.save(contactoCorreo);
    } catch (Exception e) {
      // Silenciar errores al crear contactos
    }
  }

  /**
   * Lee una imagen del sistema de archivos desde la carpeta Backend/image
   * @param nombreArchivo Nombre del archivo de imagen
   * @return Array de bytes con el contenido de la imagen
   */
  private byte[] leerImagen(String nombreArchivo) {
    try {
      // Intentar leer desde la carpeta Backend/image (relativa al directorio de trabajo)
      Path rutaImagen = Paths.get("Backend", "image", nombreArchivo);
      
      // Si no existe, intentar desde el directorio actual
      if (!Files.exists(rutaImagen)) {
        rutaImagen = Paths.get("image", nombreArchivo);
      }
      
      // Si aún no existe, intentar desde el directorio raíz del proyecto
      if (!Files.exists(rutaImagen)) {
        rutaImagen = Paths.get(System.getProperty("user.dir"), "Backend", "image", nombreArchivo);
      }
      
      if (Files.exists(rutaImagen)) {
        return Files.readAllBytes(rutaImagen);
      } else {
        logger.warn("No se encontró la imagen '{}'. Usando placeholder.", nombreArchivo);
        return ("placeholder-" + nombreArchivo).getBytes();
      }
    } catch (IOException e) {
      logger.error("Error al leer la imagen '{}': {}", nombreArchivo, e.getMessage());
      return ("error-" + nombreArchivo).getBytes();
    }
  }

}
