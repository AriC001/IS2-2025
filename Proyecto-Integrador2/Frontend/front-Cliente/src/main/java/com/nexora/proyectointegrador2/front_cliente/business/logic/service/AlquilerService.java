package com.nexora.proyectointegrador2.front_cliente.business.logic.service;

import org.springframework.stereotype.Service;

import com.nexora.proyectointegrador2.front_cliente.business.persistence.dao.AlquilerDAO;
import com.nexora.proyectointegrador2.front_cliente.dto.AlquilerDTO;

import java.util.List;
import java.util.Optional;

@Service
public class AlquilerService extends BaseService<AlquilerDTO, String> {

  public AlquilerService(AlquilerDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(AlquilerDTO entity) throws Exception {
    if(entity.getVehiculo() == null){
      throw new Exception("El nombre de Vehiculo es obligatorio");
    }
    if(entity.getUsuario() == null){
      throw new Exception("El nombre de Usuario es obligatorio");
    }
    if(entity.getFechaDesde() == null){
      throw new Exception("La fecha Desde es obligatoria");
    }
    if(entity.getFechaHasta() == null){
      throw new Exception("La fecha Hasta es obligatoria");
    }
  }

  public List<AlquilerDTO> findByUsuarioId(String usuarioId) throws Exception {
    return ((AlquilerDAO) dao).findByUsuarioId(usuarioId);
  }
  /*private VehiculoDTO vehiculo ;
  private UsuarioDTO usuario;
  private Date fechaDesde;
  private Date fechaHasta; */

  // @Override
  // protected void validateEntity(AlquilerDTO entity) throws Exception {
  //   if (entity.getNombreAlquiler() == null || entity.getNombreAlquiler().trim().isEmpty()) {
  //     throw new Exception("El nombre de Alquiler es obligatorio");
  //   }
  //   if (entity.getNombreAlquiler().length() < MIN_USERNAME_LENGTH) {
  //     throw new Exception("El nombre de Alquiler debe tener al menos " + MIN_USERNAME_LENGTH + " caracteres");
  //   }
  //   if (!entity.getNombreAlquiler().matches(USERNAME_PATTERN)) {
  //     throw new Exception("El nombre de Alquiler solo puede contener letras, números, puntos, guiones y guiones bajos");
  //   }
  //   if (entity.getClave() == null || entity.getClave().trim().isEmpty()) {
  //     throw new Exception("La clave es obligatoria");
  //   }
  //   if (entity.getClave().length() < MIN_PASSWORD_LENGTH) {
  //     throw new Exception("La clave debe tener al menos " + MIN_PASSWORD_LENGTH + " caracteres");
  //   }
  //   if (entity.getRol() == null) {
  //     throw new Exception("El rol es obligatorio");
  //   }
  // }
  // public Optional<AlquilerDTO> findByUsername(String nombreAlquiler) {
  //   if (nombreAlquiler == null || nombreAlquiler.trim().isEmpty()) {
  //     return Optional.empty();
  //   }
  //   // Buscar entre las entidades activas expuestas por la API
  //   List<AlquilerDTO> Alquilers = dao.findAllActives();
  //   return Alquilers.stream()
  //           .filter(u -> nombreAlquiler.equals(u.getNombreAlquiler()))
  //           .findFirst();
  // }


}

