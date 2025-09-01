package edu.egg.tinder.servicios;

import edu.egg.tinder.entidades.Foto;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.repositorios.FotoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class FotoServicio {
    @Autowired
    private FotoRepositorio fotoRepositorio;

    public Foto guardar(MultipartFile archivo) throws ErrorServicio {
        if (archivo != null && !archivo.isEmpty()) {
            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());

                return fotoRepositorio.save(foto);
            } catch (IOException e) {
                throw new ErrorServicio("Error al guardar la foto: " + e.getMessage());
            }
        } else {
            return null;
        }
    }

    public Foto actualizar(Long idFoto, MultipartFile archivo) throws ErrorServicio {
        if (archivo != null && !archivo.isEmpty()) {
            try {
                Optional<Foto> opt = fotoRepositorio.findById(idFoto);
                if (opt.isPresent()) {
                    Foto foto = opt.get();
                    foto.setMime(archivo.getContentType());
                    foto.setNombre(archivo.getName());
                    foto.setContenido(archivo.getBytes());

                    return fotoRepositorio.save(foto);
                } else {
                    throw new ErrorServicio("No se encontró la foto solicitada");
                }
            } catch (IOException e) {
                throw new ErrorServicio("Error al actualizar la foto: " + e.getMessage());
            }
        } else {
            return null;
        }
    }

}
