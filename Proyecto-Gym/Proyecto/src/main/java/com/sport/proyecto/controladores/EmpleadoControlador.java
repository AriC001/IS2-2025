package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.Empleado;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.enums.tipoDocumento;
import com.sport.proyecto.enums.tipoEmpleado;
import com.sport.proyecto.servicios.EmpleadoServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.sport.proyecto.servicios.UtilServicio;

import java.util.List;

@Controller
@RequestMapping("/empleados")
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


  @GetMapping("/nuevo")
  public String nuevoEmpleado(Model model){
    model.addAttribute("empleado", new Empleado());
    model.addAttribute("tiposEmpleado", tipoEmpleado.values());
    model.addAttribute("tiposDocumento", tipoDocumento.values());
    return "views/alta_empleado";
  }


  @PostMapping("/crear")
  public String crearEmpleado(@ModelAttribute("empleado") Empleado empleado, ModelMap model) {
    /*if (empleado.getFechaNacimiento() != null) {
      model.addAttribute("fechaNacimientoStr", empleado.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }*/
    try {
      empleadoServicio.crearEmpleado(
              empleado.getNombre(),
              empleado.getApellido(),
              empleado.getTipoDocumento(),
              empleado.getNumeroDocumento(),
              empleado.getFechaNacimiento(),
              empleado.getTelefono(),
              empleado.getEmail(),
              empleado.getTipoEmpleado()
      );

      model.addAttribute("msg", "Empleado creado exitosamente");
      return "redirect:/empleados/lista"; // redirige a la lista de empleados
    } catch (Exception e) {
      model.put("error", e.getMessage());
      model.addAttribute("empleado", empleado);
      model.addAttribute("tiposEmpleado", tipoEmpleado.values());
      model.addAttribute("tiposDocumento", tipoDocumento.values());
      return "views/alta_empleado"; // vuelve al formulario con error
    }
  }

  @GetMapping("/guardar")
  public String empleadoForm(Model model) {
    model.addAttribute("empleado", new Empleado());
    model.addAttribute("tiposDni", tipoDocumento.values());
    return "views/empleado-formulario";
  }



/*
  @PostMapping("/guardar")
  public String guardarEmpleado(@RequestParam String nombre, @RequestParam String apellido,@RequestParam tipoDocumento tipoDocuemento,@RequestParam String numeroDocumento,@RequestParam String dia,
                                @RequestParam String mes,
                                @RequestParam String anio,@RequestParam String telefono,@RequestParam String correoElectronico,@RequestParam tipoEmpleado tipoEmpleado,@RequestParam String idSucursal, Usuario usuario, @ModelAttribute Empleado empleado, Model model) {
    try {
      empleadoServicio.guardarEmpleado(nombre,apellido,dia,mes,anio,tipoDocuemento,numeroDocumento,telefono, correoElectronico, tipoEmpleado, idSucursal, usuario);  // ajustá según tu servicio
      model.addAttribute("exito", "Empleado guardado con éxito");
      return "redirect:/empleados";
    } catch (Exception e) {
      model.addAttribute("errorMessage", e.getMessage());
      model.addAttribute("tiposDni", tipoDocumento.values());
      return "views/empleado-formulario";
    }
  }
*/
/*
  @GetMapping("/empleados/formulario")
  public String mostrarFormulario(@RequestParam(required = false) Long id, Model model) {
    if (id != null) {
      Empleado empleado = empleadoServicio.buscarPorId(id);
      model.addAttribute("empleado", empleado);
    } else {
      model.addAttribute("empleado", new Empleado()); // para el caso nuevo
    }
    model.addAttribute("tiposDni", TipoDni.values());
    return "views/empleado-formulario";
  }
*/


  @ModelAttribute("usuariosession")
  public Usuario usuarioSession(HttpSession session) {
    return (Usuario) session.getAttribute("usuariosession");
  }


}
