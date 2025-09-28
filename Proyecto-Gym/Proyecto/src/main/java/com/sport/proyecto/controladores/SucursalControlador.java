package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.Direccion;
import com.sport.proyecto.entidades.Empresa;
import com.sport.proyecto.entidades.Sucursal;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.servicios.SucursalServicio;
import com.sport.proyecto.servicios.DepartamentoServicio;
import com.sport.proyecto.servicios.DireccionServicio;
import com.sport.proyecto.servicios.EmpresaServicio;
import com.sport.proyecto.servicios.LocalidadServicio;
import com.sport.proyecto.servicios.PaisServicio;
import com.sport.proyecto.servicios.ProvinciaServicio;

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
  @Autowired
  private PaisServicio paisServicio;
  @Autowired
  private ProvinciaServicio provinciaServicio;
  @Autowired
  private LocalidadServicio localidadServicio;
  @Autowired
  private DepartamentoServicio departamentoServicio;

  @GetMapping("")
  public String listar(Model model) {
    try {
      System.out.println("controlador");
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
       // Para el subformulario de Dirección
      model.addAttribute("nuevaDireccion", new Direccion());
      model.addAttribute("paises", paisServicio.listarPaisActivo() );
      model.addAttribute("provincias", provinciaServicio.listarProvinciaActiva());
      model.addAttribute("departamentos",departamentoServicio.listarDepartamentoActivo());
      model.addAttribute("localidades", localidadServicio.listarLocalidadActiva());
      model.addAttribute("empresas", empresaServicio.listarEmpresaActiva());
      return "views/sucursal-form";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      return "redirect:/sucursal";
    }
  }
  /** 
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
  } */
  @PostMapping("/crear")
  public String crear(@ModelAttribute("sucursal") Sucursal sucursal,
                      @ModelAttribute("nuevaDireccion") Direccion nuevaDireccion,
                      @RequestParam(name = "crearNuevaDireccion", required = false) String crearNuevaDireccionFlag,
                      RedirectAttributes ra,
                      Model model) {

      System.out.println("==== DEBUG POST /sucursal/crear ====");
      System.out.println("Sucursal.nombre: " + sucursal.getNombre());
      System.out.println("Sucursal.empresa.id: " +
              (sucursal.getEmpresa() != null ? sucursal.getEmpresa().getId() : "null"));
      System.out.println("Sucursal.direccion.id (seleccionada): " +
              (sucursal.getDireccion() != null ? sucursal.getDireccion().getId() : "null"));
      System.out.println("Flag crearNuevaDireccion: " + crearNuevaDireccionFlag);
      System.out.println("NuevaDireccion.calle: " + nuevaDireccion.getCalle());
      System.out.println("NuevaDireccion.numeracion: " + nuevaDireccion.getNumeracion());

      try {
          Direccion direccionFinal = null;

          // 1. Si el usuario quiere CREAR NUEVA dirección
          if ("true".equalsIgnoreCase(crearNuevaDireccionFlag)) {
              if (nuevaDireccion.getCalle() == null || nuevaDireccion.getCalle().isBlank()) {
                  throw new Exception("Debe ingresar al menos calle y numeración para la nueva dirección.");
              }
              System.out.println(">>> Creando NUEVA dirección");
              direccionFinal = direccionServicio.guardarDireccion(nuevaDireccion);

          // 2. Si seleccionó una dirección existente
          } else if (sucursal.getDireccion() != null && sucursal.getDireccion().getId() != null) {
              System.out.println(">>> Usando dirección EXISTENTE ID=" + sucursal.getDireccion().getId());
              direccionFinal = direccionServicio.buscarDireccion(sucursal.getDireccion().getId());

          // 3. Si no eligió nada
          } else {
              throw new Exception("Debe seleccionar una dirección existente o crear una nueva.");
          }

          // Crear la sucursal
          sucursalServicio.crearSucursal(
                  sucursal.getNombre(),
                  sucursal.getEmpresa().getId(),
                  direccionFinal.getId()
          );

          ra.addFlashAttribute("msg", "Sucursal creada exitosamente");
          return "redirect:/sucursal";

      } catch (Exception e) {
          e.printStackTrace();
          ra.addFlashAttribute("error", e.getMessage());
          return "redirect:/sucursal/crear";
      }
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
