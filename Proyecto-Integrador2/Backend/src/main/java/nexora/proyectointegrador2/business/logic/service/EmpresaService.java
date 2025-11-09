package nexora.proyectointegrador2.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Direccion;
import nexora.proyectointegrador2.business.domain.entity.Empresa;
import nexora.proyectointegrador2.business.persistence.repository.EmpresaRepository;

@Service
public class EmpresaService extends BaseService<Empresa, String> {

  @Autowired
  private DireccionService direccionService;

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

  @Override
  protected void preAlta(Empresa entity) throws Exception {
    if (entity.getDireccion() != null && entity.getDireccion().getId() == null) {
      Direccion direccionGuardada = direccionService.save(entity.getDireccion());
      entity.setDireccion(direccionGuardada);
    } else if (entity.getDireccion() != null && entity.getDireccion().getId() != null) {
      Direccion direccionExistente = direccionService.findById(entity.getDireccion().getId());
      entity.setDireccion(direccionExistente);
    }
  }

  @Override
  protected void preUpdate(String id, Empresa entity) throws Exception {
    if (entity.getDireccion() != null && entity.getDireccion().getId() == null) {
      Direccion direccionGuardada = direccionService.save(entity.getDireccion());
      entity.setDireccion(direccionGuardada);
    } else if (entity.getDireccion() != null && entity.getDireccion().getId() != null) {
      Empresa empresaExistente = findById(id);
      if (empresaExistente.getDireccion() == null ||
          !empresaExistente.getDireccion().getId().equals(entity.getDireccion().getId())) {
        Direccion direccionExistente = direccionService.findById(entity.getDireccion().getId());
        entity.setDireccion(direccionExistente);
      }
    }
  }

}
