package com.practica.ej1b.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.ej1b.business.domain.dto.PaisDTO;
import com.practica.ej1b.business.domain.entity.Pais;
import com.practica.ej1b.business.persistence.repository.PaisRepositorio;

@Service
public class PaisService extends BaseService<Pais, String, PaisDTO> {

  public PaisService(PaisRepositorio paisRepositorio) {
    super(paisRepositorio);
  }

  @Override
  protected String getNombreEntidad() {
    return "País";
  }

  @Override
  protected void validar(Pais entidad) throws Exception {
    if (entidad.getNombre() == null || entidad.getNombre().trim().isEmpty()) {
      throw new Exception("El nombre del país es obligatorio");
    }
  }

  @Override
  protected Pais dtoAEntidad(PaisDTO dto) throws Exception {
    if (dto == null) {
      throw new Exception("El DTO de país no puede ser nulo");
    }
    Pais pais = new Pais();
    pais.setId(dto.getId());
    pais.setNombre(dto.getNombre());
    return pais;
  }

  @Override
  protected Pais mapearDatosDto(Pais entidadExistente, PaisDTO dto) throws Exception {
    entidadExistente.setNombre(dto.getNombre());
    return entidadExistente;
  }

}



