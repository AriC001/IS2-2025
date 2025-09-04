package edu.egg.tinder.servicios;

import edu.egg.tinder.entidades.Foto;
import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.repositorios.UsuarioRepositorio;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;

@Service
public class UsuarioServicio {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private FotoServicio fotoServicio;

    @Autowired
    private NotificacionServicio notificacionServicio;

    @Transactional
    public Usuario registrar(MultipartFile archivo, String nombre, String apellido, String mail, String password, String password2) throws ErrorServicio {
        validar(nombre, apellido, mail, password, password2);

        Usuario usuario = Usuario.builder().nombre(nombre).
                apellido(apellido).mail(mail).
                password(password).build();
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
            usuario.setPassword(password);

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

    private void validar(String nombre, String apellido, String mail, String password, String password2) throws ErrorServicio {
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

    public Usuario login(String email, String clave) throws ErrorServicio {

        try {

            if (email == null || email.trim().isEmpty()) {
                throw new ErrorServicio("Debe indicar el usuario");
            }

            if (clave == null || clave.trim().isEmpty()) {
                throw new ErrorServicio("Debe indicar la clave");
            }

            Usuario usuario = null;
            try {
                usuario = usuarioRepositorio.findByMailAndPassword(email, clave);
            } catch (NoResultException ex) {
                throw new ErrorServicio("No existe usuario para el correo y clave indicado");
            }

            return usuario;

        } catch (ErrorServicio e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServicio("Error de Sistemas");
        }
    }

    public Usuario findById(Long idUsuario) throws ErrorServicio {
        Optional<Usuario> opt = usuarioRepositorio.findById(idUsuario);
        if(opt.isPresent()){
            Usuario us = opt.get();
            return us;
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado");
        }
    }

        //    @Override
    //    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
    //        Usuario usuario = usuarioRepositorio.findByMail(mail);
    //        if (usuario != null) {
    //            List<GrantedAuthority> permisos = new ArrayList<>();
    //
    //            GrantedAuthority p1 = new SimpleGrantedAuthority("MODULO_FOTOS");
    //            permisos.add(p1);
    //            GrantedAuthority p2 = new SimpleGrantedAuthority("MODULO_MASCOTAS");
    //            permisos.add(p2);
    //            GrantedAuthority p3 = new SimpleGrantedAuthority("MODULO_VOTOS");
    //            permisos.add(p3);
    //
    //            User user = new User(usuario.getMail(), usuario.getPassword(), permisos);
    //            return user;
    //        } else {
    //            return null;
    //        }
    //    }
}
