package com.practica.ej1b.controller;

import com.practica.ej1b.business.domain.dto.LocalidadDTO;
import com.practica.ej1b.business.domain.entity.Localidad;
import com.practica.ej1b.business.logic.service.LocalidadServicio;
import com.practica.ej1b.business.logic.service.PaisServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/localidad")
public class LocalidadControlador {

  @Autowired
  private LocalidadServicio localidadServicio;

  @Autowired
  private PaisServicio paisServicio;

  @GetMapping("/listar")
  public String listarLocalidades(Model model) {
    try{
      model.addAttribute("localidades", localidadServicio.listarLocalidadActivo());
      model.addAttribute("paises", paisServicio.listarPaisActivo());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "localidad/localidad-list";
  }

  @GetMapping("/nuevo")
  public String mostrarFormularioNuevaLocalidad(Model model) {
    try {
      model.addAttribute("localidadDTO", new LocalidadDTO());
      model.addAttribute("paises", paisServicio.listarPaisActivo());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "localidad/localidad-form";
  }

  @PostMapping("/crear")
  public String crearLocalidad(@ModelAttribute LocalidadDTO localidadDTO, Model model, RedirectAttributes redirectAttributes) {
    System.out.println(localidadDTO.getNombre());
    System.out.println(localidadDTO.getCodigoPostal());
    System.out.println(localidadDTO.getDepartamentoId());
    try {
      localidadServicio.crearLocalidad(
        localidadDTO.getNombre(),
        localidadDTO.getCodigoPostal(),
        localidadDTO.getDepartamentoId()
      );
      redirectAttributes.addFlashAttribute("success", "Localidad creada correctamente");
      return "redirect:/localidad/listar";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "localidad/localidad-form";
    }
  }

  @GetMapping("/editar/{id}")
  public String mostrarFormularioEditarLocalidad(@PathVariable String id, Model model) {
    try {
      Localidad loc = localidadServicio.buscarLocalidad(id);
      LocalidadDTO localidadDTO = LocalidadDTO.fromEntity(loc);
      model.addAttribute("localidadDTO", localidadDTO);
      model.addAttribute("paises", paisServicio.listarPaisActivo());
      return "localidad/localidad-form";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "redirect:/localidad/listar";
    }
  }

  @PostMapping("/actualizar/{id}")
  public String actualizarLocalidad(@PathVariable String id, @ModelAttribute LocalidadDTO localidadDTO, Model model,
                                    RedirectAttributes redirectAttributes) {
    try {
      localidadServicio.modificarLocalidad(
        id,
        localidadDTO.getNombre(),
        localidadDTO.getCodigoPostal(),
        localidadDTO.getDepartamentoId()
      );
      redirectAttributes.addFlashAttribute("success", "Localidad modificada correctamente");
      return "redirect:/localidad/listar";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "localidad/localidad-form";
    }
  }

  @GetMapping("/eliminar/{id}")
  public String eliminarLocalidad(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
    try {
      localidadServicio.eliminarLocalidad(id);
      redirectAttributes.addFlashAttribute("success", "Localidad eliminada correctamente");
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/localidad/listar";
  }


}
