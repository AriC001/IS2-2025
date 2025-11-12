package nexora.proyectointegrador2.business.logic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Usuario;
import nexora.proyectointegrador2.business.enums.RolUsuario;
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

  public AuthService(UsuarioRepository usuarioRepository, 
                     PasswordEncoder passwordEncoder,
                     JwtUtil jwtUtil) {
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

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

    // Generar token JWT
    String token = jwtUtil.generateToken(
        usuario.getId(), 
        usuario.getNombreUsuario(), 
        usuario.getRol()
    );

    logger.info("Login exitoso para usuario: {}", loginRequest.getNombreUsuario());

    return AuthResponseDTO.builder()
        .token(token)
        .tipo("Bearer")
        .id(usuario.getId())
        .nombreUsuario(usuario.getNombreUsuario())
        .rol(usuario.getRol())
        .build();
  }
  // Archivo: nexora.proyectointegrador2.business.logic.service.AuthService.java (o similar)

public void register(RegisterRequestDTO registerRequest) throws Exception {
    logger.info("Intento de registro para usuario: {}", registerRequest.getNombreUsuario());

    // 1. Verificar si el nombre de usuario ya existe
    if (usuarioRepository.findByNombreUsuario(registerRequest.getNombreUsuario()).isPresent()) {
        logger.warn("Fallo de registro: Nombre de usuario ya en uso: {}", registerRequest.getNombreUsuario());
        
        // Lanzar una excepción estándar con un mensaje específico
        throw new Exception("El nombre de usuario ya está registrado."); 
    }
    

    // 3. Cifrar la contraseña
    String claveCifrada = passwordEncoder.encode(registerRequest.getClave());

    // 4. Crear la entidad principal (Usuario / Cliente)
    Usuario nuevoUsuario = Usuario.builder()
        .nombreUsuario(registerRequest.getNombreUsuario())
        .clave(claveCifrada)
        .rol(RolUsuario.CLIENTE) // Asumo que el registro crea un rol CLIENTE
        // ... otros campos obligatorios ...
        .build();
    
    // 5. Guardar la entidad de Usuario (o Cliente/Usuario)
    usuarioRepository.save(nuevoUsuario);

    logger.info("✅ Registro exitoso para usuario: {}", registerRequest.getNombreUsuario());
}
}
