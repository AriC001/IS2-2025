package com.example.medicine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.example.medicine.services.UsuarioServicio;
import com.example.medicine.services.UtilServicio;
import ch.qos.logback.classic.pattern.Util;
import jakarta.servlet.http.HttpSession;

import com.example.medicine.errors.ErrorServicio;
import com.example.medicine.model.Usuario;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioServicio usuarioServicio;

     @PostMapping("/registrar")
    public String registroUsuario(ModelMap modelo, @RequestParam String nombreUsuario, @RequestParam String clave1, @RequestParam String clave2) throws ErrorServicio {
        try{
            Usuario usuario = usuarioServicio.registrar(nombreUsuario, clave1, clave2);
            modelo.put("descripcion", "Tu usuario fue registrado de manera satisfactoria. ");
            return "redirect:/usuarios/login";
        } catch (ErrorServicio e) {
        modelo.put("error", e.getMessage());
        modelo.put("nombre", nombreUsuario );
        modelo.put("clave1", clave1);
        modelo.put("clave2", clave2);
      
            return "redirect:/views/registro";
        }
    
    }
    @GetMapping("/registro")
        public String registro() {
            return "views/registro";
        }


    @GetMapping("/nuevo")
        public String nuevoUsuario() {
            return "views/registro";
        }
    @GetMapping("/login")
        public String login() {
            return "index";
        }
    @GetMapping("/home")
    public String home(){

        return "views/home";
    }

    @PostMapping("/login")
    public String loginUsuario(@RequestParam String nombreUsuario, @RequestParam String clave, ModelMap modelo,  HttpSession session) {
        try {
            Usuario usuario = usuarioServicio.buscarUsuarioPorNombre(nombreUsuario);
            if (usuario != null && UtilServicio.validarClave(clave, usuario.getClave())) {
                modelo.put("titulo", "Bienvenido de nuevo " + usuario.getNombreUsuario());
                session.setAttribute("usuariosession", usuario);
                return "views/home";
            } else {
                modelo.put("error", "Nombre de usuario o clave incorrectos.");
                return "redirect:/usuarios/login";
            }
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            return "redirect:/usuarios/login";
        }
    }

    

}