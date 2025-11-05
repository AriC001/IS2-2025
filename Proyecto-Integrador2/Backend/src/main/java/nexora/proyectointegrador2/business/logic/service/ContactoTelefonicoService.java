package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.ContactoTelefonico;
import nexora.proyectointegrador2.business.persistence.repository.ContactoTelefonicoRepository;

@Service
public class ContactoTelefonicoService extends BaseService<ContactoTelefonico, String> {

  public ContactoTelefonicoService(ContactoTelefonicoRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(ContactoTelefonico entity) throws Exception {
    if (entity.getTelefono() == null || entity.getTelefono().trim().isEmpty()) {
      throw new Exception("El teléfono es obligatorio");
    }
    
    // Validación básica de formato de teléfono (solo números, espacios, guiones, paréntesis y +)
    String telefonoRegex = "^[0-9\\s\\-\\(\\)\\+]+$";
    if (!entity.getTelefono().matches(telefonoRegex)) {
      throw new Exception("El formato del teléfono no es válido");
    }
    
    if (entity.getTipoContacto() == null) {
      throw new Exception("El tipo de contacto es obligatorio");
    }
    if (entity.getTipoTelefono() == null) {
      throw new Exception("El tipo de teléfono es obligatorio");
    }
  }

}
