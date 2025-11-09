package nexora.proyectointegrador2.business.logic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Usuario;
import nexora.proyectointegrador2.business.persistence.repository.UsuarioRepository;
import nexora.proyectointegrador2.utils.dto.AuthResponseDTO;
import nexora.proyectointegrador2.utils.dto.LoginRequestDTO;
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

}
