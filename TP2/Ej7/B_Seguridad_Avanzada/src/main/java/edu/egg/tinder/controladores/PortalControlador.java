package edu.egg.tinder.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.entidades.Zona;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.servicios.UsuarioServicio;
import edu.egg.tinder.servicios.ZonaServicio;
import jakarta.servlet.http.HttpSession;

@Controller
public class PortalControlador {
    @Autowired
    private ZonaServicio zonaServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String index(Model model) {
        return "index.html";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        return "login.html"; // devuelve el HTML de login
    }

    // ❌ COMENTADO: Spring Security ahora maneja el POST /logincheck automáticamente
    // No necesitamos implementarlo manualmente
    // @PostMapping("/login")
    // public String loginUsuario(...) { ... }

    // ❌ COMENTADO: Spring Security maneja /logout (POST) automáticamente
    // Este GET /logout ya no se usa
    // @GetMapping("/logout")
    // public String logout(HttpSession session) { ... }

    @GetMapping("/registro")
    public String registro(Model model) throws ErrorServicio {
        List<Zona> zonas = zonaServicio.mostrarZonas(); // trae todas las zonas
        model.addAttribute("zonas", zonas); // <-- esto asegura que ${zonas} exista en Thymeleaf
        return "registro.html"; // Thymeleaf tomará ${zonas}
    }

    @GetMapping("/inicio")
    public String inicio(Model model) {
        return "inicio.html"; // Thymeleaf busca inicio.html en templates/
    }

    @GetMapping("/mascotas")
    public String mascotas(Model model){
        return "mascotas.html";
    }

}
