package com.practica.ej2consumer.controller.view;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import jakarta.servlet.http.HttpSession;

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

  @GetMapping("/login")
  public String loginPage() {
    return "login";
  }

  @GetMapping("/register")
  public String registerPage() {
    return "register";
  }

  // POST /login (consumer)
  @Autowired
  private RestTemplate restTemplate;

  @PostMapping("/login")
  public String doLogin(@RequestParam String dni, HttpSession session) {
    Map<String,String> payload = Map.of("dni", dni);
    try {
      var resp = restTemplate.postForObject("http://localhost:8081/auth/login", payload, Map.class);
      if (resp != null && resp.get("token") != null) {
        String token = (String) resp.get("token");
        session.setAttribute("jwtToken", token);
        return "redirect:/autores";
      } else {
        return "redirect:/login?error";
      }
    } catch (RestClientException e) {
      // Could not contact auth server or login failed
      return "redirect:/login?error";
    }
  }

  @PostMapping("/register")
  public String doRegister(@RequestParam String nombre,
                           @RequestParam String apellido,
                           @RequestParam String dni,
                           HttpSession session) {
      // build payload - minimal persona DTO
    var payload = Map.<String,Object>of(
    "nombre", nombre,
    "apellido", apellido,
    "dni", dni
    );
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<Map<String,Object>> request = new HttpEntity<>(payload, headers);
      restTemplate.postForObject("http://localhost:8081/api/v1/personas", request, Map.class);
      // on success, redirect to login with a flag
      return "redirect:/login?registered";
    } catch (Exception ex) {
      return "redirect:/register?error";
    }
  }
}