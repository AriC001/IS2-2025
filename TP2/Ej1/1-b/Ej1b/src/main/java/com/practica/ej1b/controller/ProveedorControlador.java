package com.practica.ej1b.controller;

import com.practica.ej1b.business.domain.dto.DireccionDTO;
import com.practica.ej1b.business.domain.dto.ProveedorDTO;
import com.practica.ej1b.business.domain.entity.Direccion;
import com.practica.ej1b.business.domain.entity.Proveedor;
import com.practica.ej1b.business.logic.service.DireccionServicio;
import com.practica.ej1b.business.logic.service.PaisServicio;
import com.practica.ej1b.business.logic.service.ProveedorServicio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/proveedor")
public class ProveedorControlador {

  @Autowired
  private ProveedorServicio proveedorServicio;

  @Autowired
  private DireccionServicio direccionServicio;

  @Autowired
  private PaisServicio paisServicio;

  @GetMapping("/listar")
  public String listarProveedores(Model model) {
    try{
      model.addAttribute("proveedores", proveedorServicio.listarProveedorActivo());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "proveedor/proveedor-list";
  }

  @GetMapping("/nuevo")
  public String mostrarFormularioNuevoProveedor(Model model) {
    try {
      ProveedorDTO proveedorDTO = new ProveedorDTO();
      proveedorDTO.setDireccionDTO(new DireccionDTO());
      model.addAttribute("proveedorDTO", proveedorDTO);
      model.addAttribute("paises", paisServicio.listarPaisActivo());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "proveedor/proveedor-form";
  }

  @PostMapping("/crear")
  public String crearProveedor(@Valid @ModelAttribute ProveedorDTO proveedorDTO, BindingResult br, Model model, RedirectAttributes redirectAttributes) {
    try {
      if (br.hasErrors()) {
        model.addAttribute("proveedorDTO", proveedorDTO);
        model.addAttribute("paises", paisServicio.listarPaisActivo());
        return "proveedor/proveedor-form";
      }

      Direccion direc = direccionServicio.crearDireccion(
        proveedorDTO.getDireccionDTO().getCalle(),
        proveedorDTO.getDireccionDTO().getNumeracion(),
        proveedorDTO.getDireccionDTO().getBarrio(),
        proveedorDTO.getDireccionDTO().getManzanaPiso(),
        proveedorDTO.getDireccionDTO().getCasaDepartamento(),
        proveedorDTO.getDireccionDTO().getReferencia(),
        proveedorDTO.getDireccionDTO().getLocalidadId(),
        proveedorDTO.getDireccionDTO().getLatitud(),
        proveedorDTO.getDireccionDTO().getLongitud()
      );

      proveedorServicio.crearProveedor(
        proveedorDTO.getNombre(),
        proveedorDTO.getApellido(),
        proveedorDTO.getCuit(),
        proveedorDTO.getTelefono(),
        proveedorDTO.getCorreoElectronico(),
        direc.getId()
      );
      redirectAttributes.addFlashAttribute("success", "Proveedor creado correctamente");
      return "redirect:/proveedor/listar";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "proveedor/proveedor-form";
    }
  }

  @GetMapping("/editar/{id}")
  public String mostrarFormularioEditarProveedor(@PathVariable String id, Model model) {
    try {
      Proveedor prov = proveedorServicio.buscarProveedor(id);
      ProveedorDTO proveedorDTO = ProveedorDTO.fromEntity(prov);
      model.addAttribute("proveedorDTO", proveedorDTO);
      model.addAttribute("paises", paisServicio.listarPaisActivo());
      return "proveedor/proveedor-form";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "redirect:/proveedor/listar";
    }
  }

  @PostMapping("/actualizar/{id}")
  public String actualizarProveedor(@PathVariable String id, @Valid @ModelAttribute ProveedorDTO proveedorDTO
                                  , BindingResult br, Model model, RedirectAttributes redirectAttributes) {
    try {
      if (br.hasErrors()) {
        model.addAttribute("proveedorDTO", proveedorDTO);
        model.addAttribute("paises", paisServicio.listarPaisActivo());
        return "proveedor/proveedor-form";
      }

      Direccion direc = direccionServicio.modificarDireccion(
        proveedorDTO.getDireccionDTO().getId(),
        proveedorDTO.getDireccionDTO().getCalle(),
        proveedorDTO.getDireccionDTO().getNumeracion(),
        proveedorDTO.getDireccionDTO().getBarrio(),
        proveedorDTO.getDireccionDTO().getManzanaPiso(),
        proveedorDTO.getDireccionDTO().getCasaDepartamento(),
        proveedorDTO.getDireccionDTO().getReferencia(),
        proveedorDTO.getDireccionDTO().getLocalidadId(),
        proveedorDTO.getDireccionDTO().getLatitud(),
        proveedorDTO.getDireccionDTO().getLongitud()
      );

      proveedorServicio.modificarProveedor(
        id,
        proveedorDTO.getNombre(),
        proveedorDTO.getApellido(),
        proveedorDTO.getTelefono(),
        proveedorDTO.getCorreoElectronico(),
        proveedorDTO.getCuit(),
        direc.getId()
      );
      redirectAttributes.addFlashAttribute("success", "Proveedor actualizado correctamente");
      return "redirect:/proveedor/listar";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "proveedor/proveedor-form";
    }
  }

  @GetMapping("/eliminar/{id}")
  public String eliminarProveedor(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
    try {
      proveedorServicio.eliminarProveedor(id);
      redirectAttributes.addFlashAttribute("success", "Proveedor eliminado correctamente");
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/proveedor/listar";
  }

}
