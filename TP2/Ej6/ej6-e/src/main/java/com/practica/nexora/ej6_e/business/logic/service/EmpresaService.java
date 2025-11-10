package com.practica.nexora.ej6_e.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.nexora.ej6_e.business.domain.entity.Empresa;
import com.practica.nexora.ej6_e.business.persistence.repository.EmpresaRepository;

@Service
public class EmpresaService extends BaseService<Empresa, Long> {
  private final EmpresaRepository empresaRepository;

  public EmpresaService(EmpresaRepository empresaRepository) {
    super(empresaRepository);
    this.empresaRepository = empresaRepository;
  }

  @Override
  protected void validate(Empresa entity) throws IllegalArgumentException {
    if (entity == null) {
      throw new IllegalArgumentException("La Empresa no puede ser nula");
    }
    // Additional validation logic can be added here
    if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
      throw new IllegalArgumentException("El nombre de la empresa no puede ser nulo o vacio");
    }
  }

}
