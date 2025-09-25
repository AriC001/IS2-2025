package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Empleado;
import com.sport.proyecto.entidades.Persona;
import com.sport.proyecto.entidades.Socio;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.enums.Rol;
import com.sport.proyecto.enums.tipoEmpleado;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.EmpleadoRepositorio;
import com.sport.proyecto.repositorios.PersonaRepositorio;
import com.sport.proyecto.repositorios.SocioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PersonaServicio {

    @Autowired
    private SocioRepositorio socioRepositorio;
    @Autowired
    private PersonaRepositorio personaRepositorio;
    @Autowired
    private EmpleadoRepositorio empleadoRepositorio;
    @Autowired
    private CuotaMensualServicio cuotaMensualServicio;

    @Transactional
    public List<Persona> buscarTodos() throws Exception {
        return List.of();
    }

    @Transactional
    public Persona buscarPersona(String id) throws Exception {

        return null;
    }

    @Transactional
    public void guardar(Persona entity) throws Exception {

    }

    @Transactional
    public Persona actualizar(Persona entity, String id) throws Exception {
        return null;
    }

    @Transactional
    public void eliminarPorId(String id) throws Exception {
        Optional<Persona> opt = personaRepositorio.findById(id);
        if(opt.isPresent()){

            Persona persona = opt.get();
            // Eliminación lógica de la persona
            persona.setEliminado(true);
            // Eliminación lógica del usuario asociado
            if (persona.getUsuario() != null) {
                persona.getUsuario().setEliminado(true);
            }
            personaRepositorio.save(persona);
        }


    }

    @Transactional
    public Persona login(String email, String clave) throws ErrorServicio {
        Persona p = null;
        if(email == null || clave == null){
            throw new ErrorServicio("El email o clave no puede ser nulo");
        }
        Optional<Persona> opt = personaRepositorio.findByEmailyClave(email,clave);
        if(opt.isPresent()){
            p = opt.get();
            return p;
        }else{
            throw new ErrorServicio("Email o Contraseña incorrecto");
        }
    }

    public void validar(String nombre, String apellido, String email, String password, String password2) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido no puede ser nulo");
        }
        if (email == null || email.isEmpty()) {
            throw new ErrorServicio("El email no puede ser nulo");
        }
        if (password == null || password.length() < 6) {
            throw new ErrorServicio("La contraseña no puede ser nula y debe tener al menos 6 caracteres");
        }
        if (password2 == null || password2.length() < 6) {
            throw new ErrorServicio("La contraseña no puede ser nula y debe tener al menos 6 caracteres");
        }
        if (!password.equals(password2)){
            throw new ErrorServicio("Las contraseñas deben coincidir");
        }
    }

    @Transactional
    public Persona registro(String nombre, String apellido, String email, String clave1, String clave2, boolean esEmpleado, Optional<tipoEmpleado> tipoEmpleado) {
        validar(nombre,apellido,email,clave1,clave2);
        Persona p;
        if(esEmpleado){
            Usuario u = Usuario.builder().nombreUsuario(email).clave(UtilServicio.encriptarClave(clave1)).rol(Rol.EMPLEADO).build();
            p = Empleado.builder().nombre(nombre).
                    apellido(apellido).email(email).tipoEmpleado(tipoEmpleado.get()).usuario(u).build();
            //if(p instanceof Empleado e){
            //    e.setTipoEmpleado();
            //    p = e;
            //}
        }else{
            Usuario u = Usuario.builder().nombreUsuario(email).clave(UtilServicio.encriptarClave(clave1)).rol(Rol.SOCIO).build();
            p = Socio.builder().nombre(nombre).
                    apellido(apellido).email(email).usuario(u).build();
            if(p instanceof Socio s){
                s.setNumeroSocio(socioRepositorio.obtenerUltimoNumeroSocio()+1);
                p = s;
            }
        }
        personaRepositorio.save(p);
        if(p instanceof Socio s){
            cuotaMensualServicio.crearCuota(s.getUsuario().getId());
        }


        //notificacionServicio.enviarMail("Bienvenido a Tinder Mascota " + nombre, "Tinder de Mascota", p.getMail());
        return p;
    }



}
