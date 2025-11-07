package com.practica.nexora.ej6_e.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.practica.nexora.ej6_e.business.domain.dto.PersonaDTO;
import com.practica.nexora.ej6_e.business.domain.dto.UsuarioDTO;
import com.practica.nexora.ej6_e.business.domain.entity.Persona;
import com.practica.nexora.ej6_e.business.domain.entity.Usuario;
import com.practica.nexora.ej6_e.business.logic.service.UsuarioService;

@Controller
@RequestMapping("/auth")
public class AuthController {

  private final UsuarioService usuarioService;

  public AuthController(UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  @PostMapping("/register")
  public String registrarUsuario(
      @ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO,
      @RequestParam String confirmarClave,
      Model model) {

    try {
      // Separar los datos de persona y usuario
      PersonaDTO personaDTO = usuarioDTO.getPersona();
      
      // Crear las entidades
      Usuario usuario = new Usuario();
      usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());
      usuario.setClave(usuarioDTO.getClave());
      
      Persona persona = new Persona();
      persona.setNombre(personaDTO.getNombre());
      persona.setApellido(personaDTO.getApellido());
      
      // Registrar usuario con persona asociada
      usuarioService.registrarUsuarioConPersona(persona, usuario, confirmarClave);
      
      model.addAttribute("success", "Usuario registrado exitosamente. Por favor inicia sesión.");
      return "login";
    } catch (IllegalArgumentException e) {
      model.addAttribute("error", e.getMessage());
      model.addAttribute("usuarioDTO", usuarioDTO);
      return "register";
    } catch (Exception e) {
      model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
      model.addAttribute("usuarioDTO", usuarioDTO);
      return "register";
    }
  }
}
