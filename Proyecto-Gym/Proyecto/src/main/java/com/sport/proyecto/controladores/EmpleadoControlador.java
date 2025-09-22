package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.Empleado;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.enums.tipoDocumento;
import com.sport.proyecto.servicios.EmpleadoServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/empleado")
public class EmpleadoControlador {
  @Autowired
  private EmpleadoServicio empleadoServicio;

  @GetMapping("")
  public String empleados(Model model) {
    try{
      List<Empleado> empleados = empleadoServicio.listaEmpleadosActivos();
      model.addAttribute("empleados", empleados);
      return "views/empleados";

    } catch (Exception e){
      model.addAttribute("error", e.getMessage());
      return "views/empleados";
    }
  }

  @GetMapping("/guardar")
  public String empleadoForm(Model model) {
    try {
      return "views/empleado-formulario";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "redirect:/empleado";
    }
  }

  /*@PostMapping("/guardar")
  public String guardarEmpleado(Model model, @RequestParam tipoDocumento tipoDocumento,
                               @RequestParam String nombre, @RequestParam String apellido,
                               @RequestParam String numeroDocumento, @RequestParam String telefono,
                               @RequestParam String correoElectronico, @RequestParam String fechaNacimiento,
                               @RequestParam String tipoEmpleado, @RequestParam String idSucursal,
                               @RequestParam Usuario usuario) {
    try {
      empleadoServicio.guardarEmpleado(nombre, apellido, UtilServicio.stringADate(fechaNacimiento), tipoDocumento, numeroDocumento, telefono, correoElectronico, com.sport.proyecto.enums.tipoEmpleado.valueOf(tipoEmpleado), idSucursal, usuario);
      model.addAttribute("exito", "Empleado guardado con éxito");
      return "redirect:/empleado";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "views/empleado-formulario";
    }
  }*/


  @ModelAttribute("usuariosession")
  public Usuario usuarioSession(HttpSession session) {
    return (Usuario) session.getAttribute("usuariosession");
  }


}
