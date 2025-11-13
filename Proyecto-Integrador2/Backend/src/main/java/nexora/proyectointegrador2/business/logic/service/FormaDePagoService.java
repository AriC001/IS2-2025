package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.FormaDePago;
import nexora.proyectointegrador2.business.persistence.repository.FormaDePagoRepository;

/**
 * Servicio para gestionar las formas de pago en el sistema.
 * 
 * Este servicio proporciona operaciones CRUD para la entidad FormaDePago,
 * que representa los diferentes métodos de pago utilizados para pagar
 * alquileres o facturas (efectivo, transferencia, billetera virtual).
 * 
 * Incluye validaciones para asegurar que los datos de la forma de pago
 * sean correctos antes de persistirlos.
 */
@Service
public class FormaDePagoService extends BaseService<FormaDePago, String> {

  public FormaDePagoService(FormaDePagoRepository repository) {
    super(repository);
  }

  /**
   * Valida que la entidad FormaDePago tenga todos los campos obligatorios.
   * 
   * @param entity La entidad FormaDePago a validar
   * @throws Exception Si el tipo de pago es nulo (campo obligatorio)
   */
  @Override
  protected void validar(FormaDePago entity) throws Exception {
    if (entity.getTipoPago() == null) {
      throw new Exception("El tipo de pago es obligatorio");
    }
  }

}

