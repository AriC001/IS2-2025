package edu.egg.tinder.servicios;
import edu.egg.tinder.entidades.Voto;
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

import java.util.Collection;
import java.util.Date;
import java.util.List;
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
    public void crearMascota(Long idUsuario, String nombreMascota, Sexo sexo,MultipartFile archivo) throws ErrorServicio {
        validar(nombreMascota, sexo);

        Optional<Usuario> usuario = usuarioRepositorio.findById(idUsuario);
        if (usuario.isPresent()) {
            Mascota mascota = Mascota.builder()
                    .nombre(nombreMascota)
                    .sexo(sexo)
                    .alta(new Date())
                    .usuario(usuario.get())
                    .activo(true)
                    .build();

            Foto foto = fotoServicio.guardar(archivo);
            mascota.setFoto(foto);

            mascotaRepositorio.save(mascota);
        } else {
            // Manejar el caso en que el usuario no existe
            throw new ErrorServicio("Usuario no encontrado");
        }
    }



    @Transactional
    public void modificar(MultipartFile archivo, Long idUsuario, Long idMascota, String nombreMascota, Sexo sexo,Boolean activo) throws ErrorServicio {
        validar(nombreMascota, sexo);
        System.out.println(idMascota);
        Mascota mascota = new Mascota();
        try{
            mascota = mascotaRepositorio.findByid(idMascota);
        }catch (Exception e){
            throw new ErrorServicio("No se encontró la mascota solicitada");
        }
        if (!mascota.getUsuario().getId().equals(idUsuario)) {
            throw new ErrorServicio("No tiene permiso para modificar esta mascota");
        }
        mascota.setNombre(nombreMascota);
        mascota.setSexo(sexo);
        mascota.setActivo(activo);

        if (archivo != null && !archivo.isEmpty()) {
            if (mascota.getFoto() != null) {
                Foto actualizada = fotoServicio.actualizar(mascota.getFoto().getId(), archivo);
                mascota.setFoto(actualizada);
            } else {
                Foto nueva = fotoServicio.guardar(archivo);
                mascota.setFoto(nueva);
            }
        }

        mascotaRepositorio.save(mascota);
    }

    @Transactional
    public void eliminar(Long idUsuario,Long idMascota) throws ErrorServicio {
        Mascota mascota = new Mascota();
        try{
            mascota = mascotaRepositorio.findByid(idMascota);
        }catch (Exception e){
            throw new ErrorServicio("No se encontró la mascota solicitada");
        }
        if (!mascota.getUsuario().getId().equals(idUsuario)) {
            throw new ErrorServicio("No tiene permiso para eliminar esta mascota");
        }
        mascota.setBaja(new Date());
        mascota.setActivo(false);
        mascotaRepositorio.save(mascota);
    }

    private void validar(String nombre, Sexo sexo) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre de la mascota no puede ser nulo o vacío");
        }
        if (sexo == null) {
            throw new ErrorServicio("El sexo de la mascota no puede ser nulo");
        }
    }

    public List<Mascota> listarMascotas(Long id) throws ErrorServicio {
        List<Mascota> mascotas;
        try{
            mascotas = mascotaRepositorio.findAllMascotasByUsario(id);
            return mascotas;
        }catch (Exception e){
           throw new ErrorServicio("No se encontraron Mascotas");
        }

    }

    public List<Mascota> listarMascotasInactivas(Long id) throws ErrorServicio {
        List<Mascota> mascotas;
        try{
            mascotas = mascotaRepositorio.findAllMascotasInactivasByUsuario(id);
            return mascotas;
        }catch (Exception e){
            throw new ErrorServicio("No se encontraron Mascotas Inactivas");
        }

    }

    public Mascota buscarMascota(Long id)throws ErrorServicio {
        Mascota mascota = new Mascota();
        try{
            mascota = mascotaRepositorio.findByid(id);
        }catch (Exception e){
            throw new ErrorServicio("No se encontró la mascota solicitada");
        }
        return mascota;
    }
    public List<Mascota> listarAllMascotas() throws ErrorServicio{
        List<Mascota> mascotas;
        try{
            mascotas= mascotaRepositorio.findAllMascotasActivas();
            return mascotas;
        }catch(Exception e){
            throw new ErrorServicio("No se encontraron mascotas activas");
        }
    }

    public List<Mascota> votantesDe(Mascota mascota) {
        // Cada voto recibido tiene "mascota1" como quien votó
        return mascota.getVotosRecibidos()
                .stream()
                .map(Voto::getMascota1)
                .toList();
        //recibo la lista de votos que emití
        // Stream(): Convierte la lista en un flujo (stream), lo que permite operar sobre sus elementos de manera funcional (filtrar, mapear, contar, etc.).
        //map mapea cada elemento que era un Voto, y Voto::getMascota1 toma la mascota que emitio ese voto
        //toList me devuelve la lista de mascotas
    }

    public List<Mascota> votoDado(Mascota mascota) {
        // Cada voto enviado tiene "mascota1" como quien votó por ende necesitamos mascota2 par ver quien lo recibio
        return mascota.getVotosOriginados()
                .stream()
                .map(Voto::getMascota2)
                .toList();
        //recibo la lista de votos que emití
        // Stream(): Convierte la lista en un flujo (stream), lo que permite operar sobre sus elementos de manera funcional (filtrar, mapear, contar, etc.).
        //map mapea cada elemento que era un Voto, y Voto::getMascota2 toma la mascota que recibió ese voto
        //toList me devuelve la lista de mascotas
    }

    public boolean huboVotoReciproco(Mascota votante, Mascota votada) {
        // Buscamos si votante recibió un voto de la mascota votada
        return votante.getVotosRecibidos()
                .stream()
                .anyMatch(v -> v.getMascota1().equals(votada));
    }

    public List<Voto> votosDe(Mascota mascota) {
        return mascota.getVotosRecibidos();
    }

}


