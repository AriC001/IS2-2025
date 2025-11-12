package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Usuario;
import nexora.proyectointegrador2.business.persistence.repository.UsuarioRepository;

@Service
public class UsuarioService extends BaseService<Usuario, String> {

  private static final int MIN_USERNAME_LENGTH = 4;
  private static final int MIN_PASSWORD_LENGTH = 4;
  private static final String USERNAME_PATTERN = "^[a-zA-Z0-9._-]{4,}$";

  private final UsuarioRepository usuarioRepository;

  public UsuarioService(UsuarioRepository repository) {
    super(repository);
    this.usuarioRepository = repository;
  }

  @Override
  protected void validar(Usuario entity) throws Exception {
    if (entity.getNombreUsuario() == null || entity.getNombreUsuario().trim().isEmpty()) {
      throw new Exception("El nombre de usuario es obligatorio");
    }
    if (entity.getNombreUsuario().length() < MIN_USERNAME_LENGTH) {
      throw new Exception("El nombre de usuario debe tener al menos " + MIN_USERNAME_LENGTH + " caracteres");
    }
    if (!entity.getNombreUsuario().matches(USERNAME_PATTERN)) {
      throw new Exception("El nombre de usuario solo puede contener letras, números, puntos, guiones y guiones bajos");
    }
    if (entity.getClave() == null || entity.getClave().trim().isEmpty()) {
      throw new Exception("La clave es obligatoria");
    }
    if (entity.getClave().length() < MIN_PASSWORD_LENGTH) {
      throw new Exception("La clave debe tener al menos " + MIN_PASSWORD_LENGTH + " caracteres");
    }
    if (entity.getRol() == null) {
      throw new Exception("El rol es obligatorio");
    }
    
    // Validar que el nombre de usuario sea único
    if (usuarioRepository.findByNombreUsuarioAndEliminadoFalse(entity.getNombreUsuario()).isPresent() 
        && (entity.getId() == null || !usuarioRepository.findByNombreUsuarioAndEliminadoFalse(entity.getNombreUsuario()).get().getId().equals(entity.getId()))) {
      throw new Exception("Ya existe un usuario con el nombre de usuario: " + entity.getNombreUsuario());
    }
  }

}
