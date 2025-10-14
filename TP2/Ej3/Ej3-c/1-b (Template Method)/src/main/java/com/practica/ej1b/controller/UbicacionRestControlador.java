package com.practica.ej1b.controller;

import com.practica.ej1b.business.domain.dto.Item;
import com.practica.ej1b.business.domain.dto.UbicacionAncestryDTO;
import com.practica.ej1b.business.logic.service.UbicacionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ubicacion")
public class UbicacionRestControlador {

  private final UbicacionQueryService svc;

  @GetMapping("/paises")
  public List<Item> paises() { return svc.listarPaisesActivos(); }

  @GetMapping("/provincias")
  public List<Item> provincias(@RequestParam String paisId) { return svc.listarProvinciasActivas(paisId); }

  @GetMapping("/departamentos")
  public List<Item> departamentos(@RequestParam String provinciaId) { return svc.listarDepartamentosActivos(provinciaId); }

  @GetMapping("/localidades")
  public List<Item> localidades(@RequestParam String departamentoId) { return svc.listarLocalidadesActivas(departamentoId); }

  // NUEVO: devolver la cadena ancestral para una localidad (para precarga en editar)
  @GetMapping("/ancestry")
  public UbicacionAncestryDTO ancestry(@RequestParam String localidadId) {
    return svc.obtenerAncestryPorLocalidad(localidadId);
  }
}

