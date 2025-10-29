package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.servicios.PersonaServicio;
import com.sport.proyecto.servicios.UsuarioServicio;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class InicioControlador {

    @Autowired
    private PersonaServicio personaServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        return "index";
    }

    /*@GetMapping("/error")
    public String error(Model model){
        return "views/error";
    }*/

    @GetMapping("/login")
    public String loginForm(Model model) {
        return "views/login"; // devuelve el HTML de login
    }

    // REMOVER ESTE MÉTODO - Spring Security maneja el login automáticamente
    /*@PostMapping("/login")
    public String loginUsuario(@RequestParam String nombreUsuario, @RequestParam String clave, ModelMap model, HttpSession session) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            if (usuario == null) {
                model.put("error", "Nombre de usuario o clave incorrecto");
                return "views/login"; // se queda en la misma página
            }

            session.setAttribute("usuariosession", usuario);
            return "redirect:/index"; // login exitoso
        } catch (Exception e) {
            model.put("error", "Error inesperado: " + e.getMessage());
            return "views/login";
        }
    }*/


/*    @PostMapping("/login")
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
    }*/


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/index";
    }

    @GetMapping("/registro")
    public String registro(HttpSession session,Model model) {
        Usuario login = null;
        if(session != null) {
            login = (Usuario) session.getAttribute("usuariosession");
        }

        boolean esEmpleado = false; // por defecto false
        if(login != null && login.getId() != null){
            if(usuarioServicio.esAdmin(login.getId())){
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
        return "views/registro";
    }

    @ModelAttribute("usuariosession")
    public Usuario usuarioSession(HttpSession session) {
        return (Usuario) session.getAttribute("usuariosession");
    }

}
