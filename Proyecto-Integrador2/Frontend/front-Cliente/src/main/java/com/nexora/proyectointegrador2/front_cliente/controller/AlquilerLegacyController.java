package com.nexora.proyectointegrador2.front_cliente.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Controlador legacy para mantener compatibilidad con URLs antiguas.
 * Maneja /alquiler/detail que es llamado desde vehiculos/detail.html
 */
@Controller
@RequestMapping("/alquiler")
public class AlquilerLegacyController {

  private final AlquilerController alquilerController;

  public AlquilerLegacyController(AlquilerController alquilerController) {
    this.alquilerController = alquilerController;
  }

  /**
   * Mapeo para /alquiler/detail que delega a /mis-alquileres/detail
   * Construye la URL de redirección con todos los parámetros preservados
   */
  @GetMapping("/detail")
  public String showAlquilerDetail(@RequestParam(required = false) String id,
                                   @RequestParam(required = false) String fechaDesde,
                                   @RequestParam(required = false) String fechaHasta,
                                   @RequestParam(required = false) String total,
                                   @RequestParam(required = false) String basePrice,
                                   Model model,
                                   HttpSession session) {
    // Construir URL de redirección con todos los parámetros
    StringBuilder redirectUrl = new StringBuilder("/mis-alquileres/detail");
    boolean firstParam = true;
    
    if (id != null && !id.isBlank()) {
      redirectUrl.append(firstParam ? "?" : "&")
                 .append("id=").append(URLEncoder.encode(id, StandardCharsets.UTF_8));
      firstParam = false;
    }
    if (fechaDesde != null && !fechaDesde.isBlank()) {
      redirectUrl.append(firstParam ? "?" : "&")
                 .append("fechaDesde=").append(URLEncoder.encode(fechaDesde, StandardCharsets.UTF_8));
      firstParam = false;
    }
    if (fechaHasta != null && !fechaHasta.isBlank()) {
      redirectUrl.append(firstParam ? "?" : "&")
                 .append("fechaHasta=").append(URLEncoder.encode(fechaHasta, StandardCharsets.UTF_8));
      firstParam = false;
    }
    if (total != null && !total.isBlank()) {
      redirectUrl.append(firstParam ? "?" : "&")
                 .append("total=").append(URLEncoder.encode(total, StandardCharsets.UTF_8));
      firstParam = false;
    }
    if (basePrice != null && !basePrice.isBlank()) {
      redirectUrl.append(firstParam ? "?" : "&")
                 .append("basePrice=").append(URLEncoder.encode(basePrice, StandardCharsets.UTF_8));
    }
    
    return "redirect:" + redirectUrl.toString();
  }
}

