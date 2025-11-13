package nexora.proyectointegrador2.business.logic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nexora.proyectointegrador2.business.domain.entity.Alquiler;
import nexora.proyectointegrador2.business.domain.entity.DetalleFactura;
import nexora.proyectointegrador2.business.domain.entity.Factura;
import nexora.proyectointegrador2.business.persistence.repository.AlquilerRepository;
import nexora.proyectointegrador2.business.persistence.repository.FacturaRepository;
import nexora.proyectointegrador2.utils.dto.ReporteAlquilerDTO;
import nexora.proyectointegrador2.utils.dto.ReporteRecaudacionDTO;

@Service
public class ReporteService {

  private static final Logger logger = LoggerFactory.getLogger(ReporteService.class);

  @Autowired
  private AlquilerRepository alquilerRepository;

  @Autowired
  private FacturaRepository facturaRepository;

  /**
   * Obtiene la lista de alquileres con factura asociada para el reporte.
   * Si se proporcionan fechas, filtra por periodo de facturación.
   */
  @Transactional(readOnly = true)
  public List<ReporteAlquilerDTO> obtenerAlquileresConFactura(Date fechaDesde, Date fechaHasta) throws Exception {
    logger.info("Obteniendo alquileres con factura. Periodo: {} - {}", fechaDesde, fechaHasta);

    Collection<Alquiler> alquileres;
    
    if (fechaDesde != null && fechaHasta != null) {
      // Validar que fechaDesde <= fechaHasta
      if (fechaDesde.after(fechaHasta)) {
        throw new Exception("La fecha desde no puede ser posterior a la fecha hasta");
      }
      alquileres = alquilerRepository.findAlquileresConFacturaPorPeriodo(fechaDesde, fechaHasta);
    } else {
      alquileres = alquilerRepository.findAlquileresConFactura();
    }

    List<ReporteAlquilerDTO> reporte = new ArrayList<>();

    for (Alquiler alquiler : alquileres) {
      // Obtener la factura asociada al alquiler
      Factura factura = facturaRepository.findByAlquilerId(alquiler.getId())
          .orElse(null);

      if (factura == null) {
        logger.warn("Alquiler {} no tiene factura asociada, se omite del reporte", alquiler.getId());
        continue;
      }

      ReporteAlquilerDTO dto = ReporteAlquilerDTO.builder()
          .idAlquiler(alquiler.getId())
          .nombreCliente(alquiler.getCliente() != null ? alquiler.getCliente().getNombre() : "")
          .apellidoCliente(alquiler.getCliente() != null ? alquiler.getCliente().getApellido() : "")
          .documentoCliente(alquiler.getCliente() != null ? alquiler.getCliente().getNumeroDocumento() : "")
          .patenteVehiculo(alquiler.getVehiculo() != null ? alquiler.getVehiculo().getPatente() : "")
          .modeloVehiculo(alquiler.getVehiculo() != null && alquiler.getVehiculo().getCaracteristicaVehiculo() != null
              ? alquiler.getVehiculo().getCaracteristicaVehiculo().getModelo() : "")
          .marcaVehiculo(alquiler.getVehiculo() != null && alquiler.getVehiculo().getCaracteristicaVehiculo() != null
              ? alquiler.getVehiculo().getCaracteristicaVehiculo().getMarca() : "")
          .fechaDesde(alquiler.getFechaDesde())
          .fechaHasta(alquiler.getFechaHasta())
          .montoPagado(factura.getTotalPagado())
          .numeroFactura(factura.getNumeroFactura())
          .fechaFactura(factura.getFechaFactura())
          .build();

      reporte.add(dto);
    }

    logger.info("Se obtuvieron {} alquileres con factura para el reporte", reporte.size());
    return reporte;
  }

  /**
   * Obtiene la recaudación agrupada por modelo de vehículo para un periodo determinado.
   */
  @Transactional(readOnly = true)
  public List<ReporteRecaudacionDTO> obtenerRecaudacionPorModelo(Date fechaDesde, Date fechaHasta) throws Exception {
    logger.info("Obteniendo recaudación por modelo. Periodo: {} - {}", fechaDesde, fechaHasta);

    if (fechaDesde == null || fechaHasta == null) {
      throw new Exception("Las fechas desde y hasta son obligatorias para el reporte de recaudación");
    }

    // Validar que fechaDesde <= fechaHasta
    if (fechaDesde.after(fechaHasta)) {
      throw new Exception("La fecha desde no puede ser posterior a la fecha hasta");
    }

    Collection<Object[]> resultados = facturaRepository.findFacturasPagadasAgrupadasPorModelo(fechaDesde, fechaHasta);

    List<ReporteRecaudacionDTO> reporte = new ArrayList<>();

    for (Object[] resultado : resultados) {
      // resultado[0] = modeloVehiculo (String)
      // resultado[1] = marcaVehiculo (String)
      // resultado[2] = totalRecaudado (Double)
      // resultado[3] = cantidadAlquileres (Long)

      ReporteRecaudacionDTO dto = ReporteRecaudacionDTO.builder()
          .modeloVehiculo((String) resultado[0])
          .marcaVehiculo((String) resultado[1])
          .totalRecaudado(((Number) resultado[2]).doubleValue())
          .cantidadAlquileres(((Number) resultado[3]).longValue())
          .build();

      reporte.add(dto);
    }

    logger.info("Se obtuvieron {} modelos con recaudación para el reporte", reporte.size());
    return reporte;
  }
}

