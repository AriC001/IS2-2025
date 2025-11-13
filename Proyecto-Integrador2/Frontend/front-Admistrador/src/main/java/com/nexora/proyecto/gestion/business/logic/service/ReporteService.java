package com.nexora.proyecto.gestion.business.logic.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyecto.gestion.business.persistence.dao.ReporteDAO;
import com.nexora.proyecto.gestion.dto.ReporteAlquilerDTO;
import com.nexora.proyecto.gestion.dto.ReporteRecaudacionDTO;

@Service
public class ReporteService {

  @Autowired
  private ReporteDAO reporteDAO;

  /**
   * Obtiene el reporte de alquileres con factura.
   */
  public List<ReporteAlquilerDTO> obtenerAlquileresConFactura(Date fechaDesde, Date fechaHasta) throws Exception {
    return reporteDAO.obtenerAlquileresConFactura(fechaDesde, fechaHasta);
  }

  /**
   * Obtiene el reporte de recaudación por modelo.
   */
  public List<ReporteRecaudacionDTO> obtenerRecaudacionPorModelo(Date fechaDesde, Date fechaHasta) throws Exception {
    if (fechaDesde == null || fechaHasta == null) {
      throw new Exception("Las fechas desde y hasta son obligatorias para el reporte de recaudación");
    }
    if (fechaDesde.after(fechaHasta)) {
      throw new Exception("La fecha desde no puede ser posterior a la fecha hasta");
    }
    return reporteDAO.obtenerRecaudacionPorModelo(fechaDesde, fechaHasta);
  }
}

