package nexora.proyectointegrador2.business.persistence.repository;

import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.Documento;
import nexora.proyectointegrador2.business.enums.TipoDocumentacion;

@Repository
public interface DocumentoRepository extends BaseRepository<Documento, String> {

    // Opcionales, pero útiles si los querés usar luego
    Documento findByNombreArchivo(String nombreArchivo);

    Documento findByTipoDocumento(TipoDocumentacion tipo);
}
