package com.practica.ej1b.business.logic.service;

import com.practica.ej1b.business.domain.dto.Item;
import com.practica.ej1b.business.domain.dto.UbicacionAncestryDTO;
import com.practica.ej1b.business.domain.entity.Departamento;
import com.practica.ej1b.business.domain.entity.Localidad;
import com.practica.ej1b.business.domain.entity.Pais;
import com.practica.ej1b.business.domain.entity.Provincia;
import com.practica.ej1b.business.persistence.repository.DepartamentoRepositorio;
import com.practica.ej1b.business.persistence.repository.LocalidadRepositorio;
import com.practica.ej1b.business.persistence.repository.PaisRepositorio;
import com.practica.ej1b.business.persistence.repository.ProvinciaRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class UbicacionQueryService {
  private final PaisRepositorio paisRepo;
  private final ProvinciaRepositorio provinciaRepo;
  private final DepartamentoRepositorio departamentoRepo;
  private final LocalidadRepositorio localidadRepo;

  public List<Item> listarPaisesActivos() {
    return paisRepo.findByEliminadoFalse()
      .stream()
      .map(p -> new Item(p.getId(), p.getNombre()))
      .toList();
  }

  public List<Item> listarProvinciasActivas(String paisId) {
    return provinciaRepo.findByPaisIdAndEliminadoFalse(paisId)
      .stream()
      .map(p -> new Item(p.getId(), p.getNombre()))
      .toList();
  }

  public List<Item> listarDepartamentosActivos(String provinciaId) {
    return departamentoRepo.findByProvinciaIdAndEliminadoFalse(provinciaId)
      .stream()
      .map(d -> new Item(d.getId(), d.getNombre()))
      .toList();
  }

  public List<Item> listarLocalidadesActivas(String departamentoId) {
    return localidadRepo.findByDepartamentoIdAndEliminadoFalse(departamentoId)
      .stream()
      .map(l -> new Item(l.getId(), l.getNombre()))
      .toList();
  }

  /**
   * Devuelve la cadena ancestral (pais->provincia->departamento->localidad) para una localidadId.
   * Útil para la precarga del form al editar.
   */
  public UbicacionAncestryDTO obtenerAncestryPorLocalidad(String localidadId) {
    Localidad loc = localidadRepo.findById(localidadId).orElse(null);
    if (loc == null) return null;

    Departamento dep = loc.getDepartamento();
    Provincia prov = (dep != null) ? dep.getProvincia() : null;
    Pais pais = (prov != null) ? prov.getPais() : null;

    Item ipais = (pais != null) ? new Item(pais.getId(), pais.getNombre()) : null;
    Item iprov = (prov != null) ? new Item(prov.getId(), prov.getNombre()) : null;
    Item idep  = (dep != null) ? new Item(dep.getId(), dep.getNombre()) : null;
    Item iloc  = new Item(loc.getId(), loc.getNombre());

    return new UbicacionAncestryDTO(ipais, iprov, idep, iloc);
  }
}
