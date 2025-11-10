package edu.egg.tinder.controladores;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.egg.tinder.entidades.Mascota;
import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.entidades.Voto;
import edu.egg.tinder.enumeracion.Sexo;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.servicios.MascotaServicio;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mascotas")
public class MascotaControlador {

        @Autowired
        private MascotaServicio mascotaServicio;

        @PostMapping("/eliminar-mascota")
        public String eliminar(HttpSession session, @RequestParam Long id) {

            try {
                Usuario login = (Usuario) session.getAttribute("usuariosession");
                mascotaServicio.eliminar(login.getId(), id);

            } catch (ErrorServicio ex) {
                Logger.getLogger(MascotaControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "redirect:/mascotas/mis-mascotas";
        }
        
        @GetMapping("/baja-mascota")
        public String bajaMascota(@RequestParam Long id, Model model, HttpSession session) throws ErrorServicio {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            if (login != null) {
                Mascota mascota = mascotaServicio.buscarMascota(id);

                if (mascota != null) {
                    model.addAttribute("mascota", mascota);
                    model.addAttribute("sexos", Sexo.values());
                    model.addAttribute("accion", "Eliminar");
                }
                return "mascota";
            }
            return "redirect:/login";
        }
        
        @GetMapping("/activar-mascota")
        public String activarMascota(@RequestParam Long id, Model model, HttpSession session) throws ErrorServicio {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            if (login != null) {
                Mascota mascota = mascotaServicio.buscarMascota(id);

                if (mascota != null) {
                    model.addAttribute("mascota", mascota);
                    model.addAttribute("sexos", Sexo.values());
                    model.addAttribute("accion", "Alta");
                }
                return "mascota";
            }
            return "redirect:/login";
        }

        @GetMapping("/alta-mascota")
        public String darAlta(HttpSession session) {
            
            return "redirect:/mascotas/mascota";
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
    public String actualizarMascota(HttpSession session,@RequestParam(required = false) Long id,@RequestParam String nombre, @RequestParam Sexo sexo, @RequestParam MultipartFile archivo){
        System.out.println(id);
        try{
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            
            System.out.println("Hola");
            System.out.println(id);
            mascotaServicio.modificar(archivo, login.getId(), id,nombre,sexo,true);
            
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
            mascotaServicio.crearMascota(id, nombre, sexo, archivo);

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

            return "mascotasvotar";  // corresponde a votos.html

        } catch (ErrorServicio e) {
            throw new RuntimeException(e);
            }
    }

    @GetMapping("/votos")
    public String votosRecibidos(@RequestParam Long id, ModelMap model) throws ErrorServicio {
        Mascota mascota = mascotaServicio.buscarMascota(id);

        List<Mascota> votantes = mascotaServicio.votantesDe(mascota);
        List<Voto> votos = mascotaServicio.votosDe(mascota);

        // Marcamos si hubo voto recíproco
        votantes.forEach(m -> m.setVotoReciproco(mascotaServicio.huboVotoReciproco(m, mascota)));

        model.addAttribute("mascotas", votantes);
        model.addAttribute("tipoVoto", "Recibidos");
        model.addAttribute("votos", votos);
        return "votos";
    }


    @GetMapping("/votos-dados")
    public String votosDados(@RequestParam Long id, ModelMap model) throws ErrorServicio {
        Mascota mascota = mascotaServicio.buscarMascota(id);

        List<Mascota> votados = mascotaServicio.votoDado(mascota);

        // Marcamos si hubo voto recíproco
        votados.forEach(m -> m.setVotoReciproco(
                mascotaServicio.huboVotoReciproco(mascota, m)
        ));

        model.addAttribute("mascotas", votados);
        model.addAttribute("tipoVoto", "Dados");
        return "votos";
    }


}
