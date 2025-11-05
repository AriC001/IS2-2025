package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.ContactoCorreoElectronico;
import nexora.proyectointegrador2.business.persistence.repository.ContactoCorreoElectronicoRepository;

@Service
public class ContactoCorreoElectronicoService extends BaseService<ContactoCorreoElectronico, String> {

  private final ContactoCorreoElectronicoRepository contactoCorreoElectronicoRepository;

  public ContactoCorreoElectronicoService(ContactoCorreoElectronicoRepository repository) {
    super(repository);
    this.contactoCorreoElectronicoRepository = repository;
  }

  @Override
  protected void validar(ContactoCorreoElectronico entity) throws Exception {
    if (entity.getEmail() == null || entity.getEmail().trim().isEmpty()) {
      throw new Exception("El email es obligatorio");
    }
    
    // Validación básica de formato de email
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    if (!entity.getEmail().matches(emailRegex)) {
      throw new Exception("El formato del email no es válido");
    }
    
    if (entity.getTipoContacto() == null) {
      throw new Exception("El tipo de contacto es obligatorio");
    }
    
    // Validar que el email sea único
    if (contactoCorreoElectronicoRepository.findByEmailAndEliminadoFalse(entity.getEmail()).isPresent() 
        && (entity.getId() == null || !contactoCorreoElectronicoRepository.findByEmailAndEliminadoFalse(entity.getEmail()).get().getId().equals(entity.getId()))) {
      throw new Exception("Ya existe un contacto con el email: " + entity.getEmail());
    }
  }

}
