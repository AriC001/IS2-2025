package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.Persona;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.servicios.PersonaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/persona")
public class PersonaControlador {
    @Autowired
    private PersonaServicio personaServicio;

    @PostMapping("/registro")
    public String registro(ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String email, @RequestParam String clave1, @RequestParam String clave2, @RequestParam boolean esEmpleado) throws ErrorServicio {
        try{

            System.out.println("Registro "+nombre + " " + email + " " + clave1 + " " + esEmpleado);
            Persona p = personaServicio.registro(nombre,apellido,email,clave1,clave2,esEmpleado,null);
            modelo.put("titulo", "Bienvenido a Gimnasio Sport ");
            modelo.put("descripcion", "Tu usuario fue registrado de manera satisfactoria. ");
            return "views/login";
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("email", email);
            modelo.put("clave1", clave1);
            modelo.put("clave2", clave2);
            return "redirect:/registro";
        }

    }

}
