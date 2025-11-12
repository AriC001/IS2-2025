package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.FormaDePago;
import nexora.proyectointegrador2.business.persistence.repository.FormaDePagoRepository;

@Service
public class FormaDePagoService extends BaseService<FormaDePago, String> {

  public FormaDePagoService(FormaDePagoRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(FormaDePago entity) throws Exception {
    if (entity.getTipoPago() == null) {
      throw new Exception("El tipo de pago es obligatorio");
    }
  }

}

