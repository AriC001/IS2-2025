package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Empleado;
import com.sport.proyecto.entidades.Persona;
import com.sport.proyecto.entidades.Socio;
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
public class PersonaServicio implements ServicioBase<Persona>{

    @Autowired
    private SocioRepositorio socioRepositorio;
    @Autowired
    private PersonaRepositorio personaRepositorio;
    @Autowired
    private EmpleadoRepositorio empleadoRepositorio;

    @Override
    @Transactional
    public List<Persona> buscarTodos() throws Exception {
        return List.of();
    }

    @Override
    public Persona buscarPorId(Long id) throws Exception {

        return null;
    }

    @Override
    @Transactional
    public void guardar(Persona entity) throws Exception {

    }

    @Override
    @Transactional
    public Persona actualizar(Persona entity, Long id) throws Exception {
        return null;
    }

    @Override
    @Transactional
    public void eliminarPorId(Long id) throws Exception {

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
    public Persona registro(String nombre, String apellido, String email, String clave1, String clave2,boolean esEmpleado) {
        validar(nombre,apellido,email,clave1,clave2);
        Persona p;
        if(esEmpleado){
            p = Empleado.builder().nombre(nombre).
                    apellido(apellido).email(email).
                    clave(clave1).build();
            //if(p instanceof Empleado e){
            //    e.setTipoEmpleado();
            //    p = e;
            //}
        }else{
            p = Socio.builder().nombre(nombre).
                    apellido(apellido).email(email).
                    clave(clave1).build();
            if(p instanceof Socio s){
                s.setNumeroSocio(socioRepositorio.obtenerUltimoNumeroSocio()+1);
                p = s;
            }
        }
        personaRepositorio.save(p);


        //notificacionServicio.enviarMail("Bienvenido a Tinder Mascota " + nombre, "Tinder de Mascota", p.getMail());
        return p;
    }

    public boolean esAdmin(Long id){
        Optional<Persona> opt = personaRepositorio.findById(id);
        if(opt.isPresent()){
            Persona p = opt.get();
            if(p.getId() == 1L){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

}
