package com.nexora.proyecto.gestion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexora.proyecto.gestion.business.logic.service.AlquilerService;
import com.nexora.proyecto.gestion.business.logic.service.ClienteService;
import com.nexora.proyecto.gestion.business.logic.service.VehiculoService;
import com.nexora.proyecto.gestion.dto.AlquilerDTO;
import com.nexora.proyecto.gestion.dto.ClienteDTO;
import com.nexora.proyecto.gestion.dto.DocumentoDTO;
import com.nexora.proyecto.gestion.dto.VehiculoDTO;

@Controller
@RequestMapping("/alquileres")
public class AlquilerController extends BaseController<AlquilerDTO, String> {

  private final ClienteService clienteService;
  private final VehiculoService vehiculoService;

  public AlquilerController(AlquilerService alquilerService, ClienteService clienteService, VehiculoService vehiculoService) {
    super(alquilerService, "alquiler", "alquileres");
    this.clienteService = clienteService;
    this.vehiculoService = vehiculoService;
  }


  @Override
  protected AlquilerDTO createNewEntity() {
    AlquilerDTO alquiler = new AlquilerDTO();
    // Inicializar objetos anidados para evitar NullPointerException en Thymeleaf
    alquiler.setCliente(new ClienteDTO());
    alquiler.setVehiculo(new VehiculoDTO());
    alquiler.setDocumento(new DocumentoDTO());
    return alquiler;
  }

  @Override
  protected void prepareFormModel(Model model) {
    try {
      
      model.addAttribute("clientes", clienteService.findAllActives());
      model.addAttribute("vehiculos", vehiculoService.findAllActives());
    } catch (Exception e) {
      logger.error("Error al preparar modelo del formulario: {}", e.getMessage());
    }
  }

}

