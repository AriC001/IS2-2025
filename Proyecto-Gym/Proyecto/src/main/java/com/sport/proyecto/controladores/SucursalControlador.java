package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.Sucursal;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.servicios.SucursalServicio;
import com.sport.proyecto.servicios.DireccionServicio;
import com.sport.proyecto.servicios.EmpresaServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/sucursal")
public class SucursalControlador {
  @Autowired
  private SucursalServicio sucursalServicio;
  @Autowired
  private DireccionServicio direccionServicio;
  @Autowired
  private EmpresaServicio empresaServicio;

  @GetMapping("")
  public String listar(Model model) {
    try {
      model.addAttribute("sucursales", sucursalServicio.listarSucursalActivas());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "views/sucursales";
  }

  @GetMapping("/nueva")
  public String nueva(Model model, RedirectAttributes redirectAttributes) {
    try {
      model.addAttribute("sucursal", new Sucursal());
      model.addAttribute("direcciones", direccionServicio.listarDireccionActiva());
      model.addAttribute("empresas", empresaServicio.listarEmpresaActiva());
      return "views/sucursal-form";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      return "redirect:/sucursal";
    }
  }

  @PostMapping("/crear")
  public String crear(@ModelAttribute("sucursal") Sucursal sucursal,
                      RedirectAttributes redirectAttributes) {
    try {
      System.out.println(">>> Empresa ID: " + sucursal.getEmpresa().getId());
      System.out.println(">>> Direccion ID: " + sucursal.getDireccion().getId());

      sucursalServicio.crearSucursal(
        sucursal.getNombre(),
        sucursal.getEmpresa().getId(),
        sucursal.getDireccion().getId()
      );
      redirectAttributes.addFlashAttribute("msg", "Sucursal guardada exitosamente");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/sucursal";
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
    try {
      Sucursal sucursal = sucursalServicio.buscarSucursal(id);
      model.addAttribute("sucursal", sucursal);
      model.addAttribute("direcciones", direccionServicio.listarDireccionActiva());
      model.addAttribute("empresas", empresaServicio.listarEmpresaActiva());
      return "views/sucursal-form";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      return "redirect:/sucursal";
    }
  }

  @PostMapping("/actualizar/{id}")
  public String actualizar(@PathVariable String id,
                           @ModelAttribute("sucursal") Sucursal sucursal,
                           RedirectAttributes redirectAttributes) {
    try {
      sucursalServicio.modificarSucursal(
        id,
        sucursal.getNombre(),
        sucursal.getEmpresa().getId(),
        sucursal.getDireccion().getId()
      );
      redirectAttributes.addFlashAttribute("msg", "Sucursal actualizada exitosamente");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/sucursal";
  }

  @GetMapping("/eliminar/{id}")
  public String eliminar(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
    try {
      sucursalServicio.eliminarSucursal(id);
      redirectAttributes.addFlashAttribute("msg", "Sucursal eliminada exitosamente");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/sucursal";
  }

  @ModelAttribute("usuariosession")
  public Usuario usuarioSession(HttpSession session) {
    return (Usuario) session.getAttribute("usuariosession");
  }
}
