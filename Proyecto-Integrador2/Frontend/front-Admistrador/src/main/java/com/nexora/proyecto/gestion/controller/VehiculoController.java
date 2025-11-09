package com.nexora.proyecto.gestion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexora.proyecto.gestion.business.logic.service.VehiculoService;
import com.nexora.proyecto.gestion.dto.VehiculoDTO;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController extends BaseController<VehiculoDTO, String> {

  public VehiculoController(VehiculoService vehiculoService) {
    super(vehiculoService, "vehículo", "vehiculos");
  }


  @Override
  protected VehiculoDTO createNewEntity() {
    return new VehiculoDTO();
  }

}
