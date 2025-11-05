package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Empresa;
import nexora.proyectointegrador2.business.persistence.repository.EmpresaRepository;

@Service
public class EmpresaService extends BaseService<Empresa, String> {

  public EmpresaService(EmpresaRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(Empresa entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
      throw new Exception("El nombre de la empresa es obligatorio");
    }
    if (entity.getTelefono() == null || entity.getTelefono().trim().isEmpty()) {
      throw new Exception("El teléfono es obligatorio");
    }
    if (entity.getEmail() == null || entity.getEmail().trim().isEmpty()) {
      throw new Exception("El email es obligatorio");
    }
    
    // Validación básica de formato de email
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    if (!entity.getEmail().matches(emailRegex)) {
      throw new Exception("El formato del email no es válido");
    }
    
    if (entity.getDireccion() == null) {
      throw new Exception("La dirección es obligatoria");
    }
  }

}
