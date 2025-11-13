package com.nexora.proyecto.gestion.controller;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nexora.proyecto.gestion.business.logic.service.AlquilerService;
import com.nexora.proyecto.gestion.business.logic.service.ClienteService;
import com.nexora.proyecto.gestion.business.logic.service.VehiculoService;
import com.nexora.proyecto.gestion.dto.AlquilerDTO;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

  @Autowired
  private AlquilerService alquilerService;

  @Autowired
  private ClienteService clienteService;

  @Autowired
  private VehiculoService vehiculoService;

  @GetMapping("/")
  public String home(HttpSession session) {
    // Si no hay sesión activa, redirigir al login
    if (session.getAttribute("token") == null) {
      return "redirect:/auth/login";
    }
    
    // Si hay sesión, redirigir al dashboard principal
    return "redirect:/dashboard";
  }

  @GetMapping("/dashboard")
  public String dashboard(HttpSession session, Model model) {
    // Verificar si hay sesión activa
    if (session.getAttribute("token") == null) {
      return "redirect:/auth/login";
    }
    
    // Pasar información del usuario al modelo para mostrar en la vista
    model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
    model.addAttribute("rol", session.getAttribute("rol"));
    
    try {
      // Obtener alquileres recientes (últimos 5 ordenados por fecha de creación más reciente)
      List<AlquilerDTO> todosAlquileres = alquilerService.findAllActives();
      List<AlquilerDTO> alquileresRecientes = todosAlquileres.stream()
          .sorted((a1, a2) -> {
            // Ordenar por fechaDesde descendente (más recientes primero)
            Date fecha1 = a1.getFechaDesde() != null ? a1.getFechaDesde() : new Date(0);
            Date fecha2 = a2.getFechaDesde() != null ? a2.getFechaDesde() : new Date(0);
            return fecha2.compareTo(fecha1);
          })
          .limit(5)
          .collect(Collectors.toList());
      
      model.addAttribute("alquileresRecientes", alquileresRecientes);
      
      // Estadísticas generales
      long totalAlquileres = todosAlquileres.size();
      long totalClientes = clienteService.findAllActives().size();
      long totalVehiculos = vehiculoService.findAllActives().size();
      
      // Contar alquileres activos (fechaDesde <= hoy <= fechaHasta)
      Date hoy = new Date();
      long alquileresActivos = todosAlquileres.stream()
          .filter(a -> a.getFechaDesde() != null && a.getFechaHasta() != null
              && !a.getFechaDesde().after(hoy) && !a.getFechaHasta().before(hoy))
          .count();
      
      model.addAttribute("totalAlquileres", totalAlquileres);
      model.addAttribute("totalClientes", totalClientes);
      model.addAttribute("totalVehiculos", totalVehiculos);
      model.addAttribute("alquileresActivos", alquileresActivos);
      
    } catch (Exception e) {
      // En caso de error, usar valores por defecto
      model.addAttribute("alquileresRecientes", Collections.emptyList());
      model.addAttribute("totalAlquileres", 0);
      model.addAttribute("totalClientes", 0);
      model.addAttribute("totalVehiculos", 0);
      model.addAttribute("alquileresActivos", 0);
    }
    
    // Mostrar dashboard principal (index.html)
    return "index";
  }

}
