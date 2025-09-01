package edu.egg.tinder.servicios;

import edu.egg.tinder.entidades.Foto;
import edu.egg.tinder.entidades.Mascota;
import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.enumeracion.Sexo;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.repositorios.MascotaRepositorio;
import edu.egg.tinder.repositorios.UsuarioRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;

@Service
public class MascotaServicio {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private MascotaRepositorio mascotaRepositorio;

    @Autowired
    private FotoServicio fotoServicio;

    @Transactional
    public void agregar(MultipartFile archivo, Long idUsuario, String nombreMascota, Sexo sexo) throws ErrorServicio {
        validar(nombreMascota, sexo);

        Optional<Usuario> usuario = usuarioRepositorio.findById(idUsuario);
        if (usuario.isPresent()) {
            Mascota mascota = Mascota.builder().nombre(nombreMascota).
                    sexo(sexo).alta(new Date()).usuario(usuario.get()).
                    build();

            Foto foto = fotoServicio.guardar(archivo);
            mascota.setFoto(foto);

            mascotaRepositorio.save(mascota);
        } else {
            // Manejar el caso en que el usuario no existe
            throw new ErrorServicio("Usuario no encontrado");
        }
    }

    @Transactional
    public void modificar(MultipartFile archivo, Long idUsuario, Long idMascota, String nombreMascota, Sexo sexo) throws ErrorServicio {
        validar(nombreMascota, sexo);

        Optional<Mascota> opt = mascotaRepositorio.findById(idMascota);
        if (opt.isPresent()) {
            Mascota mascota = opt.get();
            if (!mascota.getUsuario().getId().equals(idUsuario)) {
                throw new ErrorServicio("No tiene permiso para modificar esta mascota");
            }
            mascota.setNombre(nombreMascota);
            mascota.setSexo(sexo);

            Long idFoto = null;
            if (mascota.getFoto() != null) {
                idFoto = mascota.getFoto().getId();
            }
            Foto foto = fotoServicio.actualizar(idFoto, archivo);
            mascota.setFoto(foto);

            mascotaRepositorio.save(mascota);
        } else {
            throw new ErrorServicio("No se encontró la mascota solicitada");
        }
    }

    @Transactional
    public void eliminar(Long idUsuario,Long idMascota) throws ErrorServicio {
        Optional<Mascota> opt = mascotaRepositorio.findById(idMascota);
        if (opt.isPresent()) {
            Mascota mascota = opt.get();
            if (!mascota.getUsuario().getId().equals(idUsuario)) {
                throw new ErrorServicio("No tiene permiso para eliminar esta mascota");
            }
            mascota.setBaja(new Date());
            mascotaRepositorio.save(mascota);
        } else {
            throw new ErrorServicio("No se encontró la mascota solicitada");
        }
    }

    private void validar(String nombre, Sexo sexo) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre de la mascota no puede ser nulo o vacío");
        }
        if (sexo == null) {
            throw new ErrorServicio("El sexo de la mascota no puede ser nulo");
        }
    }




}
