package com.nexora.proyecto.gestion.business.logic.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyecto.gestion.business.persistence.dao.EmpleadoDAO;
import com.nexora.proyecto.gestion.dto.EmpleadoDTO;
import com.nexora.proyecto.gestion.dto.enums.TipoEmpleado;

@Service
public class EmpleadoService extends BaseService<EmpleadoDTO, String> {

  @Autowired
  public EmpleadoService(EmpleadoDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(EmpleadoDTO entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
      throw new Exception("El nombre es obligatorio");
    }
    if (entity.getApellido() == null || entity.getApellido().trim().isEmpty()) {
      throw new Exception("El apellido es obligatorio");
    }
    if (entity.getFechaNacimiento() == null) {
      throw new Exception("La fecha de nacimiento es obligatoria");
    }
    // Validar que la fecha de nacimiento no sea futura
    Date hoy = new Date();
    if (entity.getFechaNacimiento().after(hoy)) {
      throw new Exception("La fecha de nacimiento no puede ser una fecha futura");
    }
    if (entity.getTipoDocumento() == null) {
      throw new Exception("El tipo de documento es obligatorio");
    }
    if (entity.getNumeroDocumento() == null || entity.getNumeroDocumento().trim().isEmpty()) {
      throw new Exception("El número de documento es obligatorio");
    }
    if (entity.getTipoEmpleado() == null) {
      throw new Exception("El tipo de empleado es obligatorio");
    }
    if (entity.getUsuario() != null) {
      if (entity.getUsuario().getId() == null || entity.getUsuario().getId().trim().isEmpty()) {
        entity.setUsuario(null);
      }
    }
    if (entity.getImagenPerfil() != null) {
      if (entity.getImagenPerfil().getId() == null || entity.getImagenPerfil().getId().trim().isEmpty()) {
        entity.setImagenPerfil(null);
      }
    }
  }

  /**
   * Valida que solo un empleado JEFE pueda crear empleados ADMINISTRATIVO
   */
  public void validateTipoEmpleado(EmpleadoDTO entity, String rolUsuarioActual) throws Exception {
    if (entity.getTipoEmpleado() == TipoEmpleado.ADMINISTRATIVO) {
      if (!"JEFE".equals(rolUsuarioActual)) {
        throw new Exception("Solo un empleado JEFE puede dar de alta empleados ADMINISTRATIVO");
      }
    }
  }

}

