package com.nexora.proyectointegrador2.front_cliente.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import com.nexora.proyectointegrador2.front_cliente.business.logic.service.AlquilerService;
import com.nexora.proyectointegrador2.front_cliente.dto.AlquilerDTO;

@Controller
@RequestMapping("/pago")
public class PagoController {

  private final AlquilerService alquilerService;

  public PagoController(AlquilerService alquilerService) {
    this.alquilerService = alquilerService;
  }

  /**
   * Muestra una página simple para simular la aprobación del pago.
   * El parámetro `alquilerId` es pasado como hidden en el formulario.
   */
  @GetMapping
  public String showPagoForm(@RequestParam(value = "alquilerId", required = false) String alquilerId,
                             Model model,
                             HttpSession session) {
    // Añadir alquilerId al modelo para que el template lo use
    model.addAttribute("alquilerId", alquilerId);
    // Añadir datos de sesión si están presentes (compatibilidad visual)
    model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
    model.addAttribute("rol", session.getAttribute("rol"));
    return "pago/confirm";
  }

  /**
   * Simula la confirmación del pago y carga el comprobante (receipt) del alquiler.
   * Recupera el Alquiler por id y renderiza `alquiler/receipt`.
   */
  @PostMapping("/confirm")
  public String confirmPago(@RequestParam("alquilerId") String alquilerId,
                            Model model,
                            HttpSession session) {
    try {
      // Recuperar alquiler desde la API
      AlquilerDTO alquiler = alquilerService.findById(alquilerId);
      model.addAttribute("alquiler", alquiler);
      model.addAttribute("success", "Pago simulado correctamente. Reserva confirmada.");
      return "alquiler/receipt";
    } catch (Exception e) {
      model.addAttribute("error", "No se pudo recuperar la reserva: " + e.getMessage());
      model.addAttribute("alquilerId", alquilerId);
      return "pago/confirm";
    }
  }

}
