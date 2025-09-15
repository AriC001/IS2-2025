package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.Persona;
import com.sport.proyecto.servicios.PersonaServicio;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.RuntimeErrorException;

@Controller
public class InicioControlador {

    @Autowired
    private PersonaServicio personaServicio;

    @GetMapping({"/", "/index"})
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
            Persona persona = personaServicio.login(email, clave);
            if (persona == null) {
                model.put("error", "Nombre de usuario o clave incorrecto");
                return "login"; // se queda en la misma página
            }

            session.setAttribute("usuariosession", persona);
            return "redirect:/index"; // login exitoso
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
    public String registro(HttpSession session,Model model) {
        Persona login = null;
        if(session != null) {
            login = (Persona) session.getAttribute("usuariosession");
        }

        boolean esEmpleado = false; // por defecto false
        if(login != null && login.getId() != null){
            if(personaServicio.esAdmin(login.getId())){
                esEmpleado = true;
            }
        }

        model.addAttribute("esEmpleado", esEmpleado);
        //List<Pais> paises = paisServicio.mostrarPaises paises(); // trae todas
        //model.addAttribute("paises", paises); // <-- esto asegura que ${paises} exista en Thymeleaf
        //List<Provincia> provincias = provinciaServicio.mostrarProvincias provincias(); // trae todas
        //model.addAttribute("provincias", provincias);
        //List<Localidad> localidades = localidadServicio.mostrarLocalidades localidades(); // trae todas
        //model.addAttribute("localidades", localidades);
        return "/registro";
    }

}
