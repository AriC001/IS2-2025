package com.practica.nexora.ej6_e.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.practica.nexora.ej6_e.business.domain.dto.UsuarioDTO;

@Controller
public class HomeController {

  @GetMapping("/")
  public String root() {
    return "redirect:/inicio";
  }

  @GetMapping("/inicio")
  public String inicio() {
    return "inicio";
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping("/register")
  public String register(Model model) {
    UsuarioDTO usuarioDTO = new UsuarioDTO();
    usuarioDTO.setPersona(new com.practica.nexora.ej6_e.business.domain.dto.PersonaDTO());
    model.addAttribute("usuarioDTO", usuarioDTO);
    return "register";
  }

  @GetMapping("/index")
  public String index() {
    return "index";
  }
}
