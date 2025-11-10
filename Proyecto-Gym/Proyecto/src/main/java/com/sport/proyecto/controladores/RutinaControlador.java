package com.sport.proyecto.controladores;

import com.sport.proyecto.servicios.*;
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

import com.sport.proyecto.entidades.Empleado;

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
    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/crear")
    public String mostrarFormularioRutina(Model model) {
        Rutina rutina = new Rutina();
        rutina.getDetalleRutina().add(new DetalleRutina()); // evitar lista vacía
        model.addAttribute("rutina", rutina);
        model.addAttribute("socios", socioServicio.obtenerSociosActivos());
        return "views/rutina/form_rutina";
    }
    


    @PostMapping("/alta")
    public String crearRutina(
            @RequestParam Long numeroSocio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam List<String> actividades,
            @RequestParam List<LocalDate> fechas,
            @org.springframework.web.bind.annotation.ModelAttribute("usuariosession") Usuario login,
            ModelMap modelo) throws ErrorServicio {

        System.out.println(numeroSocio + " " + fechaInicio + " " + actividades + " " + login.getId());

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
    public String guardarRutina(@RequestParam Long numeroSocio,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
                                @org.springframework.web.bind.annotation.ModelAttribute("usuariosession") Usuario login,
                                ModelMap modelo) {
        try {
            // Usuario logeado
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
    //Métodos detalle rutina
    @GetMapping("/detalle/{idRutina}")
    public String verDetallesRutina(@PathVariable String idRutina, ModelMap modelo) {
        Rutina rutina = rutinaServicio.buscarRutina(idRutina);
        if (rutina == null) {
            modelo.put("error", "La rutina no existe");
            return "error";
        }

        Collection<DetalleRutina> detalles = rutina.getDetalleRutina();

        // 🔹 Calcular el porcentaje completado
        int total = detalles.size();
        long completadas = rutina.getDetalleRutina()
        .stream()
        .filter(d -> d.getEstado().equals(EstadoDetalleRutina.REALIZADA))
        .count();
        int porcentajeCompletado = (int) (total > 0 ? (completadas * 100 / total) : 0);

        modelo.put("rutina", rutina);
        modelo.put("detalles", detalles);
        modelo.put("socio", rutina.getSocio());
        modelo.put("porcentajeCompletado", porcentajeCompletado);
        modelo.put("idRutina", idRutina); //para redirigir tras completar

        return "views/rutina/detalle_rutina";
    }
    @PostMapping("/detalle/{id}/completar")
    public String completarActividad(@PathVariable String id,
                                     @RequestParam(required = false) String rutinaId) {
        rutinaServicio.completarActividad(id, rutinaId);

        // redirigir de nuevo a la vista de rutina
        return "redirect:/rutina/detalle/" + rutinaId; 
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
    public String mostrarRutinas(ModelMap modelo, @org.springframework.web.bind.annotation.ModelAttribute("usuariosession") Usuario login) {
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

    // Endpoint para que el ADMIN cree rutinas seleccionando socio y profesor (desde ABM)
    @PostMapping("/admin/alta")
    public String adminCrearRutina(
            @RequestParam Long numeroSocio,
            @RequestParam String idProfesor,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) List<String> actividades,
            @RequestParam(required = false) List<String> fechas,
            ModelMap modelo) {
        try {
            java.util.Collection<DetalleRutina> detalles = new java.util.ArrayList<>();
            if (actividades != null && fechas != null) {
                for (int i = 0; i < Math.min(actividades.size(), fechas.size()); i++) {
                    DetalleRutina d = new DetalleRutina();
                    d.setActividad(actividades.get(i));
                    try {
                        java.time.LocalDate f = fechas.get(i) != null && !fechas.get(i).isEmpty() ? java.time.LocalDate.parse(fechas.get(i)) : null;
                        d.setFecha(f);
                    } catch (Exception ex) {
                        d.setFecha(null);
                    }
                    d.setEstado(EstadoDetalleRutina.SIN_REALIZAR);
                    d.setEliminado(false);
                    detalles.add(d);
                }
            }
            rutinaServicio.crearRutina(numeroSocio.toString(), idProfesor, detalles, fechaInicio, fechaFin);
            return "redirect:/portal/admin/rutinas";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "error";
        }
    }


}
