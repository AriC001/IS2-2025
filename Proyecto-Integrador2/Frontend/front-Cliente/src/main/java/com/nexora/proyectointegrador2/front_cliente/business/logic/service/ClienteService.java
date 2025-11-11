package com.nexora.proyectointegrador2.front_cliente.business.logic.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyectointegrador2.front_cliente.business.persistence.dao.ClienteDAO;
import com.nexora.proyectointegrador2.front_cliente.dto.ClienteDTO;

@Service
public class ClienteService extends BaseService<ClienteDTO, String> {

  @Autowired
  public ClienteService(ClienteDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(ClienteDTO entity) throws Exception {
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
    if (entity.getNacionalidad() == null) {
      throw new Exception("La nacionalidad es obligatoria");
    }
    if (entity.getUsuario() != null) {
      if (entity.getUsuario().getId() == null || entity.getUsuario().getId().trim().isEmpty()) {
        entity.setUsuario(null);
      }
    }
  }

  public ClienteDTO findByNombreUsuario(String nombreUsuario) {
      try {
          return ((ClienteDAO) dao).findByNombreUsuario(nombreUsuario);
      } catch (Exception e) {
          throw new RuntimeException("Error buscando cliente por nombreUsuario: " + e.getMessage());
      }
  }

    
}

