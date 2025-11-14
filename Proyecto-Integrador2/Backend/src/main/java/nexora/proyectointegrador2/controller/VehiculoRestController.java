package nexora.proyectointegrador2.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import nexora.proyectointegrador2.business.domain.entity.Vehiculo;
import nexora.proyectointegrador2.business.logic.service.VehiculoService;
import nexora.proyectointegrador2.utils.dto.VehiculoDTO;
import nexora.proyectointegrador2.utils.mapper.impl.VehiculoMapper;

@RestController
@RequestMapping("api/v1/vehiculos")
public class VehiculoRestController extends BaseRestController<Vehiculo, VehiculoDTO, String> {
  private final HttpServletRequest request;
  private final VehiculoService vehiculoService;

  public VehiculoRestController(VehiculoService service, VehiculoMapper mapper, HttpServletRequest request) {
    super(service, mapper);
    this.request = request;
    this.vehiculoService = service;
  }

  /**
   * Endpoint separado y explícito para obtener vehículos disponibles
   * utilizando binding estándar de Spring para parámetros de fecha.
   * Evita ambigüedades y utiliza formato ISO (yyyy-MM-dd).
   */
  @GetMapping("/filter")
  public ResponseEntity<List<VehiculoDTO>> findAvailable(
      @RequestParam(value = "fechaDesde", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.time.LocalDate fechaDesde,
      @RequestParam(value = "fechaHasta", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.time.LocalDate fechaHasta,
      @RequestParam(value = "marca",required = false) String marca, @RequestParam(value = "modelo",required = false)String modelo,
      @RequestParam(value = "anio",required = false) Integer anio
      ) throws Exception {
    logger.debug("GET /filetr called with fechaDesde={}, fechaHasta={}", fechaDesde, fechaHasta);
      String  marcaS = (marca == null || marca.trim().isEmpty()) ? null : marca.trim();
      String  modeloS = (modelo == null || modelo.trim().isEmpty()) ? null : modelo.trim();
      Integer  anioS = (anio == null || anio == 0) ? null : anio;
    Date desde = (fechaDesde == null) ? new java.util.Date()
        : java.sql.Date.valueOf(fechaDesde);
    Date hasta = (fechaHasta == null) ? null : java.sql.Date.valueOf(fechaHasta);

    logger.debug(desde + " "+ hasta);

    Collection<Vehiculo> vehiculos = vehiculoService.findAllActivesFilter(desde, hasta,marcaS,modeloS,anioS);
    List<VehiculoDTO> dtos = mapper.toDTOList(vehiculos);
    return ResponseEntity.ok(dtos);
  }

  @GetMapping("/availability")
  public ResponseEntity<Boolean> checkAvailability(
      @RequestParam(value = "fechaDesde", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.time.LocalDate fechaDesde,
      @RequestParam(value = "fechaHasta", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.time.LocalDate fechaHasta,
      @RequestParam(value = "idVehiculo", required = true) String idVehiculo) throws Exception {
    
    try {
      logger.debug("Verificando disponibilidad para vehículo {} desde {} hasta {}", idVehiculo, fechaDesde, fechaHasta);
      
      if (fechaDesde == null) {
        throw new IllegalArgumentException("fechaDesde es requerida");
      }
      if (fechaHasta == null) {
        throw new IllegalArgumentException("fechaHasta es requerida");
      }
      if (idVehiculo == null || idVehiculo.trim().isEmpty()) {
        throw new IllegalArgumentException("idVehiculo es requerido");
      }
      
      Date desde = java.sql.Date.valueOf(fechaDesde);
      Date hasta = java.sql.Date.valueOf(fechaHasta);
      
      if (desde.after(hasta)) {
        throw new IllegalArgumentException("fechaDesde no puede ser posterior a fechaHasta");
      }

      boolean availability = vehiculoService.checkAvailability(desde, hasta, idVehiculo);
      logger.debug("Disponibilidad para vehículo {}: {}", idVehiculo, availability);
      
      return ResponseEntity.ok(availability);
    } catch (IllegalArgumentException e) {
      logger.warn("Error de validación en checkAvailability: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      logger.error("Error al verificar disponibilidad para vehículo {}: {}", idVehiculo, e.getMessage(), e);
      throw e;
    }
  }
}

