package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Imagen;
import nexora.proyectointegrador2.business.logic.service.ImagenService;
import nexora.proyectointegrador2.utils.dto.ImagenDTO;
import nexora.proyectointegrador2.utils.mapper.impl.ImagenMapper;

@RestController
@RequestMapping("api/v1/imagenes")
public class ImagenRestController extends BaseRestController<Imagen, ImagenDTO, String> {
  public ImagenRestController(ImagenService service, ImagenMapper mapper) {
    super(service, mapper);
  }
}

