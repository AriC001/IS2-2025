package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.Empresa;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.servicios.EmpresaServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/empresa")
public class EmpresaControlador {

  @Autowired
  private EmpresaServicio empresaServicio;

  @GetMapping("")
  public String listar(Model model) {
    try {
      model.addAttribute("empresas", empresaServicio.listarEmpresaActiva());
      return "views/empresas";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "views/empresas";
    }
  }

  @GetMapping("/nueva")
  public String nueva(Model model, RedirectAttributes redirectAttributes) {
    try {
      model.addAttribute("empresa", new Empresa());
      return "views/empresa-form";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al crear nueva empresa");
      return "redirect:/empresa";

    }
  }

  @PostMapping("/guardar")
  public String guardar(@ModelAttribute("empresa") Empresa empresa, Model model, RedirectAttributes redirectAttributes) {
    try {
      empresaServicio.crearEmpresa(empresa.getNombre(), empresa.getTelefono(), empresa.getCorreoElectronico());
      redirectAttributes.addFlashAttribute("msg", "Empresa creada con exito");
      return "redirect:/empresa";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "views/empresa-form";
    }
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
    try {
      Empresa empresa = empresaServicio.buscarEmpresa(id);
      model.addAttribute("empresa", empresa);
      return "views/empresa-form";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al cargar la empresa");
      return "redirect:/empresa";
    }
  }

  @PostMapping("/actualizar/{id}")
  public String actualizar(@PathVariable String id, @ModelAttribute("empresa") Empresa empresa, RedirectAttributes redirectAttributes, Model model) {
    try {
      empresaServicio.modificarEmpresa(id, empresa.getNombre(), empresa.getTelefono(), empresa.getCorreoElectronico());
      redirectAttributes.addFlashAttribute("msg", "Empresa actualizada con exito");
      return "redirect:/empresa";
    } catch (Exception e) {
      model.addAttribute("error", "Error al actualizar la empresa");
      return "views/empresa-form";
    }
  }

  @GetMapping("/eliminar/{id}")
  public String eliminar(@PathVariable String id, RedirectAttributes redirectAttributes) {
    try {
      empresaServicio.eliminarEmpresa(id);
      redirectAttributes.addFlashAttribute("msg", "Empresa eliminada con exito");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al eliminar la empresa");
    }
    return "redirect:/empresa";
  }

  @ModelAttribute("usuariosession")
  public Usuario usuarioSession(HttpSession session) {
    return (Usuario) session.getAttribute("usuariosession");
  }

}
