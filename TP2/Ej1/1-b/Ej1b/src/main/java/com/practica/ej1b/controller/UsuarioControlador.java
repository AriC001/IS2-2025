package com.practica.ej1b.controller;

import com.practica.ej1b.business.domain.dto.UsuarioDTO;
import com.practica.ej1b.business.domain.entity.Usuario;
import com.practica.ej1b.business.logic.service.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

  @Autowired
  private UsuarioServicio usuarioServicio;

  @GetMapping("/listar")
  public String listarUsuarios(Model model) {
    try {
      model.addAttribute("usuarios", usuarioServicio.listarUsuarioActivo());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "usuario/usuario-list";
  }

  @GetMapping("/nuevo")
  public String mostrarFormularioNuevoUsuario(Model model) {
    try {
      model.addAttribute("usuarioDTO", new UsuarioDTO());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "usuario/usuario-form";
  }

  @PostMapping("/crear")
  public String crearUsuario(@ModelAttribute UsuarioDTO usuarioDTO, Model model, RedirectAttributes redirectAttributes) {
    try {
      usuarioServicio.crearUsuario(
          usuarioDTO.getNombreUsuario(),
          usuarioDTO.getContrasenia(),
          usuarioDTO.getNombre(),
          usuarioDTO.getApellido(),
          usuarioDTO.getCorreoElectronico(),
          usuarioDTO.getTelefono()
      );
      redirectAttributes.addFlashAttribute("success", "Usuario creado correctamente");
      return "redirect:/usuario/listar";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "usuario/usuario-form";
    }
  }

  @GetMapping("/editar/{id}")
  public String mostrarFormularioEditarUsuario(@PathVariable String id, Model model) {
    try {
      Usuario user = usuarioServicio.buscarUsuario(id);
      UsuarioDTO usuarioDTO = UsuarioDTO.fromEntity(user);
      model.addAttribute("usuarioDTO", usuarioDTO);
      return "usuario/usuario-form";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "redirect:/usuario/listar";
    }
  }

  @PostMapping("/actualizar/{id}")
  public String actualizarUsuario(@PathVariable String id, @ModelAttribute UsuarioDTO usuarioDTO,
                                  RedirectAttributes redirectAttributes, Model model) {
    try{
      usuarioServicio.modificarUsuario(
          id,
          usuarioDTO.getNombreUsuario(),
          usuarioDTO.getContrasenia(),
          usuarioDTO.getNombre(),
          usuarioDTO.getApellido(),
          usuarioDTO.getCorreoElectronico(),
          usuarioDTO.getTelefono()
      );
      redirectAttributes.addFlashAttribute("success", "Usuario actualizado correctamente");
      return "redirect:/usuario/listar";
    }catch(Exception e){
      model.addAttribute("error", e.getMessage());
      return "usuario/usuario-form";
    }
  }

  @GetMapping("/eliminar/{id}")
  public String eliminarUsuario(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
    try {
      usuarioServicio.eliminarUsuario(id);
      redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente");
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/usuario/listar";
  }

  @GetMapping("/cambioContraseña/{id}")
  public String mostrarFormularioCambioContrasenia(@PathVariable String id, Model model) {
    try {
      Usuario user = usuarioServicio.buscarUsuario(id);
      UsuarioDTO usuarioDTO = UsuarioDTO.fromEntity(user);
      model.addAttribute("usuarioDTO", usuarioDTO);
      return "usuario/password";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "redirect:/usuario/listar";
    }
  }

  @PostMapping("/actualizarContraseña/{id}")
  public String actualizarContrasenia(@PathVariable String id, @RequestParam String contraseniaActual, String nuevaContrasenia
    , String nuevaConstraseniaConfirmada, Model model, RedirectAttributes redirectAttributes) {
    try {
      usuarioServicio.cambiarContrasenia(id, contraseniaActual, nuevaContrasenia, nuevaConstraseniaConfirmada);
      redirectAttributes.addFlashAttribute("success", "Contraseña actualizada correctamente");
      return "redirect:/usuario/listar";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "usuario/password";
    }
  }

}
