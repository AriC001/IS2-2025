package com.practica.ej1b.controller;

import com.practica.ej1b.business.domain.dto.DireccionDTO;
import com.practica.ej1b.business.domain.dto.EmpresaDTO;
import com.practica.ej1b.business.domain.entity.Empresa;
import com.practica.ej1b.business.logic.service.EmpresaServicio;
import com.practica.ej1b.business.logic.service.PaisServicio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/empresa")
public class EmpresaControlador {

  @Autowired
  private EmpresaServicio empresaServicio;

  @Autowired
  private PaisServicio paisServicio;

  @GetMapping("/listar")
  public String listarEmpresas(Model model) {
    try{
      model.addAttribute("empresas", empresaServicio.listarEmpresaActivo());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "empresa/empresa-list";
  }

  @GetMapping("/nuevo")
  public String mostrarFormularioNuevaEmpresa(Model model) {
    try {
      EmpresaDTO empresaDTO = new EmpresaDTO();
      empresaDTO.setDireccionDTO(new DireccionDTO());
      model.addAttribute("empresaDTO", empresaDTO);
      model.addAttribute("paises", paisServicio.listarPaisActivo());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "empresa/empresa-form";
  }

  @PostMapping("/crear")
  public String crearEmpresa(@Valid @ModelAttribute EmpresaDTO empresaDTO, BindingResult br, Model model, RedirectAttributes redirectAttributes) {
    try {
      if (br.hasErrors()) {
        model.addAttribute("empresaDTO", empresaDTO);
        model.addAttribute("paises", paisServicio.listarPaisActivo());
        return "empresa/empresa-form";
      }
      empresaServicio.crearEmpresaConDireccion(
        empresaDTO.getRazonSocial(),
        empresaDTO.getDireccionDTO().getCalle(),
        empresaDTO.getDireccionDTO().getNumeracion(),
        empresaDTO.getDireccionDTO().getBarrio(),
        empresaDTO.getDireccionDTO().getManzanaPiso(),
        empresaDTO.getDireccionDTO().getCasaDepartamento(),
        empresaDTO.getDireccionDTO().getReferencia(),
        empresaDTO.getDireccionDTO().getLocalidadId(),
        empresaDTO.getDireccionDTO().getLatitud(),
        empresaDTO.getDireccionDTO().getLongitud()
      );
      redirectAttributes.addFlashAttribute("success", "Empresa creada correctamente");
      return "redirect:/empresa/listar";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "empresa/empresa-form";
    }
  }

  @GetMapping("/editar/{id}")
  public String mostrarFormularioEditarEmpresa(@PathVariable String id, Model model) {
    try {
      Empresa emp = empresaServicio.buscarEmpresa(id);
      EmpresaDTO empresaDTO = EmpresaDTO.fromEntity(emp);
      model.addAttribute("empresaDTO", empresaDTO);
      model.addAttribute("paises", paisServicio.listarPaisActivo());
      return "empresa/empresa-form";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "redirect:/empresa/listar";
    }
  }

  @PostMapping("/actualizar/{id}")
  public String actualizarEmpresa(@PathVariable String id, @Valid @ModelAttribute EmpresaDTO empresaDTO
                                  , BindingResult br, Model model, RedirectAttributes redirectAttributes) {
    try {
      if (br.hasErrors()) {
        model.addAttribute("empresaDTO", empresaDTO);
        model.addAttribute("paises", paisServicio.listarPaisActivo());
        return "empresa/empresa-form";
      }
      empresaServicio.modificarEmpresaConDireccion(
        id,
        empresaDTO.getRazonSocial(),
        empresaDTO.getDireccionDTO().getCalle(),
        empresaDTO.getDireccionDTO().getNumeracion(),
        empresaDTO.getDireccionDTO().getBarrio(),
        empresaDTO.getDireccionDTO().getManzanaPiso(),
        empresaDTO.getDireccionDTO().getCasaDepartamento(),
        empresaDTO.getDireccionDTO().getReferencia(),
        empresaDTO.getDireccionDTO().getLocalidadId(),
        empresaDTO.getDireccionDTO().getLatitud(),
        empresaDTO.getDireccionDTO().getLongitud()
      );
      redirectAttributes.addFlashAttribute("success", "Empresa actualizada correctamente");
      return "redirect:/empresa/listar";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "empresa/empresa-form";
    }
  }

  @GetMapping("/eliminar/{id}")
  public String eliminarEmpresa(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
    try {
      empresaServicio.eliminarEmpresa(id);
      redirectAttributes.addFlashAttribute("success", "Empresa eliminada correctamente");
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/empresa/listar";
  }

}
