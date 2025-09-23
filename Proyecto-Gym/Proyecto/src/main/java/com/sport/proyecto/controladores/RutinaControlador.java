package com.sport.proyecto.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.time.LocalDate;
import org.springframework.ui.Model;
import java.util.Collection;
import com.sport.proyecto.servicios.RutinaServicio;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.enums.EstadoRutina;
import com.sport.proyecto.enums.EstadoDetalleRutina;

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
        Rutina rutina = new Rutina();
        rutina.getDetalleRutina().add(new DetalleRutina()); // evitar lista vacía
        model.addAttribute("rutina", rutina);
        model.addAttribute("socios", socioServicio.obtenerSociosActivos());
        return "views/rutina/form_rutina";
    }



   /**  @PostMapping("/alta")
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
        
    @PostMapping("/alta")
    public String crearRutina(ModelMap modelo, HttpSession session,
                            @RequestParam LocalDate fechaInicio,
                            @RequestParam String nroSocio,
                            @RequestParam Collection<DetalleRutina> detalle,
                            @RequestParam LocalDate fechaFin) throws ErrorServicio {
        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");

            // ✅ Buscar empleado (profesor) por idUsuario
            Empleado profesor = empleadoServicio.buscarEmpleadoPorIdUsuario(login.getId());

            if (profesor == null) {
                throw new ErrorServicio("El usuario logueado no está asociado a un profesor.");
            }

            Rutina rutina = rutinaServicio.crearRutina(
                    nroSocio,
                    profesor.getId(),   // 👉 este es el id del profesor
                    detalle,
                    fechaInicio,
                    fechaFin
            );

            return "redirect:/rutina/mis_rutinas"; 
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            return "error"; 
        }
    }
        
    @PostMapping("/alta")
    public String crearRutina(@RequestParam Long numeroSocio, // recibimos el ID del socio
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
                            @RequestParam Collection<DetalleRutina> detalle,
                            HttpSession session,
                            ModelMap modelo) {

        try {
            // Usuario logeado
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            Empleado profesor = empleadoServicio.buscarEmpleadoPorIdUsuario(login.getId());

            // Buscar socio por ID
            Optional<Socio> socio = socioServicio.findByNumeroSocio(numeroSocio);
            if (socio.isEmpty()) {
                throw new ErrorServicio("No se encontró el socio con número: " + numeroSocio);
            }
            // Crear rutina
            Rutina rutina = new Rutina();
            rutina.setSocio(socio.get());
            rutina.setProfesor(profesor);
            rutina.setFechaInicio(fechaInicio);
            rutina.setFechaFin(fechaFin);
            for (DetalleRutina det : detalle) {
                rutina.crearDetalleRutina(det.getActividad(), det.getFecha());
            }
            rutina.setEstado(EstadoRutina.EN_PROCESO);

            // Guardar rutina
            rutinaServicio.guardar(rutina);

            return "redirect:/rutina/mis_rutinas";

        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            modelo.addAttribute("socios", socioServicio.obtenerSociosActivos()); // recargar lista para el form
            return "views/rutina/form_rutina";
        }
    } Este es el que funciona
    **/
    @PostMapping("/alta")
    public String crearRutina(
            @RequestParam Long numeroSocio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam List<String> actividades,
            @RequestParam List<LocalDate> fechas,
            HttpSession session,
            ModelMap modelo) throws ErrorServicio {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        Empleado profesor = empleadoServicio.buscarEmpleadoPorIdUsuario(login.getId());

        Rutina rutina = new Rutina();
        rutina.setProfesor(profesor);
        rutina.setSocio(socioServicio.findByNumeroSocio(numeroSocio).orElseThrow(() -> new ErrorServicio("Socio no encontrado")));
        rutina.setFechaInicio(fechaInicio);
        rutina.setFechaFin(fechaFin);
        rutina.setEstado(EstadoRutina.EN_PROCESO);

        for (int i = 0; i < actividades.size(); i++) {
            rutina.crearDetalleRutina(actividades.get(i), fechas.get(i));
        }

        rutinaServicio.guardarRutina(rutina);
        return "redirect:/rutina/mis_rutinas";
    }




    @PostMapping("/guardar")
    public String guardarRutina(@RequestParam Long numeroSocio,  // recibís solo el id
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
                                HttpSession session,
                                ModelMap modelo) {
        try {
            // Usuario logeado
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            Empleado profesor = empleadoServicio.buscarEmpleadoPorIdUsuario(login.getId());

            // Buscar socio por id
            Optional<Socio> socio = socioServicio.findByNumeroSocio(numeroSocio);

            // Crear rutina
            if (socio.isEmpty()) {
                throw new ErrorServicio("No se encontró el socio con número: " + numeroSocio);
            }
            Rutina rutina = new Rutina();
            rutina.setSocio(socio.get());
            rutina.setProfesor(profesor);
            rutina.setFechaInicio(fechaInicio);
            rutina.setFechaFin(fechaFin);

            rutinaServicio.guardarRutina(rutina);

            return "redirect:/rutina/mis_rutinas";

        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            modelo.addAttribute("socios", socioServicio.obtenerSociosActivos());
            return "views/rutina/form_rutina";
        }
    }

   @GetMapping("/detalle/{idRutina}")
    public String verDetallesRutina(@PathVariable String idRutina, ModelMap modelo) {
        Rutina rutina = rutinaServicio.buscarRutina(idRutina);
        if (rutina == null) {
            modelo.put("error", "La rutina no existe");
            return "error";
        }

        modelo.put("rutina", rutina);
        modelo.put("detalles", rutina.getDetalleRutina());
        modelo.put("socio", rutina.getSocio());

        return "views/rutina/detalle_rutina"; // 👈 ojo acá
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
    public String modificarRutina(ModelMap modelo, @RequestParam String id, @RequestParam LocalDate fechaInicio,
     @RequestParam String id_Socio, @RequestParam String id_Profesor
     , @RequestParam Collection<DetalleRutina> detalle, @RequestParam EstadoRutina estado,
     @RequestParam LocalDate fechaFin) throws ErrorServicio {
        try {
            LocalDate fecha = fechaInicio;
            rutinaServicio.modificarRutina(id, id_Profesor, id_Socio, fechaInicio, fechaFin, estado,detalle);
            return "redirect:/rutina/mis_rutinas"; // Redirigir a la lista de rutinas después de modificar
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            return "error"; // Redirigir a una página de error en caso de excepción
        }
    }
   @GetMapping("/mis_rutinas")
    public String mostrarRutinas(ModelMap modelo, HttpSession session) {
        //System.out.println("Entré a mostrarRutinas");
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        List<Rutina> rutinas;

        switch (login.getRol()) {
            case SOCIO:
                Socio socio = socioServicio.buscarSocioPorIdUsuario(login.getId()).orElse(null);
                if (socio != null) {
                    rutinas = rutinaServicio.listarRutinasPorSocio(socio.getNumeroSocio());
                } else {
                    rutinas = List.of(); // Si no se encuentra el socio, devolvemos una lista vacía
                }
                break;

            case EMPLEADO:// si querés que los empleados también vean rutinas
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
