package edu.egg.tinder.controladores;


import edu.egg.tinder.entidades.Mascota;
import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.enumeracion.Sexo;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.servicios.MascotaServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/mascotas")
public class MascotaControlador {

        @Autowired
        private MascotaServicio mascotaServicio;

        @GetMapping("/eliminar-mascota")
        public String eliminar(HttpSession session, @RequestParam Long id) {

            try {
                Usuario login = (Usuario) session.getAttribute("usuariosession");
                mascotaServicio.eliminar(login.getId(), id);

            } catch (ErrorServicio ex) {
                Logger.getLogger(MascotaControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "redirect:/mascotas/mis-mascotas";
        }

        @GetMapping("/alta-mascota")
        public String darAlta(HttpSession session) {

                Usuario login = (Usuario) session.getAttribute("usuariosession");
                return "redirect:/mascotas/mascota";
            /*try {
               // mascotaServicio.crearMascota(login.getId());

            } catch (ErrorServicio ex) {
                Logger.getLogger(MascotaControlador.class.getName()).log(Level.SEVERE, null, ex);

            }
            return "redirect:/mascotas/mis-mascotas";*/
        }

    @GetMapping("/mis-mascotas")
    public String misMascotas(HttpSession session, ModelMap model) {

        try {

            Usuario login = (Usuario) session.getAttribute("usuariosession");
            if (login == null) {
                return "redirect:/login";
            }

            List<Mascota> mascotas = mascotaServicio.listarMascotas(login.getId());
            model.put("mascotas", mascotas);

            return "mascotas";

        }catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @GetMapping("/mascota")
    public String agregarMascota(Model model, HttpSession session) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("sexos", Sexo.values());
        if (login != null) {
            Mascota mascota = new Mascota(); // para el caso de "alta"
            model.addAttribute("mascota", mascota);
            model.addAttribute("accion", "Crear");
        }
        return "mascota";
    }

    //<a th:href="@{/mascota/editar-mascota(id=__${mascota.id}__,accion=Actualizar)}">Editar</a> -
    @GetMapping("/editar-mascota")
    public String editarMascota(@RequestParam Long id, @RequestParam(required = false) String accion, Model model, HttpSession session) throws ErrorServicio {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login != null) {
            Mascota mascota = mascotaServicio.buscarMascota(id);

            if (mascota != null) {
                model.addAttribute("mascota", mascota);
                model.addAttribute("sexos", Sexo.values());
                model.addAttribute("accion", (accion != null) ? accion : "Actualizar");
            }
            return "mascota";
        }
        return "redirect:/login";
    }

    @PostMapping("/actualizar-mascota")
    public String actualizarYCrearMacota(HttpSession session,@RequestParam(required = false) Long id,@RequestParam String nombre, @RequestParam Sexo sexo, @RequestParam MultipartFile archivo){
        System.out.println(id);
        try{
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            if(id != null){
                System.out.println("Hola");
                System.out.println(id);
                mascotaServicio.modificar(archivo, login.getId(), id,nombre,sexo,true);
            }else {
                mascotaServicio.crearMascota(login.getId(), nombre, sexo, archivo);
            }
        } catch (ErrorServicio e) {
            throw new RuntimeException(e);
        }
        return "redirect:/mascotas/mis-mascotas";
    }

    @GetMapping("/debaja-mascotas")
    public String mascotasInactivas(HttpSession session, ModelMap model){
            try {
                Usuario login = (Usuario) session.getAttribute("usuariosession");
                List<Mascota> mascotas;
                mascotas = mascotaServicio.listarMascotasInactivas(login.getId());
                model.put("mascotas", mascotas);
                return "mascotasdebaja";
            } catch (ErrorServicio e) {
                throw new RuntimeException(e);
            }
    }

    @PostMapping("/alta-mascota")
    public String altaMacota(HttpSession session,@RequestParam Long id,@RequestParam String nombre, @RequestParam Sexo sexo, @RequestParam(required = false) MultipartFile archivo){
        try{
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            mascotaServicio.modificar(archivo, login.getId(), id,nombre,sexo,true);

        } catch (ErrorServicio e) {
            throw new RuntimeException(e);
        }
        return "redirect:/mascotas/mis-mascotas";
    }
    @GetMapping("/votar-mascotas")
    public String votarMascotas(HttpSession session, Model modelo) {
        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            
            // Obtenemos las mascotas activas
            List<Mascota> mascotas = mascotaServicio.listarAllMascotas();
            
            modelo.addAttribute("mascotas", mascotas);

            return "votos";  // corresponde a votos.html o votos.jsp según uses Thymeleaf/JSP

        } catch (ErrorServicio e) {
            throw new RuntimeException(e);
            }
    }

}
