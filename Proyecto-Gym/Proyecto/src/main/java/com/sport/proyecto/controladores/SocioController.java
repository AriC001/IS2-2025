package com.sport.proyecto.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sport.proyecto.entidades.Socio;
import com.sport.proyecto.servicios.SocioServicio;


@Controller
@RequestMapping("/socio")
public class SocioController {

  @Autowired
  private SocioServicio socioService;

  @GetMapping("")
  public String listar(ModelMap model) {

    model.addAttribute("socios", socioService.listarSocioActivos());
    return "views/socios";
  }
  

}
