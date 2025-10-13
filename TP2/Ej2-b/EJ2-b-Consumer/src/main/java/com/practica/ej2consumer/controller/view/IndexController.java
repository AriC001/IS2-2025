package com.practica.ej2consumer.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

  @GetMapping
  public String index(Model model) {
    model.addAttribute("title", "Sistema de Gestión");
    model.addAttribute("message", "Bienvenido al sistema de gestión");
    return "home/index";
  }

  @GetMapping("/home")
  public String home(Model model) {
    model.addAttribute("title", "Inicio");
    model.addAttribute("message", "Bienvenido al sistema de gestión");
    return "home/index";
  }
}