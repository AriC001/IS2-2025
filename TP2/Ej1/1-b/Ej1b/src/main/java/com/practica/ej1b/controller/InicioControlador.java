package com.practica.ej1b.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.practica.ej1b.business.logic.service.UsuarioServicio;
import com.practica.ej1b.business.domain.entity.Usuario;

@Controller
public class InicioControlador {

  @Autowired
  private UsuarioServicio usuarioServicio;

  @Autowired
  private PasswordEncoder encoder;

  @GetMapping({ "/","/index", "/home"})
  public String showHomePage() {
    return "index";
  }

  @GetMapping("/login")
  public String showLoginForm() {
    return "login";
  }

  @GetMapping("/logout")
  public String logout() {
    return "redirect:/login?logout";
  }

  @PostMapping("/login")
  public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
    Usuario usuario = null;
    try {
      usuario = usuarioServicio.buscarUsuarioPorNombre(username);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    if (usuario != null && encoder.matches(password, usuario.getContrasenia())) {
      session.setAttribute("usuario", usuario);
      return "redirect:/index";
    } else {
      model.addAttribute("error", "Usuario o contraseña incorrectos");
      return "login";
    }
  }
}
