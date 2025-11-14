package com.nexora.proyectointegrador2.front_cliente.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.nexora.proyectointegrador2.front_cliente.business.logic.service.AlquilerService;
import com.nexora.proyectointegrador2.front_cliente.business.logic.service.UsuarioService;
import com.nexora.proyectointegrador2.front_cliente.business.logic.service.VehiculoService;
import com.nexora.proyectointegrador2.front_cliente.dto.AlquilerDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.UsuarioDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.VehiculoDTO;

@Controller
@RequestMapping("/pago")
public class PagoController {

  private static final Logger logger = LoggerFactory.getLogger(PagoController.class);
  
  private final AlquilerService alquilerService;
  private final UsuarioService usuarioService;
  private final VehiculoService vehiculoService;

  public PagoController(AlquilerService alquilerService, 
                       UsuarioService usuarioService,
                       VehiculoService vehiculoService) {
    this.alquilerService = alquilerService;
    this.usuarioService = usuarioService;
    this.vehiculoService = vehiculoService;
  }

  /**
   * Muestra una página simple para simular la aprobación del pago.
   * Acepta tanto `alquilerId` (alquiler existente) como `vehiculoId` (crear alquiler nuevo).
   */
  @GetMapping
  public String showPagoForm(@RequestParam(value = "alquilerId", required = false) String alquilerId,
                             @RequestParam(value = "vehiculoId", required = false) String vehiculoId,
                             @RequestParam(value = "fechaDesde", required = false) String fechaDesdeStr,
                             @RequestParam(value = "fechaHasta", required = false) String fechaHastaStr,
                             @RequestParam(value = "total", required = false) String totalStr,
                             Model model,
                             HttpSession session) {
    try {
      String finalAlquilerId = alquilerId;
      
      // Si no hay alquilerId pero hay vehiculoId, crear el alquiler primero
      if (alquilerId == null && vehiculoId != null) {
        logger.info("Creando alquiler para vehículo {} antes del pago", vehiculoId);
        
        // Obtener usuario de la sesión
        Object usuarioIdAttr = session.getAttribute("usuarioId");
        if (usuarioIdAttr == null) {
          model.addAttribute("error", "Debes iniciar sesión para realizar un pago");
          return "redirect:/auth/login";
        }
        
        String usuarioId = usuarioIdAttr.toString();
        UsuarioDTO usuario = usuarioService.findById(usuarioId);
        
        // Obtener vehículo
        VehiculoDTO vehiculo = vehiculoService.findById(vehiculoId);
        
        // Crear alquiler
        AlquilerDTO nuevoAlquiler = new AlquilerDTO();
        nuevoAlquiler.setUsuario(usuario);
        nuevoAlquiler.setVehiculo(vehiculo);
        
        // Establecer estado como PENDIENTE explícitamente (antes del pago)
        nuevoAlquiler.setEstado("PENDIENTE");
        
        // Parsear fechas
        if (fechaDesdeStr != null && !fechaDesdeStr.trim().isEmpty()) {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          Date desde = sdf.parse(fechaDesdeStr);
          nuevoAlquiler.setFechaDesde(desde);
        }
        if (fechaHastaStr != null && !fechaHastaStr.trim().isEmpty()) {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          Date hasta = sdf.parse(fechaHastaStr);
          nuevoAlquiler.setFechaHasta(hasta);
        }
        
        // Crear el alquiler
        AlquilerDTO creado = alquilerService.create(nuevoAlquiler);
        finalAlquilerId = creado.getId();
        logger.info("Alquiler creado con ID: {}", finalAlquilerId);
      }
      
      // Añadir alquilerId al modelo para que el template lo use
      model.addAttribute("alquilerId", finalAlquilerId);
      
      // Cargar datos de sesión para el template
      addSessionAttributesToModel(model, session);
      
      return "pago/confirm";
    } catch (Exception e) {
      logger.error("Error al preparar formulario de pago: {}", e.getMessage(), e);
      model.addAttribute("error", "Error al preparar el pago: " + e.getMessage());
      return "redirect:/mis-alquileres?error=" + e.getMessage();
    }
  }

  /**
   * Simula la confirmación del pago y carga el comprobante (receipt) del alquiler.
   * Actualiza el estado del alquiler a PAGADO y luego muestra el recibo.
   */
  @PostMapping("/confirm")
  public String confirmPago(@RequestParam("alquilerId") String alquilerId,
                            Model model,
                            HttpSession session) {
    try {
      // Recuperar alquiler desde la API
      AlquilerDTO alquiler = alquilerService.findById(alquilerId);
      
      if (alquiler == null) {
        model.addAttribute("error", "No se encontró la reserva con ID: " + alquilerId);
        model.addAttribute("alquilerId", alquilerId);
        return "pago/confirm";
      }
      
      // Actualizar estado del alquiler a PAGADO
      alquiler.setEstado("PAGADO");
      AlquilerDTO alquilerActualizado = alquilerService.update(alquilerId, alquiler);
      
      logger.info("Pago confirmado para alquiler {}. Estado actualizado a PAGADO.", alquilerId);
      
      // Cargar datos de sesión para el template
      addSessionAttributesToModel(model, session);
      
      // Pasar el alquiler actualizado al modelo
      model.addAttribute("alquiler", alquilerActualizado);
      model.addAttribute("success", "Pago confirmado exitosamente. Tu reserva ha sido confirmada.");
      
      return "alquiler/receipt";
    } catch (Exception e) {
      logger.error("Error al confirmar pago para alquiler {}: {}", alquilerId, e.getMessage(), e);
      model.addAttribute("error", "No se pudo confirmar el pago: " + e.getMessage());
      model.addAttribute("alquilerId", alquilerId);
      addSessionAttributesToModel(model, session);
      return "pago/confirm";
    }
  }
  
  /**
   * Método auxiliar para agregar atributos de sesión al modelo
   */
  private void addSessionAttributesToModel(Model model, HttpSession session) {
    model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
    model.addAttribute("rol", session.getAttribute("rol"));
  }

}
