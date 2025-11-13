package com.nexora.proyecto.gestion.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.nexora.proyecto.gestion.business.logic.service.ReporteService;
import com.nexora.proyecto.gestion.dto.ReporteAlquilerDTO;
import com.nexora.proyecto.gestion.dto.ReporteRecaudacionDTO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

  private static final Logger logger = LoggerFactory.getLogger(ReporteController.class);
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  @Autowired
  private ReporteService reporteService;

  @Autowired
  private RestTemplate restTemplate;

  @Value("${api.base.url}")
  private String baseUrl;

  /**
   * Muestra la página principal de reportes con formularios de filtro.
   */
  @GetMapping
  public String index(Model model, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }

    addSessionAttributesToModel(model, session);
    return "reportes/index";
  }

  /**
   * Muestra el reporte de alquileres con factura.
   */
  @GetMapping("/alquileres-con-factura")
  public String mostrarAlquileresConFactura(
      @RequestParam(required = false) String fechaDesde,
      @RequestParam(required = false) String fechaHasta,
      Model model,
      HttpSession session,
      RedirectAttributes redirectAttributes) {
    
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }

    try {
      addSessionAttributesToModel(model, session);

      Date desde = parseDate(fechaDesde);
      Date hasta = parseDate(fechaHasta);

      List<ReporteAlquilerDTO> datos = reporteService.obtenerAlquileresConFactura(desde, hasta);
      
      // Calcular total
      double totalMonto = datos.stream()
          .mapToDouble(d -> d.getMontoPagado() != null ? d.getMontoPagado() : 0.0)
          .sum();
      
      model.addAttribute("datos", datos);
      model.addAttribute("totalMonto", totalMonto);
      model.addAttribute("fechaDesde", fechaDesde);
      model.addAttribute("fechaHasta", fechaHasta);
      model.addAttribute("baseUrl", baseUrl);

      return "reportes/alquileres-con-factura";
    } catch (Exception e) {
      logger.error("Error al obtener reporte de alquileres con factura: {}", e.getMessage(), e);
      redirectAttributes.addFlashAttribute("error", "Error al obtener el reporte: " + e.getMessage());
      return "redirect:/reportes";
    }
  }

  /**
   * Muestra el reporte de recaudación por modelo.
   */
  @GetMapping("/recaudacion-por-modelo")
  public String mostrarRecaudacionPorModelo(
      @RequestParam(required = true) String fechaDesde,
      @RequestParam(required = true) String fechaHasta,
      Model model,
      HttpSession session,
      RedirectAttributes redirectAttributes) {
    
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }

    try {
      addSessionAttributesToModel(model, session);

      Date desde = parseDate(fechaDesde);
      Date hasta = parseDate(fechaHasta);

      if (desde == null || hasta == null) {
        redirectAttributes.addFlashAttribute("error", "Las fechas desde y hasta son obligatorias");
        return "redirect:/reportes";
      }

      List<ReporteRecaudacionDTO> datos = reporteService.obtenerRecaudacionPorModelo(desde, hasta);
      
      // Calcular totales
      double totalRecaudado = datos.stream()
          .mapToDouble(d -> d.getTotalRecaudado() != null ? d.getTotalRecaudado() : 0.0)
          .sum();
      long totalAlquileres = datos.stream()
          .mapToLong(d -> d.getCantidadAlquileres() != null ? d.getCantidadAlquileres() : 0L)
          .sum();
      
      model.addAttribute("datos", datos);
      model.addAttribute("totalRecaudado", totalRecaudado);
      model.addAttribute("totalAlquileres", totalAlquileres);
      model.addAttribute("fechaDesde", fechaDesde);
      model.addAttribute("fechaHasta", fechaHasta);
      model.addAttribute("baseUrl", baseUrl);

      return "reportes/recaudacion-por-modelo";
    } catch (Exception e) {
      logger.error("Error al obtener reporte de recaudación por modelo: {}", e.getMessage(), e);
      redirectAttributes.addFlashAttribute("error", "Error al obtener el reporte: " + e.getMessage());
      return "redirect:/reportes";
    }
  }

  /**
   * Verifica si hay una sesión activa.
   */
  private String checkSession(HttpSession session) {
    if (session.getAttribute("token") == null) {
      return "redirect:/auth/login";
    }
    return null;
  }

  /**
   * Agrega los atributos comunes de la sesión al modelo.
   */
  private void addSessionAttributesToModel(Model model, HttpSession session) {
    model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
    model.addAttribute("rol", session.getAttribute("rol"));
  }

  /**
   * Descarga el reporte de alquileres con factura en formato PDF.
   */
  @GetMapping("/alquileres-con-factura/pdf")
  public ResponseEntity<byte[]> descargarAlquileresConFacturaPdf(
      @RequestParam(required = false) String fechaDesde,
      @RequestParam(required = false) String fechaHasta,
      HttpSession session) {
    
    if (session.getAttribute("token") == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    try {
      UriComponentsBuilder builder = UriComponentsBuilder
          .fromHttpUrl(baseUrl + "/reportes/alquileres-con-factura/pdf");
      
      if (fechaDesde != null && !fechaDesde.trim().isEmpty()) {
        builder.queryParam("fechaDesde", fechaDesde);
      }
      if (fechaHasta != null && !fechaHasta.trim().isEmpty()) {
        builder.queryParam("fechaHasta", fechaHasta);
      }

      String url = builder.toUriString();
      logger.debug("Descargando PDF de alquileres con factura desde: {}", url);

      ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
      byte[] body = response.getBody();

      if (body == null || body.length == 0) {
        return ResponseEntity.notFound().build();
      }

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_PDF);
      headers.setContentLength(body.length);
      headers.setContentDispositionFormData("attachment", "reporte-alquileres-con-factura.pdf");

      return new ResponseEntity<>(body, headers, HttpStatus.OK);
    } catch (HttpClientErrorException e) {
      logger.error("Error al descargar PDF de alquileres: {}", e.getMessage());
      return ResponseEntity.status(e.getStatusCode()).build();
    } catch (Exception e) {
      logger.error("Error inesperado al descargar PDF de alquileres: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Descarga el reporte de alquileres con factura en formato Excel.
   */
  @GetMapping("/alquileres-con-factura/excel")
  public ResponseEntity<byte[]> descargarAlquileresConFacturaExcel(
      @RequestParam(required = false) String fechaDesde,
      @RequestParam(required = false) String fechaHasta,
      HttpSession session) {
    
    if (session.getAttribute("token") == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    try {
      UriComponentsBuilder builder = UriComponentsBuilder
          .fromHttpUrl(baseUrl + "/reportes/alquileres-con-factura/excel");
      
      if (fechaDesde != null && !fechaDesde.trim().isEmpty()) {
        builder.queryParam("fechaDesde", fechaDesde);
      }
      if (fechaHasta != null && !fechaHasta.trim().isEmpty()) {
        builder.queryParam("fechaHasta", fechaHasta);
      }

      String url = builder.toUriString();
      logger.debug("Descargando Excel de alquileres con factura desde: {}", url);

      ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
      byte[] body = response.getBody();

      if (body == null || body.length == 0) {
        return ResponseEntity.notFound().build();
      }

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
      headers.setContentLength(body.length);
      headers.setContentDispositionFormData("attachment", "reporte-alquileres-con-factura.xlsx");

      return new ResponseEntity<>(body, headers, HttpStatus.OK);
    } catch (HttpClientErrorException e) {
      logger.error("Error al descargar Excel de alquileres: {}", e.getMessage());
      return ResponseEntity.status(e.getStatusCode()).build();
    } catch (Exception e) {
      logger.error("Error inesperado al descargar Excel de alquileres: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Descarga el reporte de recaudación por modelo en formato PDF.
   */
  @GetMapping("/recaudacion-por-modelo/pdf")
  public ResponseEntity<byte[]> descargarRecaudacionPorModeloPdf(
      @RequestParam(required = true) String fechaDesde,
      @RequestParam(required = true) String fechaHasta,
      HttpSession session) {
    
    if (session.getAttribute("token") == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    try {
      String url = UriComponentsBuilder
          .fromHttpUrl(baseUrl + "/reportes/recaudacion-por-modelo/pdf")
          .queryParam("fechaDesde", fechaDesde)
          .queryParam("fechaHasta", fechaHasta)
          .toUriString();

      logger.debug("Descargando PDF de recaudación por modelo desde: {}", url);

      ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
      byte[] body = response.getBody();

      if (body == null || body.length == 0) {
        return ResponseEntity.notFound().build();
      }

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_PDF);
      headers.setContentLength(body.length);
      headers.setContentDispositionFormData("attachment", "reporte-recaudacion-por-modelo.pdf");

      return new ResponseEntity<>(body, headers, HttpStatus.OK);
    } catch (HttpClientErrorException e) {
      logger.error("Error al descargar PDF de recaudación: {}", e.getMessage());
      return ResponseEntity.status(e.getStatusCode()).build();
    } catch (Exception e) {
      logger.error("Error inesperado al descargar PDF de recaudación: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Descarga el reporte de recaudación por modelo en formato Excel.
   */
  @GetMapping("/recaudacion-por-modelo/excel")
  public ResponseEntity<byte[]> descargarRecaudacionPorModeloExcel(
      @RequestParam(required = true) String fechaDesde,
      @RequestParam(required = true) String fechaHasta,
      HttpSession session) {
    
    if (session.getAttribute("token") == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    try {
      String url = UriComponentsBuilder
          .fromHttpUrl(baseUrl + "/reportes/recaudacion-por-modelo/excel")
          .queryParam("fechaDesde", fechaDesde)
          .queryParam("fechaHasta", fechaHasta)
          .toUriString();

      logger.debug("Descargando Excel de recaudación por modelo desde: {}", url);

      ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
      byte[] body = response.getBody();

      if (body == null || body.length == 0) {
        return ResponseEntity.notFound().build();
      }

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
      headers.setContentLength(body.length);
      headers.setContentDispositionFormData("attachment", "reporte-recaudacion-por-modelo.xlsx");

      return new ResponseEntity<>(body, headers, HttpStatus.OK);
    } catch (HttpClientErrorException e) {
      logger.error("Error al descargar Excel de recaudación: {}", e.getMessage());
      return ResponseEntity.status(e.getStatusCode()).build();
    } catch (Exception e) {
      logger.error("Error inesperado al descargar Excel de recaudación: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Parsea una fecha en formato yyyy-MM-dd a Date.
   */
  private Date parseDate(String fechaStr) {
    if (fechaStr == null || fechaStr.trim().isEmpty()) {
      return null;
    }
    try {
      return dateFormat.parse(fechaStr);
    } catch (Exception e) {
      logger.warn("Error al parsear fecha: {}", fechaStr, e);
      return null;
    }
  }
}

