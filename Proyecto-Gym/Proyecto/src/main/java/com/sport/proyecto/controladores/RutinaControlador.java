package com.sport.proyecto.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.sport.proyecto.enums.*;
import java.util.ArrayList;
import java.util.Collection;
import com.sport.proyecto.entidades.DetalleRutina; 
import com.sport.proyecto.enums.EstadoRutina;
import com.sport.proyecto.enums.EstadoDetalleRutina;
import com.sport.proyecto.servicios.RutinaServicio;
import com.sport.proyecto.errores.ErrorServicio;
import org.springframework.ui.ModelMap; 
import org.springframework.web.bind.annotation.RequestParam;    
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;  
import java.util.List;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.sport.proyecto.entidades.Persona;
import com.sport.proyecto.entidades.Rutina ;
import com.sport.proyecto.entidades.Socio;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.enums.Rol;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import com.sport.proyecto.servicios.SocioServicio;
import com.sport.proyecto.entidades.Empleado;
import com.sport.proyecto.servicios.EmpleadoServicio;
import java.util.List;
import org.springframework.ui.Model;
@Controller
@RequestMapping("/rutina")
public class RutinaControlador {
    @Autowired
    private RutinaServicio rutinaServicio;
    @Autowired
    private SocioServicio socioServicio;
    @Autowired
    private EmpleadoServicio empleadoServicio;

    @GetMapping("/crear")
    public String mostrarFormularioRutina(Model model) {
        // Cargar datos necesarios para el formulario (socios, profesores, etc.)
        List<Socio> socios = socioServicio.obtenerSociosActivos();
        List<Empleado> profesores = empleadoServicio.obtenerProfesores();

        model.addAttribute("socios", socios);
        model.addAttribute("profesores", profesores);

        // Si tenés detalles de rutina, también agregarlos
        model.addAttribute("detalle", new ArrayList<DetalleRutina>());
        model.addAttribute("rutina", new Rutina()); // <--- Esto es clave

        return "views/rutina/form_rutina"; // Nombre del HTML del formulario
    }


    @PostMapping("/alta")
    public String crearRutina(ModelMap modelo, HttpSession session, @RequestParam Date fechaInicio,
                               @RequestParam String id_socio, @RequestParam String id_profesor,
                               @RequestParam Collection<DetalleRutina> detalle, @RequestParam Date fechaFin) throws ErrorServicio {
        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            java.util.Date fecha = fechaInicio;
            Rutina rutina=rutinaServicio.crearRutina(id_socio, id_profesor, detalle, fechaInicio, fechaFin);
            return "redirect:/rutina/mis_rutinas"; // Redirigir a la lista de rutinas después de crear
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            return "error"; // Redirigir a una página de error en caso de excepción
        }
    }
   @GetMapping("/detalle/{idRutina}")
    public String verDetallesRutina(@PathVariable String idRutina, ModelMap modelo) {
        Rutina rutina = rutinaServicio.buscarRutina(idRutina);
        Collection<DetalleRutina> detalles = rutina.getDetalleRutina();

        Persona socio = rutina.getSocio();

        modelo.put("rutina", rutina);
        modelo.put("detalles", detalles);
        modelo.put("socio", socio); // puede ser null, hacé comprobación en la vista

        return "detalle_rutina";
    }
    @PostMapping("/finalizar")
    public String finalizarRutina(ModelMap modelo, @RequestParam String id) throws ErrorServicio {
        try {
            rutinaServicio.finalizarRutina(id);
            return "redirect:/rutina/mis_rutinas"; // Redirigir a la lista de rutinas después de finalizar
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            return "error"; // Redirigir a una página de error en caso de excepción
        }
    }   
    @PostMapping("/eliminar")
    public String eliminarRutina(ModelMap modelo, @RequestParam String id) throws ErrorServicio
    {
        try {
            rutinaServicio.eliminarRutina(id);
            return "redirect:/rutina/mis_rutinas"; // Redirigir a la lista de rutinas después de eliminar
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            return "error"; // Redirigir a una página de error en caso de excepción
        }
    }
    @PostMapping("/modificar")
    public String modificarRutina(ModelMap modelo, @RequestParam String id, @RequestParam Date fechaInicio,
     @RequestParam String id_Socio, @RequestParam String id_Profesor
     , @RequestParam Collection<DetalleRutina> detalle, @RequestParam EstadoRutina estado,
     @RequestParam Date fechaFin) throws ErrorServicio {
        try {
            java.util.Date fecha = fechaInicio;
            rutinaServicio.modificarRutina(id, id_Profesor, id_Socio, fechaInicio, fechaFin, estado,detalle);
            return "redirect:/rutina/mis_rutinas"; // Redirigir a la lista de rutinas después de modificar
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            return "error"; // Redirigir a una página de error en caso de excepción
        }
    }
   @GetMapping("/mis_rutinas")
    public String mostrarRutinas(ModelMap modelo, HttpSession session) {
        System.out.println("Entré a mostrarRutinas");
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        List<Rutina> rutinas;

        switch (login.getRol()) {
            case SOCIO:
                Socio socio = socioServicio.buscarSocioPorIdUsuario(login.getId().toString());
                rutinas = rutinaServicio.listarRutinasPorSocio(socio.getNumeroSocio().toString());
                break;

            case EMPLEADO_ADMINISTRATIVO:// si querés que los empleados también vean rutinas
            case EMPLEADO_PROFESOR:
                Empleado empleado = empleadoServicio.buscarEmpleadoPorIdUsuario(login.getId().toString());
                rutinas = rutinaServicio.listarRutinasPorProfesor(empleado.getId());
                break;

            case ADMIN:
                rutinas = rutinaServicio.listarTodasLasRutinas(); // todas las rutinas
                break;

            default:
                rutinas = List.of();
        }

        modelo.put("rutinas", rutinas);

        return "views/rutina/mis_rutinas";
    }


}
