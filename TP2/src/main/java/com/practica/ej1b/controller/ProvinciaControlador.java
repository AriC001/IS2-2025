package com.practica.ej1b.controller;

import com.practica.ej1b.business.domain.dto.ProvinciaDTO;
import com.practica.ej1b.business.domain.entity.Provincia;
import com.practica.ej1b.business.logic.service.PaisServicio;
import com.practica.ej1b.business.logic.service.ProvinciaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/provincia")
public class ProvinciaControlador {

  @Autowired
  private ProvinciaServicio provinciaServicio;

  @Autowired
  private PaisServicio paisServicio;

  @GetMapping("/listar")
  public String listarProvincias(Model model) {
    try{
      model.addAttribute("provincias", provinciaServicio.listarProvinciaActivo());
      model.addAttribute("paises", paisServicio.listarPaisActivo());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "provincia/provincia-list";
  }

  @GetMapping("/nuevo")
  public String mostrarFormularioNuevaProvincia(Model model) {
    try {
      model.addAttribute("provinciaDTO", new ProvinciaDTO());
      model.addAttribute("paises", paisServicio.listarPaisActivo());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "provincia/provincia-form";
  }

  @PostMapping("/crear")
  public String crearProvincia(@ModelAttribute ProvinciaDTO provinciaDTO, Model model, RedirectAttributes redirectAttributes) {
    System.out.println(provinciaDTO.getNombre());
    System.out.println(provinciaDTO.getPaisId());
    try {
      provinciaServicio.crearProvincia(
        provinciaDTO.getNombre(),
        provinciaDTO.getPaisId()
      );
      redirectAttributes.addFlashAttribute("success", "Provincia creada correctamente");
      return "redirect:/provincia/listar";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "provincia/provincia-form";
    }
  }

  @GetMapping("/editar/{id}")
  public String mostrarFormularioEditarProvincia(@PathVariable String id, Model model) {
    try {
      Provincia prov = provinciaServicio.buscarProvincia(id);
      ProvinciaDTO provinciaDTO = ProvinciaDTO.fromEntity(prov);
      model.addAttribute("provinciaDTO", provinciaDTO);
      model.addAttribute("paises", paisServicio.listarPaisActivo());
      return "provincia/provincia-form";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "redirect:/provincia/listar";
    }
  }

  @PostMapping("/actualizar/{id}")
  public String actualizarProvincia(@PathVariable String id, @ModelAttribute ProvinciaDTO provinciaDTO,
                                    Model model, RedirectAttributes redirectAttributes) {
    System.out.println(provinciaDTO.getNombre());
    System.out.println(provinciaDTO.getPaisId());
    try {
      provinciaServicio.modificarProvincia(
        id,
        provinciaDTO.getNombre(),
        provinciaDTO.getPaisId()
      );
      redirectAttributes.addFlashAttribute("success", "Provincia actualizada correctamente");
      return "redirect:/provincia/listar";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "provincia/provincia-form";
    }
  }

  @GetMapping("/eliminar/{id}")
  public String eliminarProvincia(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
    try {
      provinciaServicio.eliminarProvincia(id);
      redirectAttributes.addFlashAttribute("success", "Provincia eliminada correctamente");
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/provincia/listar";
  }

}
