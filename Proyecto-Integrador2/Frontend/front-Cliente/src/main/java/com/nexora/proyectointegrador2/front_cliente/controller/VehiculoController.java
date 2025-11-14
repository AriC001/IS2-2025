package com.nexora.proyectointegrador2.front_cliente.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;
import java.util.List;

import com.nexora.proyectointegrador2.front_cliente.business.logic.service.VehiculoService;
import com.nexora.proyectointegrador2.front_cliente.dto.VehiculoDTO;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController extends BaseController<VehiculoDTO, String> {

  private final VehiculoService vehiculoService;

  public VehiculoController(VehiculoService vehiculoService) {
    super(vehiculoService, "vehiculo", "vehiculos");
    this.vehiculoService = vehiculoService;
  }


  @Override
  protected VehiculoDTO createNewEntity() {
    return new VehiculoDTO();
  }

  @GetMapping({"/list","/"}) 
  public String autos(
    @RequestParam(required = false) String marca,
    @RequestParam(required = false) String modelo,
    @RequestParam(required = false) String precioMax,
    @RequestParam(required = false) String fechaDesde,
    @RequestParam(required = false) String fechaHasta,
    Model model,
    HttpSession session
  ) {
    // Validar sesión (usa método protegido del BaseController)
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }

    try {
      // Añadir atributos de sesión comunes al modelo
      addSessionAttributesToModel(model, session);

      // Llamar al servicio (heredado desde BaseController) para obtener DTOs
      List<VehiculoDTO> vehiculos;
      vehiculos = service.findAllActives();

      // Añadir lista al modelo con el nombre del entityPath ("vehiculos")
      model.addAttribute(entityPath, vehiculos);

      // Pasar los valores de filtros de vuelta al template para retención en inputs
      model.addAttribute("marca", marca);
      model.addAttribute("modelo", modelo);
      model.addAttribute("precioMax", precioMax);
      model.addAttribute("fechaDesde", fechaDesde);
      model.addAttribute("fechaHasta", fechaHasta);

      return "vehiculos/list";
    } catch (Exception e) {
      // En caso de fallo, delegar en el manejo de excepciones del controlador base
      e.printStackTrace();
      model.addAttribute("error", "Error al obtener vehículos: " + e.getMessage());
      return "vehiculos/list";
    }
  }

  /**
   * Endpoint JSON que devuelve la lista de vehículos disponibles entre dos fechas.
   * Se usa desde JS en la página para marcar disponibilidad sin recargar la plantilla.
   */
  @GetMapping("/available")
  @ResponseBody
  public boolean available(
      @RequestParam String fechaDesde,
      @RequestParam String fechaHasta,
      @RequestParam String id,
      HttpSession session
  ) {
    // Validar sesión (igual que en otros endpoints)
    String redirect = checkSession(session);
    if (redirect != null) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autenticado");
    }

    try {
      logger.debug("Verificando disponibilidad para vehículo {} desde {} hasta {}", id, fechaDesde, fechaHasta);
      
      if (fechaDesde == null || fechaDesde.isBlank()) {
        throw new IllegalArgumentException("fechaDesde es requerida");
      }
      if (fechaHasta == null || fechaHasta.isBlank()) {
        throw new IllegalArgumentException("fechaHasta es requerida");
      }
      if (id == null || id.isBlank()) {
        throw new IllegalArgumentException("id es requerido");
      }
      
      java.util.Map<String,String> filters = new java.util.HashMap<>();
      filters.put("fechaDesde", fechaDesde);
      filters.put("fechaHasta", fechaHasta);
      filters.put("idVehiculo", id);

      // Delegar a service (findAvailability construye la consulta al backend)
      boolean result = vehiculoService.findAvailability(filters);
      logger.debug("Resultado de disponibilidad para vehículo {}: {}", id, result);
      return result;
    } catch (IllegalArgumentException e) {
      logger.warn("Error de validación en available: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      logger.error("Error al consultar disponibilidad: {}", e.getMessage(), e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar disponibilidad: " + e.getMessage());
    }
  }

  @GetMapping("/filter")
  public String filtro(
          @RequestParam(required = false) String marca,
          @RequestParam(required = false) String modelo,
          @RequestParam(required = false) String precioMax,
          @RequestParam(required = false) String fechaDesde,
          @RequestParam(required = false) String fechaHasta,
          Model model,
          HttpSession session
  ) {
    // Validar sesión (usa método protegido del BaseController)
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }

    try {
      // Añadir atributos de sesión comunes al modelo
      addSessionAttributesToModel(model, session);

      // Llamar al servicio (heredado desde BaseController) para obtener DTOs
      List<VehiculoDTO> vehiculos;
      boolean hasFilter = (marca != null && !marca.isBlank()) || (modelo != null && !modelo.isBlank())
              || (precioMax != null && !precioMax.isBlank()) || (fechaDesde != null && !fechaDesde.isBlank())
              || (fechaHasta != null && !fechaHasta.isBlank());
      System.out.println(hasFilter);
      if (hasFilter) {
        java.util.Map<String, String> filters = new java.util.HashMap<>();
        filters.put("marca", marca);
        filters.put("modelo", modelo);
        filters.put("precioMax", precioMax);
        filters.put("fechaDesde", fechaDesde);
        filters.put("fechaHasta", fechaHasta);
        vehiculos = service.findByFilters(filters);
      } else {
        System.out.println("aAAAAa");
        vehiculos = service.findAllActives();
      }

      // Añadir lista al modelo con el nombre del entityPath ("vehiculos")
      model.addAttribute(entityPath, vehiculos);

      // Pasar los valores de filtros de vuelta al template para retención en inputs
      model.addAttribute("marca", marca);
      model.addAttribute("modelo", modelo);
      model.addAttribute("precioMax", precioMax);
      model.addAttribute("fechaDesde", fechaDesde);
      model.addAttribute("fechaHasta", fechaHasta);

      return "vehiculos/list";
    } catch (Exception e) {
      // En caso de fallo, delegar en el manejo de excepciones del controlador base
      e.printStackTrace();
      model.addAttribute("error", "Error al obtener vehículos: " + e.getMessage());
      return "vehiculos/list";
    }
  }


}
