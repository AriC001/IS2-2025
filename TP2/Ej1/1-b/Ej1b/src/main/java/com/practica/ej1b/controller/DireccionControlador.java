package com.practica.ej1b.controller;

import com.practica.ej1b.business.domain.dto.DireccionDTO;
import com.practica.ej1b.business.domain.entity.Direccion;
import com.practica.ej1b.business.logic.service.DireccionServicio;
import com.practica.ej1b.business.logic.service.PaisServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/direccion")
public class DireccionControlador {

  @Autowired
  private DireccionServicio direccionServicio;

  @Autowired
  private PaisServicio paisServicio;

  @GetMapping("/listar")
  public String listarDirecciones(Model model) {
    try{
      model.addAttribute("direcciones", direccionServicio.listarDireccionActivo());
      model.addAttribute("paises", paisServicio.listarPaisActivo());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "direccion/direccion-list";
  }

  @GetMapping("/nuevo")
  public String mostrarFormularioNuevaDireccion(Model model) {
    System.out.println("Accediendo a /direccion/nuevo"); // Log para depuración
    try {
      model.addAttribute("direccionDTO", new DireccionDTO());
      model.addAttribute("paises", paisServicio.listarPaisActivo());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "direccion/direccion-form";
  }

  @PostMapping("/crear")
  public String crearDireccion(@ModelAttribute DireccionDTO direccionDTO, Model model, RedirectAttributes redirectAttributes) {
    System.out.println(direccionDTO.getCalle());
    System.out.println(direccionDTO.getNumeracion());
    System.out.println(direccionDTO.getManzanaPiso());
    System.out.println(direccionDTO.getCasaDepartamento());
    System.out.println(direccionDTO.getLocalidadId());
    try {
      direccionServicio.crearDireccion(
        direccionDTO.getCalle(),
        direccionDTO.getNumeracion(),
        direccionDTO.getBarrio(),
        direccionDTO.getManzanaPiso(),
        direccionDTO.getCasaDepartamento(),
        direccionDTO.getReferencia(),
        direccionDTO.getLocalidadId(),
        direccionDTO.getLatitud(),
        direccionDTO.getLongitud()
      );
      redirectAttributes.addFlashAttribute("success", "Dirección creada correctamente");
      return "redirect:/direccion/listar";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "direccion/direccion-form";
    }
  }

  @GetMapping("/editar/{id}")
  public String mostrarFormularioEditarDireccion(@PathVariable String id, Model model) {
    try {
      Direccion dir = direccionServicio.buscarDireccion(id);
      DireccionDTO direccionDTO = DireccionDTO.fromEntity(dir);
      model.addAttribute("direccionDTO", direccionDTO);
      model.addAttribute("paises", paisServicio.listarPaisActivo());
      return "direccion/direccion-form";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "redirect:/direccion/listar";
    }
  }

  @PostMapping("/actualizar/{id}")
  public String actualizarDireccion(@ModelAttribute DireccionDTO direccionDTO, Model model, RedirectAttributes redirectAttributes) {
    try {
      direccionServicio.modificarDireccion(
        direccionDTO.getId(),
        direccionDTO.getCalle(),
        direccionDTO.getNumeracion(),
        direccionDTO.getBarrio(),
        direccionDTO.getManzanaPiso(),
        direccionDTO.getCasaDepartamento(),
        direccionDTO.getReferencia(),
        direccionDTO.getLocalidadId(),
        direccionDTO.getLatitud(),
        direccionDTO.getLongitud()
      );
      redirectAttributes.addFlashAttribute("success", "Dirección modificada correctamente");
      return "redirect:/direccion/listar";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "direccion/direccion-form";
    }
  }

  @GetMapping("/eliminar/{id}")
  public String eliminarDireccion(@ModelAttribute DireccionDTO direccionDTO, Model model, RedirectAttributes redirectAttributes) {
    try {
      direccionServicio.eliminarDireccion(direccionDTO.getId());
      redirectAttributes.addFlashAttribute("success", "Dirección eliminada correctamente");
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/direccion/listar";
  }
}
