package com.example.taller.controller;

import com.example.taller.dto.UsuarioSafeDTO;
import com.example.taller.error.ErrorServicio;
import com.example.taller.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InicioController {

    @Autowired
    private final UsuarioService usuarioService;

    public InicioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    //@PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        model.addAttribute("taller", "Bienvenido al Sistema");

        UsuarioSafeDTO usuario = (UsuarioSafeDTO) session.getAttribute("usuariosession");

        if (usuario != null) {
            if (usuario.getRol() != null && usuario.getRol().toString().equals("ADMIN")) {
                return "redirect:/admin/panel";
            } else {
                return "index"; // usar plantilla existente en templates/index.html
            }
        } else {
            return "index"; // si no hay usuario en sesión mostrar index
        }

    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("taller", "Registro de Usuario");
        System.out.println("entra a registro");
        return "views/register";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("taller", "Login de Usuario");
        return "views/login";
    }

    @PostMapping("/register")
    public String registro(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String nombreUsuario,
            @RequestParam String clave,
            @RequestParam String confirmPassword,
            Model model) throws ErrorServicio {
        try {
            usuarioService.registrar(clave, nombreUsuario, confirmPassword);
            model.addAttribute("exito", "Registro exitoso. Por favor, inicie sesión.");
            return "views/login";
        } catch (ErrorServicio e) {
            model.addAttribute("msgError", e.getMessage());
            model.addAttribute("nombre", nombre);
            model.addAttribute("apellido", apellido);
            return "views/register";
        }
    }
}
