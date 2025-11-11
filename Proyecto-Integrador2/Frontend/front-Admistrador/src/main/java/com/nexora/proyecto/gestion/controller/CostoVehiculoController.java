package com.nexora.proyecto.gestion.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexora.proyecto.gestion.business.logic.service.CostoVehiculoService;
import com.nexora.proyecto.gestion.dto.CostoVehiculoDTO;

@Controller
@RequestMapping("/costos-vehiculo")
public class CostoVehiculoController extends BaseController<CostoVehiculoDTO, String> {

  public CostoVehiculoController(CostoVehiculoService costoVehiculoService) {
    super(costoVehiculoService, "costoVehiculo", "costos-vehiculo");
  }

  @Override
  protected CostoVehiculoDTO createNewEntity() {
    return new CostoVehiculoDTO();
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setLenient(false);
    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
  }

}

