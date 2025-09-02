package edu.egg.tinder.controladores;


import edu.egg.tinder.entidades.Mascota;
import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.enumeracion.Sexo;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.servicios.MascotaServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/mascotas")
public class MascotaControlador {

        @Autowired
        private MascotaServicio mascotaServicio;

        @PostMapping("/eliminar-perfil")
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

    /*
    @GetMapping("/mascota/editar/{id}")
    public String editarMascota(@PathVariable Long id, Model model, HttpSession session) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login != null) {
            Mascota mascota = mascotaServicio.modificar();
            model.addAttribute("mascota", mascota);
            model.addAttribute("sexos", Sexo.values());
            return "mascota";
        }
        return "redirect:/login";
    }*/

    @PostMapping("/actualizar-mascota")
    public String actualizarYCrearMacota(HttpSession session,@RequestParam String nombre, @RequestParam Sexo sexo, @RequestParam MultipartFile archivo){
        try{
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            mascotaServicio.crearMascota(login.getId(), nombre,sexo,archivo);
        } catch (ErrorServicio e) {
            throw new RuntimeException(e);
        }
        return "redirect:/mascotas/mis-mascotas";
    }

}
