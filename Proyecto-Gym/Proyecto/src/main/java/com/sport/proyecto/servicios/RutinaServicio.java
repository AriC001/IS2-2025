package com.sport.proyecto.servicios;

import org.hibernate.annotations.MapKeyCompositeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sport.proyecto.entidades.DetalleRutina;
import com.sport.proyecto.entidades.Empleado;
import com.sport.proyecto.entidades.Persona;
import com.sport.proyecto.entidades.Rutina;
import com.sport.proyecto.entidades.Socio;
import com.sport.proyecto.errores.ErrorServicio;
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
@Service
public class RutinaServicio {
    @Autowired
    private SocioRepositorio socioRepositorio;
    @Autowired
    private PersonaRepositorio personaRepositorio;
    @Autowired
    private EmpleadoRepositorio empleadoRepositorio;
    @Autowired
    private RutinaRepositorio rutinaRepository;

    @Transactional
    public Rutina crearRutina(String id_socio, String id_empleado, Collection<DetalleRutina> detalle, Date fechaInicio, Date fechaFin) throws ErrorServicio {
        // Lógica para crear una nueva rutina
        validar(id_empleado, id_socio, fechaInicio, fechaFin, detalle);
        Rutina rutina = new Rutina();
        rutina.setFechaInicio(fechaInicio);
        rutina.setFechaFin(null);
        detalle.add(crearDetalleRutina(fechaInicio,"Inicio de rutina"));
        rutina.setDetalleRutina(detalle);
        rutina.setEstado(EstadoRutina.EN_PROCESO);
        rutina.setSocio(socioRepositorio.findByNumeroSocio(Long.parseLong(id_socio)));
        rutina.setProfesor(empleadoRepositorio.findProfesor(id_empleado));
        return rutinaRepository.save(rutina);
    }
    @Transactional
    public Rutina buscarRutina(String id) throws ErrorServicio {
        // Lógica para buscar una rutina por su ID
        Rutina rutina = rutinaRepository.findById(id).orElse(null);
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
            rutinaExistente.setFechaFin(new Date());
            rutinaRepository.save(rutinaExistente);
        } else {
            throw new ErrorServicio("Rutina no encontrada");
        }
    }
    @Transactional
    public void modificarRutina(String id, String idProfesor, String idSocio, 
    Date fechaInicio, Date fechaFin, EstadoRutina estado, Collection<DetalleRutina> detalle) throws ErrorServicio {
        
        // Lógica para modificar una rutina existente
        
        Rutina rutinaExistente = buscarRutina(id);
        if (rutinaExistente != null) {
            validar(idProfesor, idSocio, fechaInicio, fechaFin, detalle);
            rutinaExistente.setFechaFin(fechaFin);
            rutinaExistente.setProfesor(empleadoRepositorio.findProfesor(idProfesor));
            rutinaExistente.setSocio(socioRepositorio.findByNumeroSocio(Long.parseLong(idSocio)));
            rutinaExistente.setFechaInicio(fechaInicio);
            rutinaExistente.setDetalleRutina(detalle);
            rutinaExistente.setEstado(estado);
            rutinaRepository.save(rutinaExistente);
        } else {
            throw new ErrorServicio("Rutina no encontrada");
        }
    }

    @Transactional
    public void eliminarRutina(String id) throws ErrorServicio {
        Optional<Rutina> opt = rutinaRepository.findById(id);
        if(opt.isPresent()){

            Rutina rutina = opt.get();
            // Eliminación lógica de la rutina
            rutina.setEliminado(true);
            rutinaRepository.save(rutina);
        }
        else{
            throw new ErrorServicio("No se encontró la rutina.");
        }
    }
    @Transactional
    public DetalleRutina crearDetalleRutina(Date fecha, String actividad) throws ErrorServicio {
        DetalleRutina detalle = new DetalleRutina();
        detalle.setFecha(fecha);
        detalle.setActividad(actividad);
        detalle.setEstado(EstadoDetalleRutina.SIN_REALIZAR);
        return detalle;
    }
    @Transactional
    public List<Rutina> listarRutinasPorSocio(String nro_socio) throws ErrorServicio {
        // Lógica para listar todas las rutinas de un socio específico
        Socio socio = socioRepositorio.findByNumeroSocio(Long.parseLong(nro_socio));
        if (socio == null) {
            throw new ErrorServicio("Socio no encontrado");
        }
        return rutinaRepository.findBySocio(socio);
    }
    @Transactional
    public List<Rutina> listarTodasLasRutinas() {
        // Lógica para listar todas las rutinas
        return rutinaRepository.findAll();
    }
    public List<Rutina> listarRutinasPorProfesor(String idProfesor) throws ErrorServicio {
        Empleado profesor = empleadoRepositorio.findProfesor(idProfesor);
        if (profesor == null) {
            throw new ErrorServicio("Profesor no encontrado");
        }
        return rutinaRepository.findAll().stream()
                .filter(rutina -> rutina.getProfesor() != null && rutina.getProfesor().getId().equals(idProfesor))
                .toList();
    }
    @Transactional
    public void validar(String idProfesor, String idSocio, Date fechaInicio, Date fechaFin, Collection<DetalleRutina> detalle) throws ErrorServicio {
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
        if (fechaFin.before(fechaInicio)) {
            throw new ErrorServicio("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
        if (fechaInicio.after(new Date())) {
            throw new ErrorServicio("La fecha de inicio no puede ser futura.");
        }
        if (detalle == null || detalle.isEmpty()) {
            throw new ErrorServicio("El detalle de la rutina no puede ser nulo o vacío.");
        }
    }
}
