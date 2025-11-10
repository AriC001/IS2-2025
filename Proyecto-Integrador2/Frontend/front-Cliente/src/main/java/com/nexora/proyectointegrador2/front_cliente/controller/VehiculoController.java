package com.nexora.proyectointegrador2.front_cliente.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexora.proyectointegrador2.front_cliente.business.logic.service.VehiculoService;
import com.nexora.proyectointegrador2.front_cliente.dto.VehiculoDTO;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController extends BaseController<VehiculoDTO, String> {

  public VehiculoController(VehiculoService vehiculoService) {
    super(vehiculoService, "vehiculo", "vehiculos");
  }


  @Override
  protected VehiculoDTO createNewEntity() {
    return new VehiculoDTO();
  }

  @GetMapping("/list")
    public String autos(){
        /*
        Auto controller API CALL para trarse todos los autos
         */
        return "vehiculos/list";
    }

}
