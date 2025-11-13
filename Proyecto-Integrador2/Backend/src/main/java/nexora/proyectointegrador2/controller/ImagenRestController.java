package nexora.proyectointegrador2.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Imagen;
import nexora.proyectointegrador2.business.logic.service.ImagenService;
import nexora.proyectointegrador2.utils.dto.ImagenDTO;
import nexora.proyectointegrador2.utils.mapper.impl.ImagenMapper;

@RestController
@RequestMapping("api/v1/imagenes")
public class ImagenRestController extends BaseRestController<Imagen, ImagenDTO, String> {
  
  private final ImagenService imagenService;
  
  public ImagenRestController(ImagenService service, ImagenMapper mapper) {
    super(service, mapper);
    this.imagenService = service;
  }

  /**
   * Obtiene la imagen como respuesta HTTP directa con el tipo MIME correcto.
   * Útil para mostrar imágenes en etiquetas <img>.
   */
  @GetMapping("/{id}/imagen")
  public ResponseEntity<byte[]> obtenerImagen(@PathVariable String id) throws Exception {
    Imagen imagen = imagenService.findById(id);
    
    if (imagen == null || imagen.getContenido() == null || imagen.getContenido().length == 0) {
      return ResponseEntity.notFound().build();
    }
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(imagen.getMime() != null ? imagen.getMime() : "image/jpeg"));
    headers.setContentLength(imagen.getContenido().length);
    headers.setCacheControl("max-age=3600");
    
    return new ResponseEntity<>(imagen.getContenido(), headers, HttpStatus.OK);
  }
}

