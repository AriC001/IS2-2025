package nexora.proyectointegrador2.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.logic.service.ReporteExcelService;
import nexora.proyectointegrador2.business.logic.service.ReportePdfService;
import nexora.proyectointegrador2.business.logic.service.ReporteService;
import nexora.proyectointegrador2.utils.dto.ReporteAlquilerDTO;
import nexora.proyectointegrador2.utils.dto.ReporteRecaudacionDTO;

@RestController
@RequestMapping("api/v1/reportes")
public class ReporteRestController {

  private static final Logger logger = LoggerFactory.getLogger(ReporteRestController.class);
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  @Autowired
  private ReporteService reporteService;

  @Autowired
  private ReportePdfService reportePdfService;

  @Autowired
  private ReporteExcelService reporteExcelService;

  /**
   * Obtiene el reporte de alquileres con factura en formato JSON.
   * Parámetros opcionales: fechaDesde y fechaHasta (formato: yyyy-MM-dd)
   */
  @GetMapping("/alquileres-con-factura")
  public ResponseEntity<List<ReporteAlquilerDTO>> obtenerAlquileresConFactura(
      @RequestParam(required = false) String fechaDesde,
      @RequestParam(required = false) String fechaHasta) throws Exception {
    
    logger.info("Obteniendo reporte de alquileres con factura. Periodo: {} - {}", fechaDesde, fechaHasta);

    Date desde = parseDate(fechaDesde);
    Date hasta = parseDate(fechaHasta);

    List<ReporteAlquilerDTO> datos = reporteService.obtenerAlquileresConFactura(desde, hasta);
    return ResponseEntity.ok(datos);
  }

  /**
   * Descarga el reporte de alquileres con factura en formato PDF.
   */
  @GetMapping("/alquileres-con-factura/pdf")
  public ResponseEntity<byte[]> descargarAlquileresConFacturaPdf(
      @RequestParam(required = false) String fechaDesde,
      @RequestParam(required = false) String fechaHasta) throws Exception {
    
    logger.info("Generando PDF de reporte de alquileres con factura. Periodo: {} - {}", fechaDesde, fechaHasta);

    Date desde = parseDate(fechaDesde);
    Date hasta = parseDate(fechaHasta);

    List<ReporteAlquilerDTO> datos = reporteService.obtenerAlquileresConFactura(desde, hasta);
    byte[] pdfBytes = reportePdfService.generarReporteAlquileresPdf(datos);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", "reporte-alquileres-con-factura.pdf");
    headers.setContentLength(pdfBytes.length);

    return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
  }

  /**
   * Descarga el reporte de alquileres con factura en formato Excel.
   */
  @GetMapping("/alquileres-con-factura/excel")
  public ResponseEntity<byte[]> descargarAlquileresConFacturaExcel(
      @RequestParam(required = false) String fechaDesde,
      @RequestParam(required = false) String fechaHasta) throws Exception {
    
    logger.info("Generando Excel de reporte de alquileres con factura. Periodo: {} - {}", fechaDesde, fechaHasta);

    Date desde = parseDate(fechaDesde);
    Date hasta = parseDate(fechaHasta);

    List<ReporteAlquilerDTO> datos = reporteService.obtenerAlquileresConFactura(desde, hasta);
    byte[] excelBytes = reporteExcelService.generarReporteAlquileresExcel(datos);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    headers.setContentDispositionFormData("attachment", "reporte-alquileres-con-factura.xlsx");
    headers.setContentLength(excelBytes.length);

    return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
  }

  /**
   * Obtiene el reporte de recaudación por modelo en formato JSON.
   * Parámetros obligatorios: fechaDesde y fechaHasta (formato: yyyy-MM-dd)
   */
  @GetMapping("/recaudacion-por-modelo")
  public ResponseEntity<List<ReporteRecaudacionDTO>> obtenerRecaudacionPorModelo(
      @RequestParam(required = true) String fechaDesde,
      @RequestParam(required = true) String fechaHasta) throws Exception {
    
    logger.info("Obteniendo reporte de recaudación por modelo. Periodo: {} - {}", fechaDesde, fechaHasta);

    Date desde = parseDate(fechaDesde);
    Date hasta = parseDate(fechaHasta);

    if (desde == null || hasta == null) {
      throw new Exception("Las fechas desde y hasta son obligatorias para el reporte de recaudación");
    }

    List<ReporteRecaudacionDTO> datos = reporteService.obtenerRecaudacionPorModelo(desde, hasta);
    return ResponseEntity.ok(datos);
  }

  /**
   * Descarga el reporte de recaudación por modelo en formato PDF.
   */
  @GetMapping("/recaudacion-por-modelo/pdf")
  public ResponseEntity<byte[]> descargarRecaudacionPorModeloPdf(
      @RequestParam(required = true) String fechaDesde,
      @RequestParam(required = true) String fechaHasta) throws Exception {
    
    logger.info("Generando PDF de reporte de recaudación por modelo. Periodo: {} - {}", fechaDesde, fechaHasta);

    Date desde = parseDate(fechaDesde);
    Date hasta = parseDate(fechaHasta);

    if (desde == null || hasta == null) {
      throw new Exception("Las fechas desde y hasta son obligatorias para el reporte de recaudación");
    }

    List<ReporteRecaudacionDTO> datos = reporteService.obtenerRecaudacionPorModelo(desde, hasta);
    byte[] pdfBytes = reportePdfService.generarReporteRecaudacionPdf(datos);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", "reporte-recaudacion-por-modelo.pdf");
    headers.setContentLength(pdfBytes.length);

    return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
  }

  /**
   * Descarga el reporte de recaudación por modelo en formato Excel.
   */
  @GetMapping("/recaudacion-por-modelo/excel")
  public ResponseEntity<byte[]> descargarRecaudacionPorModeloExcel(
      @RequestParam(required = true) String fechaDesde,
      @RequestParam(required = true) String fechaHasta) throws Exception {
    
    logger.info("Generando Excel de reporte de recaudación por modelo. Periodo: {} - {}", fechaDesde, fechaHasta);

    Date desde = parseDate(fechaDesde);
    Date hasta = parseDate(fechaHasta);

    if (desde == null || hasta == null) {
      throw new Exception("Las fechas desde y hasta son obligatorias para el reporte de recaudación");
    }

    List<ReporteRecaudacionDTO> datos = reporteService.obtenerRecaudacionPorModelo(desde, hasta);
    byte[] excelBytes = reporteExcelService.generarReporteRecaudacionExcel(datos);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    headers.setContentDispositionFormData("attachment", "reporte-recaudacion-por-modelo.xlsx");
    headers.setContentLength(excelBytes.length);

    return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
  }

  /**
   * Parsea una fecha en formato yyyy-MM-dd a Date.
   * Retorna null si la fecha es null o vacía.
   */
  private Date parseDate(String fechaStr) throws ParseException {
    if (fechaStr == null || fechaStr.trim().isEmpty()) {
      return null;
    }
    try {
      return dateFormat.parse(fechaStr);
    } catch (ParseException e) {
      logger.error("Error al parsear fecha: {}", fechaStr, e);
      throw new ParseException("Formato de fecha inválido. Use el formato yyyy-MM-dd", 0);
    }
  }
}

