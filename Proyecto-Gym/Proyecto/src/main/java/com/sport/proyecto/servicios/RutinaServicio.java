package com.sport.proyecto.servicios;

import org.hibernate.annotations.MapKeyCompositeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.sport.proyecto.entidades.DetalleRutina;
import com.sport.proyecto.entidades.Empleado;
import com.sport.proyecto.entidades.Persona;
import com.sport.proyecto.entidades.Rutina;
import com.sport.proyecto.entidades.Socio;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.DetalleRutinaRepositorio;
import com.sport.proyecto.repositorios.EmpleadoRepositorio;
import com.sport.proyecto.repositorios.PersonaRepositorio;
import com.sport.proyecto.repositorios.RutinaRepositorio;
import com.sport.proyecto.enums.EstadoRutina;
import com.sport.proyecto.enums.EstadoDetalleRutina;
import com.sport.proyecto.repositorios.SocioRepositorio;
import java.util.Date;
import java.util.Optional;
import java.util.Collection;
import java.util.List;
import java.time.LocalDate;
@Service
public class RutinaServicio {
    @Autowired
    private SocioRepositorio socioRepositorio;
    @Autowired
    private PersonaRepositorio personaRepositorio;
    @Autowired
    private EmpleadoRepositorio empleadoRepositorio;
    @Autowired
    private RutinaRepositorio rutinaRepositorio;
    @Autowired
    private DetalleRutinaRepositorio detalleRutinaRepositorio;

    @Transactional
    public Rutina crearRutina(String id_socio, String id_empleado, Collection<DetalleRutina> detalle, LocalDate fechaInicio, LocalDate fechaFin) throws ErrorServicio {
        // Lógica para crear una nueva rutina
        validar(id_empleado, id_socio, fechaInicio, fechaFin, detalle);
        Rutina rutina = new Rutina();
        rutina.setFechaInicio(fechaInicio);
        rutina.setFechaFin(null);
        rutina.setDetalleRutina(detalle);
        rutina.setEstado(EstadoRutina.EN_PROCESO);
        if(socioRepositorio.findByNumeroSocio(Long.parseLong(id_socio)).isPresent()){
            rutina.setSocio(socioRepositorio.findByNumeroSocio(Long.parseLong(id_socio)).get());
        }
        else{
            throw new ErrorServicio("No existe un socio con ese ID.");
        }
        rutina.setProfesor(empleadoRepositorio.findProfesor(id_empleado));
        return rutinaRepositorio.save(rutina);
    }
    @Transactional
    public Rutina buscarRutina(String id) throws ErrorServicio {
        // Lógica para buscar una rutina por su ID
        Rutina rutina = rutinaRepositorio.findById(id).orElse(null);
        if (rutina == null || rutina.isEliminado()) {
            throw new ErrorServicio("Rutina no encontrada o eliminada");
        }
        return rutina;
    }
    @Transactional
    public void finalizarRutina(String id) throws ErrorServicio {
        // Lógica para actualizar una rutina existente
        Rutina rutinaExistente = buscarRutina(id);
        if (rutinaExistente != null) {
            rutinaExistente.setEstado(EstadoRutina.FINALIZADA);
            rutinaExistente.setFechaFin(LocalDate.now());
            rutinaRepositorio.save(rutinaExistente);
        } else {
            throw new ErrorServicio("Rutina no encontrada");
        }
    }
    @Transactional
    public void modificarRutina(String id, String idProfesor, String idSocio, 
    LocalDate fechaInicio, LocalDate fechaFin, EstadoRutina estado, Collection<DetalleRutina> detalle) throws ErrorServicio {
        
        // Lógica para modificar una rutina existente
        
        Rutina rutinaExistente = buscarRutina(id);
        if (rutinaExistente != null) {
            validar(idProfesor, idSocio, fechaInicio, fechaFin, detalle);
            rutinaExistente.setFechaFin(fechaFin);
            rutinaExistente.setProfesor(empleadoRepositorio.findProfesor(idProfesor));
            if(socioRepositorio.findByNumeroSocio(Long.parseLong(idSocio)).isPresent()){
                rutinaExistente.setSocio(socioRepositorio.findByNumeroSocio(Long.parseLong(idSocio)).get());
            }
            rutinaExistente.setFechaInicio(fechaInicio);
            rutinaExistente.setDetalleRutina(detalle);
            rutinaExistente.setEstado(estado);
            rutinaRepositorio.save(rutinaExistente);
        } else {
            throw new ErrorServicio("Rutina no encontrada");
        }
    }

    @Transactional
    public void eliminarRutina(String id) throws ErrorServicio {
        Optional<Rutina> opt = rutinaRepositorio.findById(id);
        if(opt.isPresent()){

            Rutina rutina = opt.get();
            // Eliminación lógica de la rutina
            rutina.setEliminado(true);
            rutinaRepositorio.save(rutina);
        }
        else{
            throw new ErrorServicio("No se encontró la rutina.");
        }
    }

    @Transactional
    public Rutina guardarRutina(Rutina rutina) throws ErrorServicio {

        if (rutina.getSocio() == null)
            throw new ErrorServicio("Debe seleccionar un socio.");
        if (rutina.getProfesor() == null)
            throw new ErrorServicio("Debe estar asociado a un profesor.");
        if (rutina.getFechaInicio() == null || rutina.getFechaFin() == null)
            throw new ErrorServicio("Debe indicar fecha de inicio y fin.");

        Socio socio = socioRepositorio.findByNumeroSocio(rutina.getSocio().getNumeroSocio())
                .orElseThrow(() -> new ErrorServicio("Socio no encontrado"));
        Empleado profesor = empleadoRepositorio.findById(rutina.getProfesor().getId())
                .orElseThrow(() -> new ErrorServicio("Profesor no encontrado"));

        rutina.setSocio(socio);
        rutina.setProfesor(profesor);

        // Ya que los detalles están asociados con rutina, se guardan en cascada
        return rutinaRepositorio.save(rutina);
    }
    @Transactional
    public String completarActividad(@PathVariable String idDetalle,
                                     @RequestParam(required = false) String rutinaId) {
         DetalleRutina detalle = detalleRutinaRepositorio.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado con id: " + idDetalle));

        detalle.setEstado(EstadoDetalleRutina.REALIZADA); // O EstadoActividad.COMPLETADA si usás enum
        detalleRutinaRepositorio.save(detalle);

        // redirigir de nuevo a la vista de rutina
        return "redirect:/rutina/ver/" + rutinaId; 
        // Ajustá según el path de tu página de detalles de la rutina
    }
    /** 
    @Transactional
    public Rutina guardar(Rutina rutina) throws ErrorServicio {

        if (rutina.getSocio() == null || rutina.getSocio().getId() == null) {
            throw new ErrorServicio("Debe seleccionar un socio.");
        }

        if (rutina.getProfesor() == null || rutina.getProfesor().getId() == null) {
            throw new ErrorServicio("Debe estar asociado a un profesor.");
        }

        if (rutina.getFechaInicio() == null || rutina.getFechaFin() == null) {
            throw new ErrorServicio("Debe indicar fecha de inicio y fin.");
        }

        if (rutina.getFechaFin().isBefore(rutina.getFechaInicio())) {
            throw new ErrorServicio("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        // 🚨 Importante: asegurarse que los objetos sean entidades manejadas
        Socio socio = socioRepositorio.findByNumeroSocio(rutina.getSocio().getNumeroSocio())
                        .orElseThrow(() -> new ErrorServicio("Socio no encontrado"));
        Empleado profesor = empleadoRepositorio.findById(rutina.getProfesor().getId())
                        .orElseThrow(() -> new ErrorServicio("Profesor no encontrado"));

        rutina.setSocio(socio);
        rutina.setProfesor(profesor);

        return rutinaRepositorio.save(rutina);
    }
        **/
    @Transactional
    public DetalleRutina crearDetalleRutina(LocalDate fecha, String actividad) throws ErrorServicio {
        DetalleRutina detalle = new DetalleRutina();
        detalle.setFecha(fecha);
        detalle.setActividad(actividad);
        detalle.setEstado(EstadoDetalleRutina.SIN_REALIZAR);
        return detalle;
    }
    @Transactional
    public List<Rutina> listarRutinasPorSocio(Long nro_socio) throws ErrorServicio {
        // Lógica para listar todas las rutinas de un socio específico
        Socio socio = socioRepositorio.findByNumeroSocio(nro_socio).orElse(null);
        if (socio == null) {
            throw new ErrorServicio("Socio no encontrado");
        }
        return rutinaRepositorio.findBySocio(socio);
    }
    @Transactional
    public List<Rutina> listarTodasLasRutinas() {
        // Lógica para listar todas las rutinas
        return rutinaRepositorio.findAll();
    }
    public List<Rutina> listarRutinasPorProfesor(String idProfesor) throws ErrorServicio {
        Empleado profesor = empleadoRepositorio.findProfesor(idProfesor);
        if (profesor == null) {
            throw new ErrorServicio("Profesor no encontrado");
        }
        return rutinaRepositorio.findAll().stream()
                .filter(rutina -> rutina.getProfesor() != null && rutina.getProfesor().getId().equals(idProfesor))
                .toList();
    }
    @Transactional
    public void validar(String idProfesor, String idSocio, LocalDate fechaInicio, LocalDate fechaFin, Collection<DetalleRutina> detalle) throws ErrorServicio {
        if (idProfesor == null || idProfesor.isEmpty()) {
            throw new ErrorServicio("El ID del profesor no puede ser nulo o vacío.");
        }
        if(empleadoRepositorio.findById(idProfesor)==null){
            throw new ErrorServicio("No existe un empleado con ese ID.");
        }
        if (idSocio == null || idSocio.isEmpty()) {
            throw new ErrorServicio("El ID del socio no puede ser nulo o vacío.");
        }
        if (socioRepositorio.findByNumeroSocio(Long.parseLong(idSocio))== null) {
            throw new ErrorServicio("No existe un socio con ese ID.");
        }
        if (fechaInicio == null) {
            throw new ErrorServicio("La fecha de inicio no puede ser nula.");
        }
        if (fechaFin == null) {
            throw new ErrorServicio("La fecha de fin no puede ser nula.");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new ErrorServicio("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
        if (fechaInicio.isAfter(LocalDate.now())) {
            throw new ErrorServicio("La fecha de inicio no puede ser futura.");
        }
        if (detalle == null || detalle.isEmpty()) {
            throw new ErrorServicio("El detalle de la rutina no puede ser nulo o vacío.");
        }
    }
}
