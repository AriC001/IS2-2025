package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.Empleado;
import com.sport.proyecto.enums.tipoDocumento;
import com.sport.proyecto.servicios.EmpleadoServicio;
import com.sport.proyecto.servicios.PersonaServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/empleado")
public class EmpleadoControlador {
  @Autowired
  private EmpleadoServicio empleadoServicio;

  @GetMapping("")
  public String empleados(Model model, HttpSession session) {
    try{
      List<Empleado> empleados = empleadoServicio.listaEmpleadosActivos();
      model.addAttribute("empleados", empleados);
      model.addAttribute("usuariosession", session);
      return "views/empleados";

    } catch (Exception e){
      model.addAttribute("error", e.getMessage());
      return "views/empleados";
    }
  }

  @GetMapping("/nuevo")
  public String nuevoEmpleado(Model model) {
    try {
      return "views/empleado-formulario";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "redirect:/empleado";
    }
  }



}
