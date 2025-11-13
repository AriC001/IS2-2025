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
    
    // Validar que el tipo MIME sea jpg o png
    String mimeType = entity.getMime().toLowerCase().trim();
    if (!mimeType.equals("image/jpeg") && !mimeType.equals("image/jpg") && !mimeType.equals("image/png")) {
      throw new Exception("El tipo de archivo debe ser JPG o PNG. Tipo recibido: " + entity.getMime());
    }
    
    // Validar extensión del archivo por nombre
    String nombreArchivo = entity.getNombre().toLowerCase();
    if (!nombreArchivo.endsWith(".jpg") && !nombreArchivo.endsWith(".jpeg") && !nombreArchivo.endsWith(".png")) {
      throw new Exception("La extensión del archivo debe ser .jpg, .jpeg o .png. Archivo recibido: " + entity.getNombre());
    }
    
    if (entity.getContenido() == null || entity.getContenido().length == 0) {
      throw new Exception("El contenido de la imagen es obligatorio");
    }
    if (entity.getTipoImagen() == null) {
      throw new Exception("El tipo de imagen es obligatorio");
    }
  }

}
