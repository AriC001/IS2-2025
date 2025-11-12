package nexora.proyectointegrador2.business.logic.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Cliente;
import nexora.proyectointegrador2.business.domain.entity.Direccion;
import nexora.proyectointegrador2.business.domain.entity.Localidad;
import nexora.proyectointegrador2.business.domain.entity.Nacionalidad;
import nexora.proyectointegrador2.business.domain.entity.Usuario;
import nexora.proyectointegrador2.business.enums.RolUsuario;
import nexora.proyectointegrador2.business.enums.TipoDocumentacion;
import nexora.proyectointegrador2.business.persistence.repository.UsuarioRepository;
import nexora.proyectointegrador2.utils.dto.AuthResponseDTO;
import nexora.proyectointegrador2.utils.dto.LoginRequestDTO;
import nexora.proyectointegrador2.utils.dto.RegisterRequestDTO;
import nexora.proyectointegrador2.utils.security.JwtUtil;

@Service
public class AuthService {

  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  @Autowired
  private ClienteService clienteService;

  @Autowired
  private LocalidadService localidadService;

  @Autowired
  private NacionalidadService nacionalidadService;


  public AuthService(UsuarioRepository usuarioRepository, 
                     PasswordEncoder passwordEncoder,
                     JwtUtil jwtUtil) {
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  private static final String CLAVE_POR_DEFECTO = "mycar";

  public AuthResponseDTO login(LoginRequestDTO loginRequest) throws Exception {
    logger.info("Intento de autenticación para usuario: {}", loginRequest.getNombreUsuario());

    // Buscar usuario por nombre de usuario
    Usuario usuario = usuarioRepository.findByNombreUsuario(loginRequest.getNombreUsuario())
        .orElseThrow(() -> new Exception("Usuario no encontrado"));

    // Verificar que el usuario no esté eliminado
    if (usuario.isEliminado()) {
      logger.warn("Intento de login con usuario eliminado: {}", loginRequest.getNombreUsuario());
      throw new Exception("Usuario inactivo");
    }

    // Verificar contraseña
    if (!passwordEncoder.matches(loginRequest.getClave(), usuario.getClave())) {
      logger.warn("Contraseña incorrecta para usuario: {}", loginRequest.getNombreUsuario());
      throw new Exception("Credenciales inválidas");
    }

    // Verificar si la contraseña es la por defecto "mycar"
    // Comparar la contraseña encriptada del usuario con la contraseña por defecto encriptada
    boolean requiereCambioClave = passwordEncoder.matches(CLAVE_POR_DEFECTO, usuario.getClave());

    // Generar token JWT
    String token = jwtUtil.generateToken(
        usuario.getId(), 
        usuario.getNombreUsuario(), 
        usuario.getRol()
    );

    logger.info("Login exitoso para usuario: {}", loginRequest.getNombreUsuario());
    if (requiereCambioClave) {
      logger.warn("⚠ Usuario {} tiene contraseña por defecto, requiere cambio de contraseña", loginRequest.getNombreUsuario());
    }

    return AuthResponseDTO.builder()
        .token(token)
        .tipo("Bearer")
        .id(usuario.getId())
        .nombreUsuario(usuario.getNombreUsuario())
        .rol(usuario.getRol())
        .requiereCambioClave(requiereCambioClave)
        .build();
  }
  public void register(RegisterRequestDTO registerRequest) throws Exception {
    logger.info("Intento de registro para usuario: {}", registerRequest.getNombreUsuario());

    // 1. Verificar si el nombre de usuario ya existe
    if (usuarioRepository.findByNombreUsuario(registerRequest.getNombreUsuario()).isPresent()) {
        logger.warn("Fallo de registro: Nombre de usuario ya en uso: {}", registerRequest.getNombreUsuario());
        throw new Exception("El nombre de usuario ya está registrado."); 
    }

    // 2. Cifrar la contraseña
    String claveCifrada = passwordEncoder.encode(registerRequest.getClave());

    // 3. Crear el Usuario
    Usuario nuevoUsuario = Usuario.builder()
        .nombreUsuario(registerRequest.getNombreUsuario())
        .clave(claveCifrada)
        .rol(RolUsuario.CLIENTE)
        .build();
    nuevoUsuario.setEliminado(false);
    nuevoUsuario = usuarioRepository.save(nuevoUsuario);
    logger.debug("Usuario creado con ID: {}", nuevoUsuario.getId());

    // 4. Obtener Localidad y Nacionalidad
    Localidad localidad = localidadService.findById(registerRequest.getLocalidadId());
    Nacionalidad nacionalidad = nacionalidadService.findById(registerRequest.getNacionalidadId());

    // 5. Crear la Dirección (sin guardarla aún, el ClienteService.preAlta() lo hará)
    Direccion direccion = Direccion.builder()
        .calle(registerRequest.getCalle())
        .numero(registerRequest.getNumero())
        .barrio(registerRequest.getBarrio())
        .manzanaPiso(registerRequest.getManzanaPiso())
        .casaDepartamento(registerRequest.getCasaDepartamento())
        .referencia(registerRequest.getReferencia())
        .localidad(localidad)
        .build();
    direccion.setEliminado(false);

    // 6. Convertir LocalDate a Date para fechaNacimiento
    LocalDate localDate = registerRequest.getFechaNacimiento();
    Date fechaNacimiento = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

    // 7. Convertir String a TipoDocumentacion
    TipoDocumentacion tipoDocumento;
    try {
      tipoDocumento = TipoDocumentacion.valueOf(registerRequest.getTipoDocumento().toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new Exception("Tipo de documento inválido: " + registerRequest.getTipoDocumento());
    }

    // 8. Crear el Cliente
    Cliente cliente = new Cliente();
    cliente.setNombre(registerRequest.getNombre());
    cliente.setApellido(registerRequest.getApellido());
    cliente.setFechaNacimiento(fechaNacimiento);
    cliente.setTipoDocumento(tipoDocumento);
    cliente.setNumeroDocumento(registerRequest.getNumeroDocumento());
    cliente.setUsuario(nuevoUsuario);
    cliente.setDireccion(direccion);
    cliente.setNacionalidad(nacionalidad);
    cliente.setDireccionEstadia(registerRequest.getDireccionEstadia() != null ? registerRequest.getDireccionEstadia() : "");
    cliente.setEliminado(false);

    // 9. Los contactos son opcionales, se pueden agregar después si es necesario
    // Dejamos la lista de contactos vacía/null
    cliente.setContactos(null);

    // 10. Guardar el Cliente (esto también guardará los contactos gracias a CascadeType.ALL)
    cliente = clienteService.save(cliente);
    logger.info("✅ Registro exitoso para usuario: {} con Cliente ID: {}", registerRequest.getNombreUsuario(), cliente.getId());
  }
}
