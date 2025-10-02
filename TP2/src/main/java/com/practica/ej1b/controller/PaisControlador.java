package com.practica.ej1b.controller;

import com.practica.ej1b.business.domain.entity.Pais;
import com.practica.ej1b.business.logic.service.PaisServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pais")
public class PaisControlador {

  @Autowired
  private PaisServicio paisServicio;

  @GetMapping("/listar")
  public String listarPaises(Model model) {
    try {
      model.addAttribute("paises", paisServicio.listarPaisActivo());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "pais/pais-list";
  }

  @GetMapping("/nuevo")
  public String mostrarFormularioNuevoPais(Model model) {
    try {
      model.addAttribute("pais", new Pais());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "pais/pais-form";
  }

  @PostMapping("/crear")
  public String crearPais(@ModelAttribute Pais pais, Model model, RedirectAttributes redirectAttributes) {
    try {
      paisServicio.crearPais(
          pais.getNombre()
      );
      redirectAttributes.addFlashAttribute("success", "País creado correctamente");
      return "redirect:/pais/listar";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "pais/pais-form";
    }
  }

  @GetMapping("/editar/{id}")
  public String mostrarFormularioEditarPais(@PathVariable String id, Model model) {
    try {
      model.addAttribute("pais", paisServicio.buscarPais(id));
      return "pais/pais-form";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "redirect:/pais/listar";
    }
  }

  @PostMapping("/actualizar/{id}")
  public String actualizarPais(@PathVariable String id, @ModelAttribute Pais pais, Model model, RedirectAttributes redirectAttributes) {
    try {
      paisServicio.modificarPais(
        id,
        pais.getNombre()
      );
      redirectAttributes.addFlashAttribute("success", "País modificado correctamente");
      return "redirect:/pais/listar";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "pais/pais-form";
    }
  }

  @GetMapping("/eliminar/{id}")
  public String eliminarPais(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
    try {
      paisServicio.eliminarPais(id);
      redirectAttributes.addFlashAttribute("success", "País eliminado correctamente");
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/pais/listar";
  }

}
