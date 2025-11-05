package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.ConfiguracionCorreoElectronico;
import nexora.proyectointegrador2.business.persistence.repository.ConfiguracionCorreoElectronicoRepository;

@Service
public class ConfiguracionCorreoElectronicoService extends BaseService<ConfiguracionCorreoElectronico, String> {

  public ConfiguracionCorreoElectronicoService(ConfiguracionCorreoElectronicoRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(ConfiguracionCorreoElectronico entity) throws Exception {
    if (entity.getSmtp() == null || entity.getSmtp().trim().isEmpty()) {
      throw new Exception("El servidor SMTP es obligatorio");
    }
    if (entity.getPuerto() == null || entity.getPuerto() <= 0 || entity.getPuerto() > 65535) {
      throw new Exception("El puerto debe estar entre 1 y 65535");
    }
    if (entity.getEmail() == null || entity.getEmail().trim().isEmpty()) {
      throw new Exception("El email es obligatorio");
    }
    
    // Validación básica de formato de email
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    if (!entity.getEmail().matches(emailRegex)) {
      throw new Exception("El formato del email no es válido");
    }
    
    if (entity.getClave() == null || entity.getClave().trim().isEmpty()) {
      throw new Exception("La clave es obligatoria");
    }
    if (entity.getEmpresa() == null) {
      throw new Exception("La empresa es obligatoria");
    }
  }

}
