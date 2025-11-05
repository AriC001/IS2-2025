package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Usuario;
import nexora.proyectointegrador2.business.persistence.repository.UsuarioRepository;

@Service
public class UsuarioService extends BaseService<Usuario, String> {

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
    if (entity.getNombreUsuario().length() < 4) {
      throw new Exception("El nombre de usuario debe tener al menos 4 caracteres");
    }
    if (entity.getClave() == null || entity.getClave().trim().isEmpty()) {
      throw new Exception("La clave es obligatoria");
    }
    if (entity.getClave().length() < 6) {
      throw new Exception("La clave debe tener al menos 6 caracteres");
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
