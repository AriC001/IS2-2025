package edu.egg.tinder.servicios;

import edu.egg.tinder.entidades.Foto;
import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.repositorios.UsuarioRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    public void registrar(MultipartFile archivo, String nombre, String apellido, String mail, String password) throws ErrorServicio {
        validar(nombre, apellido, mail, password);

        Usuario usuario = Usuario.builder().nombre(nombre).
                apellido(apellido).mail(mail).
                password(password).build();
        Foto foto = fotoServicio.guardar(archivo);

        usuario.setFoto(foto);
        usuarioRepositorio.save(usuario);

        notificacionServicio.enviarMail("Bienvenido a Tinder Mascota " + nombre, "Tinder de Mascota", usuario.getMail());
    }

    @Transactional
    public void modificar(MultipartFile archivo, Long id, String nombre, String apellido, String mail, String password) throws ErrorServicio {
        validar(nombre, apellido, mail, password);
        Optional<Usuario> opt = usuarioRepositorio.findById(id);
        if(opt.isPresent()){
            Usuario usuario = opt.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setMail(mail);
            usuario.setPassword(password);

            Long idFoto = null;
            if (usuario.getFoto() != null) {
                idFoto = usuario.getFoto().getId();
            }

            Foto foto = fotoServicio.actualizar(idFoto, archivo);
            usuario.setFoto(foto);

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

    private void validar(String nombre, String apellido, String mail, String password) throws ErrorServicio {
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
