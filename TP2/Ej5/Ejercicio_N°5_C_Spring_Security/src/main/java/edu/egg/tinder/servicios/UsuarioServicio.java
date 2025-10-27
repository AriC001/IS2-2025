package edu.egg.tinder.servicios;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.egg.tinder.entidades.Foto;
import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.repositorios.UsuarioRepositorio;
import jakarta.transaction.Transactional;

@Service
public class UsuarioServicio implements UserDetailsService{
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FotoServicio fotoServicio;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByEmail(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        
        return new User(usuario.getMail(), usuario.getPassword(), new ArrayList<>());
    }

    @Autowired
    private NotificacionServicio notificacionServicio;

    @Transactional
    public Usuario registrar(MultipartFile archivo, String nombre, String apellido, String mail, String password, String password2) throws ErrorServicio {
        validar(nombre, apellido, mail, password, password2);

        Usuario usuario = Usuario.builder().nombre(nombre).
                apellido(apellido).mail(mail).
                password(passwordEncoder.encode(password)).build();
        Foto foto = fotoServicio.guardar(archivo);

        usuario.setFoto(foto);
        usuarioRepositorio.save(usuario);

        notificacionServicio.enviarMail("Bienvenido a Tinder Mascota " + nombre, "Tinder de Mascota", usuario.getMail());
        return usuario;
    }

    @Transactional
    public void modificar(MultipartFile archivo, Long id, String nombre, String apellido, String mail, String password,String password2) throws ErrorServicio {
        validar(nombre, apellido, mail, password, password2);
        Optional<Usuario> opt = usuarioRepositorio.findById(id);
        if(opt.isPresent()){
            Usuario usuario = opt.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setMail(mail);
            usuario.setPassword(passwordEncoder.encode(password));

            if (archivo != null && !archivo.isEmpty()) {
                if (usuario.getFoto() != null) {
                    Foto actualizada = fotoServicio.actualizar(usuario.getFoto().getId(), archivo);
                    usuario.setFoto(actualizada);
                } else {
                    Foto nueva = fotoServicio.guardar(archivo);
                    usuario.setFoto(nueva);
                }
            }

            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado");
        }

    }

    @Transactional
    public void desactivar(Long id) throws ErrorServicio {
        Optional<Usuario> opt = usuarioRepositorio.findById(id);
        if(opt.isPresent()){
            Usuario usuario = opt.get();
            usuario.setBaja(new Date());
            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado");
        }
    }

    @Transactional
    public void activar(Long id) throws ErrorServicio {
        Optional<Usuario> opt = usuarioRepositorio.findById(id);
        if(opt.isPresent()){
            Usuario usuario = opt.get();
            usuario.setBaja(null);
            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado");
        }
    }

    public void validar(String nombre, String apellido, String mail, String password, String password2) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido no puede ser nulo");
        }
        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El mail no puede ser nulo");
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

    // public Usuario login(String email, String clave) throws ErrorServicio {

    //     try {

    //         if (email == null || email.trim().isEmpty()) {
    //             throw new ErrorServicio("Debe indicar el usuario");
    //         }

    //         if (clave == null || clave.trim().isEmpty()) {
    //             throw new ErrorServicio("Debe indicar la clave");
    //         }

    //         Usuario usuario = null;
    //         try {
    //             usuario = usuarioRepositorio.findByMailAndPassword(email, clave);
    //         } catch (NoResultException ex) {
    //             throw new ErrorServicio("No existe usuario para el correo y clave indicado");
    //         }

    //         return usuario;

    //     } catch (ErrorServicio e) {
    //         throw e;
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         throw new ErrorServicio("Error de Sistemas");
    //     }
    // }

    public Usuario findById(Long idUsuario) throws ErrorServicio {
        Optional<Usuario> opt = usuarioRepositorio.findById(idUsuario);
        if(opt.isPresent()){
            Usuario us = opt.get();
            return us;
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado");
        }
    }

    public Usuario buscarUsuarioPorEmail(String email) throws ErrorServicio {
        Usuario usuario = usuarioRepositorio.findByEmail(email);
        if (usuario == null) {
            throw new ErrorServicio("No se encontró el usuario con el email indicado");
        }
        return usuario;
    }

}
