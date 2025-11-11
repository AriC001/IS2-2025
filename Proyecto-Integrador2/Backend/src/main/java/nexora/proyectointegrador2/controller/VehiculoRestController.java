package nexora.proyectointegrador2.controller;

import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

  public VehiculoRestController(VehiculoService service, VehiculoMapper mapper, HttpServletRequest request) {
    super(service, mapper);
    this.request = request;
  }

  @Override
  @GetMapping
  public ResponseEntity<List<VehiculoDTO>> findAll() throws Exception {
    logger.debug("Obteniendo todos los registros activos (filtrando por fecha)");

    String sDesde = request.getParameter("fechaDesde");
    String sHasta = request.getParameter("fechaHasta");

    Date fechaDesde = null;
    Date fechaHasta = null;

    if (sDesde != null && !sDesde.isEmpty()) {
      LocalDate ld = LocalDate.parse(sDesde);
      fechaDesde = java.sql.Date.valueOf(ld);
    }
    if (sHasta != null && !sHasta.isEmpty()) {
      LocalDate lh = LocalDate.parse(sHasta);
      fechaHasta = java.sql.Date.valueOf(lh);
    }

    // Si no viene fechaDesde la consideramos hoy
    if (fechaDesde == null) {
      fechaDesde = new Date();
    }

    // Llamamos al service especializado
    Collection<Vehiculo> vehiculos = ((VehiculoService) service)
        .findAllActivesDate(fechaDesde, fechaHasta);

    List<VehiculoDTO> dtos = mapper.toDTOList(vehiculos);
    logger.debug("Se obtuvieron {} registros", dtos.size());
    return ResponseEntity.ok(dtos);
  }
}

