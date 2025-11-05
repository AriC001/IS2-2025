package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Empleado;
import nexora.proyectointegrador2.business.persistence.repository.EmpleadoRepository;

@Service
public class EmpleadoService extends BaseService<Empleado, String> {

  private final EmpleadoRepository empleadoRepository;

  public EmpleadoService(EmpleadoRepository repository) {
    super(repository);
    this.empleadoRepository = repository;
  }

  @Override
  protected void validar(Empleado entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
      throw new Exception("El nombre es obligatorio");
    }
    if (entity.getApellido() == null || entity.getApellido().trim().isEmpty()) {
      throw new Exception("El apellido es obligatorio");
    }
    if (entity.getFechaNacimiento() == null) {
      throw new Exception("La fecha de nacimiento es obligatoria");
    }
    if (entity.getTipoDocumento() == null) {
      throw new Exception("El tipo de documento es obligatorio");
    }
    if (entity.getNumeroDocumento() == null || entity.getNumeroDocumento().trim().isEmpty()) {
      throw new Exception("El número de documento es obligatorio");
    }
    
    // Validar que el número de documento sea único
    if (empleadoRepository.findByNumeroDocumentoAndEliminadoFalse(entity.getNumeroDocumento()).isPresent() 
        && (entity.getId() == null || !empleadoRepository.findByNumeroDocumentoAndEliminadoFalse(entity.getNumeroDocumento()).get().getId().equals(entity.getId()))) {
      throw new Exception("Ya existe un empleado con el número de documento: " + entity.getNumeroDocumento());
    }
    
    if (entity.getTipoEmpleado() == null) {
      throw new Exception("El tipo de empleado es obligatorio");
    }
  }

}
