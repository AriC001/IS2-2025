package edu.egg.tinder.servicios;

import edu.egg.tinder.entidades.Mascota;
import edu.egg.tinder.entidades.Voto;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.repositorios.MascotaRepositorio;
import edu.egg.tinder.repositorios.VotoRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class VotoServicio {
    @Autowired
    private VotoRepositorio votoRepositorio;

    @Autowired
    private MascotaRepositorio mascotaRepositorio;

    @Autowired
    private NotificacionServicio notificacionServicio;

    @Transactional
    public void votar(Long idUsuario,Long idMascota1, Long idMascota2) throws ErrorServicio {
        if (idMascota1.equals(idMascota2)) {
            throw new ErrorServicio("No se puede votarse a uno mismo");
        }

        Optional<Mascota> opt1 = mascotaRepositorio.findById(idMascota1);
        if (opt1.isPresent()) {
            Mascota mascota1 = opt1.get();
            if (!mascota1.getUsuario().getId().equals(idUsuario)) {
                throw new ErrorServicio("No tiene permiso para votar con esta mascota");
            }
        } else {
            throw new ErrorServicio("No se encontró la mascota solicitada");
        }

        Optional<Mascota> opt2 = mascotaRepositorio.findById(idMascota2);
        if (opt2.isPresent()) {
            Mascota mascota2 = opt2.get();
            Voto voto = Voto.builder().fecha(new Date()).mascota1(opt1.get()).mascota2(mascota2).build();

            notificacionServicio.enviarMail("Tu mascota " + mascota2.getNombre() +
                            " ha recibido un nuevo voto!",
                    "Tinder de Mascota",
                    mascota2.getUsuario().getMail());

            votoRepositorio.save(voto);
        } else {
            throw new ErrorServicio("No se encontró la mascota solicitada");
        }
    }

    @Transactional
    public void responder(Long idUsuario, Long idVoto) throws ErrorServicio {
        Optional<Voto> opt = votoRepositorio.findById(idVoto);
        if (opt.isPresent()) {
            Voto voto = opt.get();
            if (!voto.getMascota2().getUsuario().getId().equals(idUsuario)) {
                throw new ErrorServicio("No tiene permiso para responder a este voto");
            }
            voto.setRespuesta(new Date());

            notificacionServicio.enviarMail("Tu voto ha sido respondido!",
                    "Tinder de Mascota",
                    voto.getMascota1().getUsuario().getMail());

            votoRepositorio.save(voto);
        } else {
            throw new ErrorServicio("No se encontró el voto solicitado");
        }
    }

}
