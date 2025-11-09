package nexora.proyectointegrador2.business.logic.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Alquiler;
import nexora.proyectointegrador2.business.domain.entity.Cliente;
import nexora.proyectointegrador2.business.domain.entity.Documento;
import nexora.proyectointegrador2.business.domain.entity.Vehiculo;
import nexora.proyectointegrador2.business.persistence.repository.AlquilerRepository;

@Service
public class AlquilerService extends BaseService<Alquiler, String> {

  private final AlquilerRepository alquilerRepository;

  @Autowired
  private ClienteService clienteService;

  @Autowired
  private VehiculoService vehiculoService;

  @Autowired
  private DocumentoService documentoService;

  public AlquilerService(AlquilerRepository repository) {
    super(repository);
    this.alquilerRepository = repository;
  }

  @Override
  protected void validar(Alquiler entity) throws Exception {
    if (entity.getFechaDesde() == null) {
      throw new Exception("La fecha desde es obligatoria");
    }
    if (entity.getFechaHasta() == null) {
      throw new Exception("La fecha hasta es obligatoria");
    }
    if (entity.getFechaDesde().after(entity.getFechaHasta())) {
      throw new Exception("La fecha desde no puede ser posterior a la fecha hasta");
    }
    if (entity.getCliente() == null) {
      throw new Exception("El cliente es obligatorio");
    }
    if (entity.getVehiculo() == null) {
      throw new Exception("El vehículo es obligatorio");
    }
    
    // Validar que el vehículo esté disponible en el rango de fechas
    // (Esta validación se puede mejorar con consultas más complejas)
  }

  @Override
  protected void preAlta(Alquiler entity) throws Exception {
    if (entity.getCliente() != null && entity.getCliente().getId() == null) {
      Cliente clienteGuardado = clienteService.save(entity.getCliente());
      entity.setCliente(clienteGuardado);
    } else if (entity.getCliente() != null && entity.getCliente().getId() != null) {
      Cliente clienteExistente = clienteService.findById(entity.getCliente().getId());
      entity.setCliente(clienteExistente);
    }

    if (entity.getVehiculo() != null && entity.getVehiculo().getId() == null) {
      Vehiculo vehiculoGuardado = vehiculoService.save(entity.getVehiculo());
      entity.setVehiculo(vehiculoGuardado);
    } else if (entity.getVehiculo() != null && entity.getVehiculo().getId() != null) {
      Vehiculo vehiculoExistente = vehiculoService.findById(entity.getVehiculo().getId());
      entity.setVehiculo(vehiculoExistente);
    }

    if (entity.getDocumento() != null && entity.getDocumento().getId() == null) {
      Documento documentoGuardado = documentoService.save(entity.getDocumento());
      entity.setDocumento(documentoGuardado);
    } else if (entity.getDocumento() != null && entity.getDocumento().getId() != null) {
      Documento documentoExistente = documentoService.findById(entity.getDocumento().getId());
      entity.setDocumento(documentoExistente);
    }
  }

  @Override
  protected void preUpdate(String id, Alquiler entity) throws Exception {
    if (entity.getCliente() != null && entity.getCliente().getId() == null) {
      Cliente clienteGuardado = clienteService.save(entity.getCliente());
      entity.setCliente(clienteGuardado);
    } else if (entity.getCliente() != null && entity.getCliente().getId() != null) {
      Alquiler alquilerExistente = findById(id);
      if (alquilerExistente.getCliente() == null ||
          !alquilerExistente.getCliente().getId().equals(entity.getCliente().getId())) {
        Cliente clienteExistente = clienteService.findById(entity.getCliente().getId());
        entity.setCliente(clienteExistente);
      }
    }

    if (entity.getVehiculo() != null && entity.getVehiculo().getId() == null) {
      Vehiculo vehiculoGuardado = vehiculoService.save(entity.getVehiculo());
      entity.setVehiculo(vehiculoGuardado);
    } else if (entity.getVehiculo() != null && entity.getVehiculo().getId() != null) {
      Alquiler alquilerExistente = findById(id);
      if (alquilerExistente.getVehiculo() == null ||
          !alquilerExistente.getVehiculo().getId().equals(entity.getVehiculo().getId())) {
        Vehiculo vehiculoExistente = vehiculoService.findById(entity.getVehiculo().getId());
        entity.setVehiculo(vehiculoExistente);
      }
    }

    if (entity.getDocumento() != null && entity.getDocumento().getId() == null) {
      Documento documentoGuardado = documentoService.save(entity.getDocumento());
      entity.setDocumento(documentoGuardado);
    } else if (entity.getDocumento() != null && entity.getDocumento().getId() != null) {
      Alquiler alquilerExistente = findById(id);
      if (alquilerExistente.getDocumento() == null ||
          !alquilerExistente.getDocumento().getId().equals(entity.getDocumento().getId())) {
        Documento documentoExistente = documentoService.findById(entity.getDocumento().getId());
        entity.setDocumento(documentoExistente);
      }
    }
  }

}

