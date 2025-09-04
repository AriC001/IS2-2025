package edu.egg.tinder.controladores;

import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.entidades.Zona;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.servicios.UsuarioServicio;
import edu.egg.tinder.servicios.ZonaServicio;
import jakarta.servlet.http.HttpSession;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PortalControlador {
    @Autowired
    private ZonaServicio zonaServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        return "login"; // devuelve el HTML de login
    }

    @PostMapping("/login")
    public String loginUsuario(@RequestParam String email,
                               @RequestParam String clave,
                               ModelMap model,
                               HttpSession session) {
        try {
            Usuario usuario = usuarioServicio.login(email, clave);
            if (usuario == null) {
                model.put("error", "Nombre de usuario o clave incorrecto");
                return "login"; // se queda en la misma página
            }

            session.setAttribute("usuariosession", usuario);
            return "redirect:/inicio"; // login exitoso
        } catch (Exception e) {
            model.put("error", "Error inesperado: " + e.getMessage());
            return "login";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.setAttribute("usuariosession", null);
        return "redirect:/inicio";
    }

    @GetMapping("/registro")
    public String registro(Model model) throws ErrorServicio {
        List<Zona> zonas = zonaServicio.mostrarZonas(); // trae todas las zonas
        model.addAttribute("zonas", zonas); // <-- esto asegura que ${zonas} exista en Thymeleaf
        return "/registro"; // Thymeleaf tomará ${zonas}
    }

    @GetMapping("/inicio")
    public String inicio(Model model) {
        return "inicio"; // Thymeleaf busca inicio.html en templates/
    }

    @GetMapping("/mascotas")
    public String mascotas(Model model){
        return "mascotas";
    }
}
