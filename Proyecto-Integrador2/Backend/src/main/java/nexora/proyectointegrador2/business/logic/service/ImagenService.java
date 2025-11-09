package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Imagen;
import nexora.proyectointegrador2.business.persistence.repository.ImagenRepository;

@Service
public class ImagenService extends BaseService<Imagen, String> {

  public ImagenService(ImagenRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(Imagen entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
      throw new Exception("El nombre de la imagen es obligatorio");
    }
    if (entity.getMime() == null || entity.getMime().trim().isEmpty()) {
      throw new Exception("El tipo MIME es obligatorio");
    }
    if (entity.getContenido() == null || entity.getContenido().length == 0) {
      throw new Exception("El contenido de la imagen es obligatorio");
    }
    if (entity.getTipoImagen() == null) {
      throw new Exception("El tipo de imagen es obligatorio");
    }
  }

}
