package com.example.etemplate.servicios;


import com.example.etemplate.entities.Imagen;
import com.example.etemplate.repositories.ImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ImageService extends GenericServiceImpl<Imagen, String>{

    public ImageService(ImagenRepository repository) {
       super(repository);
    }
/*
    public Imagen guardar(MultipartFile archivo) throws ErrorServicio {
        if (archivo != null && !archivo.isEmpty()) {
            try {
                Imagen foto = new Imagen();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());

                return imagenRepository.save(foto);
            } catch (IOException e) {
                throw new ErrorServicio("Error al guardar la foto: " + e.getMessage());
            }
        } else {
            return null;
        }
    }

    public Imagen actualizar(String idImagen, MultipartFile archivo) throws ErrorServicio {
        if (archivo != null && !archivo.isEmpty()) {
            try {
                Optional<Imagen> opt = imagenRepository.findById(idImagen);
                if (opt.isPresent()) {
                    Imagen foto = opt.get();
                    foto.setMime(archivo.getContentType());
                    foto.setNombre(archivo.getName());
                    foto.setContenido(archivo.getBytes());

                    return imagenRepository.save(foto);
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
*/
}
