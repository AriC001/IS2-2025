package com.nexora.proyecto.gestion.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexora.proyecto.gestion.business.logic.service.AlquilerService;
import com.nexora.proyecto.gestion.business.logic.service.FacturaService;
import com.nexora.proyecto.gestion.business.persistence.dao.FacturaDAO;
import com.nexora.proyecto.gestion.dto.AlquilerDTO;
import com.nexora.proyecto.gestion.dto.FacturaDTO;
import com.nexora.proyecto.gestion.dto.enums.TipoPago;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

  @Autowired
  private FacturaService facturaService;

  @Autowired
  private FacturaDAO facturaDAO;

  @Autowired
  private AlquilerService alquilerService;

  /**
   * Muestra el formulario para generar una factura para un alquiler.
   */
  @GetMapping("/generar/{alquilerId}")
  public String mostrarFormularioGenerarFactura(@PathVariable String alquilerId, Model model, HttpSession session) {
    if (session.getAttribute("token") == null) {
      return "redirect:/auth/login";
    }
    try {
      model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
      model.addAttribute("rol", session.getAttribute("rol"));
      
      AlquilerDTO alquiler = alquilerService.findById(alquilerId);
      if (alquiler == null) {
        return "redirect:/alquileres";
      }

      // Verificar si ya existe una factura para este alquiler
      FacturaDTO facturaExistente = facturaDAO.findByAlquilerId(alquilerId);
      if (facturaExistente != null) {
        return "redirect:/facturas/" + facturaExistente.getId();
      }

      // Calcular el total del alquiler
      double total = calcularTotalAlquiler(alquiler);

      model.addAttribute("alquiler", alquiler);
      model.addAttribute("total", total);
      model.addAttribute("tipoPago", TipoPago.EFECTIVO); // Solo efectivo desde front administrativo
      return "facturas/form-generar";
    } catch (Exception e) {
      return "redirect:/alquileres";
    }
  }

  /**
   * Procesa la generación de la factura.
   */
  @PostMapping("/generar")
  public String generarFactura(
      @RequestParam String alquilerId,
      @RequestParam(defaultValue = "EFECTIVO") TipoPago tipoPago,
      @RequestParam(required = false) String observacion,
      RedirectAttributes redirectAttributes) {
    try {
      FacturaDTO factura = facturaDAO.generarFactura(alquilerId, tipoPago, observacion);
      redirectAttributes.addFlashAttribute("success", "Factura generada exitosamente");
      return "redirect:/facturas/" + factura.getId();
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al generar factura: " + e.getMessage());
      return "redirect:/alquileres/" + alquilerId;
    }
  }

  /**
   * Muestra los detalles de una factura.
   */
  @GetMapping("/{id}")
  public String verFactura(@PathVariable String id, Model model, HttpSession session) {
    if (session.getAttribute("token") == null) {
      return "redirect:/auth/login";
    }
    try {
      model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
      model.addAttribute("rol", session.getAttribute("rol"));
      
      FacturaDTO factura = facturaService.findById(id);
      if (factura == null) {
        return "redirect:/alquileres";
      }
      model.addAttribute("factura", factura);
      return "facturas/detail";
    } catch (Exception e) {
      return "redirect:/alquileres";
    }
  }

  /**
   * Descarga el PDF de una factura.
   */
  @GetMapping("/{id}/descargar")
  public ResponseEntity<byte[]> descargarFactura(@PathVariable String id, HttpServletResponse response) {
    try {
      byte[] pdfContent = facturaDAO.descargarPdf(id);
      
      FacturaDTO factura = facturaService.findById(id);
      String nombreArchivo = "factura_" + (factura != null ? factura.getNumeroFactura() : id) + ".pdf";

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_PDF);
      headers.setContentDispositionFormData("attachment", nombreArchivo);
      headers.setContentLength(pdfContent.length);

      return ResponseEntity.ok()
          .headers(headers)
          .body(pdfContent);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Calcula el total del alquiler basado en días y costo diario.
   */
  private double calcularTotalAlquiler(AlquilerDTO alquiler) {
    if (alquiler.getFechaDesde() == null || alquiler.getFechaHasta() == null) {
      return 0.0;
    }
    
    long diffInMillis = alquiler.getFechaHasta().getTime() - alquiler.getFechaDesde().getTime();
    long dias = java.util.concurrent.TimeUnit.DAYS.convert(diffInMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
    
    if (alquiler.getVehiculo() != null && 
        alquiler.getVehiculo().getCaracteristicaVehiculo() != null &&
        alquiler.getVehiculo().getCaracteristicaVehiculo().getCostoVehiculo() != null) {
      double costoDiario = alquiler.getVehiculo().getCaracteristicaVehiculo().getCostoVehiculo().getCosto();
      return dias * costoDiario;
    }
    
    return 0.0;
  }

}

