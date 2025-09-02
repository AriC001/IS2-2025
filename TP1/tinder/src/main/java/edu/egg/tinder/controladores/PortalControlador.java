package edu.egg.tinder.controladores;

import edu.egg.tinder.entidades.Zona;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.servicios.ZonaServicio;
import jakarta.servlet.http.HttpSession;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PortalControlador {
    @Autowired
    private ZonaServicio zonaServicio;

    @GetMapping("/")
    public String index(Model model) {
        return "index_1";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false)String error, @RequestParam(required = false)String logout, ModelMap model){
        if(error!=null){
            model.put("error", "Nombre de Usuario o clave incorrecto");
        }
        if(logout != null){
            model.put("logout", "Ha salido correctamente de la plataforma.");
        }
        return "/login.html";
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
