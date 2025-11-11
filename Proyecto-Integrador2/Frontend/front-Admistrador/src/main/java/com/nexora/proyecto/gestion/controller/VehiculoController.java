package com.nexora.proyecto.gestion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexora.proyecto.gestion.business.logic.service.CaracteristicaVehiculoService;
import com.nexora.proyecto.gestion.business.logic.service.VehiculoService;
import com.nexora.proyecto.gestion.dto.VehiculoDTO;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController extends BaseController<VehiculoDTO, String> {

  private final CaracteristicaVehiculoService caracteristicaVehiculoService;

  public VehiculoController(VehiculoService vehiculoService,
      CaracteristicaVehiculoService caracteristicaVehiculoService) {
    super(vehiculoService, "vehiculo", "vehiculos");
    this.caracteristicaVehiculoService = caracteristicaVehiculoService;
  }

  @Override
  protected VehiculoDTO createNewEntity() {
    return new VehiculoDTO();
  }

  @Override
  protected void prepareFormModel(Model model) {
    try {
      model.addAttribute("caracteristicasVehiculo", caracteristicaVehiculoService.findAllActives());
    } catch (Exception e) {
      logger.error("Error al cargar características vehículo: {}", e.getMessage());
    }
  }

}
