package com.sport.proyecto.controladores;

import com.sport.proyecto.servicios.PersonaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/empleados")
public class EmpleadoControlador {
  @Autowired
  private PersonaServicio personaServicio;

  @GetMapping("")
  public String empleados(){
    return "views/empleados";
  }

  @GetMapping("/nuevo")
  public String nuevoEmpleado(){
    return "views/empleados";
  }

}
