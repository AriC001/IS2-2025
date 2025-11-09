package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Documento;
import nexora.proyectointegrador2.business.persistence.repository.DocumentoRepository;

@Service
public class DocumentoService extends BaseService<Documento, String> {

  public DocumentoService(DocumentoRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(Documento entity) throws Exception {
    if (entity.getTipoDocumento() == null) {
      throw new Exception("El tipo de documento es obligatorio");
    }
    if (entity.getPathArchivo() == null || entity.getPathArchivo().trim().isEmpty()) {
      throw new Exception("El path del archivo es obligatorio");
    }
    if (entity.getNombreArchivo() == null || entity.getNombreArchivo().trim().isEmpty()) {
      throw new Exception("El nombre del archivo es obligatorio");
    }
  }

}

