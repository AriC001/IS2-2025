package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Documento;
import nexora.proyectointegrador2.business.logic.service.DocumentoService;
import nexora.proyectointegrador2.utils.dto.DocumentoDTO;
import nexora.proyectointegrador2.utils.mapper.impl.DocumentoMapper;

@RestController
@RequestMapping("api/v1/documentos")
public class DocumentoRestController extends BaseRestController<Documento, DocumentoDTO, String> {
  public DocumentoRestController(DocumentoService service, DocumentoMapper mapper) {
    super(service, mapper);
  }
}

