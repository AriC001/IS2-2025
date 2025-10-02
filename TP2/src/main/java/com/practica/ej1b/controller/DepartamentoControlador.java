package com.practica.ej1b.controller;

import com.practica.ej1b.business.domain.dto.DepartamentoDTO;
import com.practica.ej1b.business.domain.entity.Departamento;
import com.practica.ej1b.business.logic.service.DepartamentoServicio;
import com.practica.ej1b.business.logic.service.PaisServicio;
import com.practica.ej1b.business.logic.service.ProvinciaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/departamento")
public class DepartamentoControlador {

  @Autowired
  private DepartamentoServicio departamentoServicio;

  @Autowired
  private PaisServicio paisServicio;

  @GetMapping("/listar")
  public String listarDepartamentos(Model model) {
    try{
      model.addAttribute("departamentos", departamentoServicio.listarDepartamentoActivo());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "departamento/departamento-list";
  }

  @GetMapping("/nuevo")
  public String mostrarFormularioNuevoDepartamento(Model model) {
    try {
      model.addAttribute("departamentoDTO", new DepartamentoDTO());
      model.addAttribute("paises", paisServicio.listarPaisActivo());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "departamento/departamento-form";
  }

  @PostMapping("/crear")
  public String crearDepartamento(DepartamentoDTO departamentoDTO, Model model, RedirectAttributes redirectAttributes) {
    try {
      departamentoServicio.crearDepartamento(
        departamentoDTO.getNombre(),
        departamentoDTO.getProvinciaId()
      );
      redirectAttributes.addFlashAttribute("success", "Departamento creado correctamente");
      return "redirect:/departamento/listar";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "departamento/departamento-form";
    }
  }

  @GetMapping("/editar/{id}")
  public String mostrarFormularioEditarDepartamento(@PathVariable String id,  Model model) {
    try {
      Departamento dep = departamentoServicio.buscarDepartamento(id);
      DepartamentoDTO departamentoDTO = DepartamentoDTO.fromEntity(dep);
      model.addAttribute("departamentoDTO", departamentoDTO);
      model.addAttribute("paises", paisServicio.listarPaisActivo());
      return "departamento/departamento-form";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "redirect:/departamento/listar";
    }
  }

  @PostMapping("/actualizar/{id}")
  public String actualizarDepartamento(@PathVariable String id, DepartamentoDTO departamentoDTO, Model model,
                                       RedirectAttributes redirectAttributes) {
    try {
      departamentoServicio.modificarDepartamento(
        id,
        departamentoDTO.getNombre(),
        departamentoDTO.getProvinciaId()
      );
      redirectAttributes.addFlashAttribute("success", "Departamento modificado correctamente");
      return "redirect:/departamento/listar";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "departamento/departamento-form";
    }
  }

  @GetMapping("/eliminar/{id}")
  public String eliminarDepartamento(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
    try {
      departamentoServicio.eliminarDepartamento(id);
      redirectAttributes.addFlashAttribute("success", "Departamento eliminado correctamente");
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/departamento/listar";
  }

}
