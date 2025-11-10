package nexora.proyectointegrador2.business.logic.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import nexora.proyectointegrador2.business.domain.entity.Documento;
import nexora.proyectointegrador2.business.enums.TipoDocumentacion;
import nexora.proyectointegrador2.business.persistence.repository.DocumentoRepository;
import nexora.proyectointegrador2.utils.dto.DocumentoDTO;

@Service
public class DocumentoService extends BaseService<Documento, String> {

    // Puedes mover esta ruta a application.properties si querés configurarla.
    private static final String UPLOAD_DIR = "uploads/documentos/";

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

    /**
     * Sube un documento PDF o Word al servidor y lo registra en la base de datos.
     */
    public DocumentoDTO subirDocumento(MultipartFile file, String tipoDocumento, String clienteId, String alquilerId) throws Exception {

        if (file == null || file.isEmpty()) {
            throw new Exception("El archivo está vacío o es nulo.");
        }

        // Validar tipo MIME permitido
        String contentType = file.getContentType();
        if (!esTipoPermitido(contentType)) {
            throw new Exception("Tipo de archivo no permitido. Sólo PDF y Word.");
        }

        // Validar tipoDocumento recibido
        TipoDocumentacion tipoEnum;
        try {
            tipoEnum = TipoDocumentacion.valueOf(tipoDocumento);
        } catch (IllegalArgumentException e) {
            throw new Exception("Tipo de documentación inválido: " + tipoDocumento);
        }

        // Preparar carpeta de destino
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generar nombre único y ruta de destino
        String nombreOriginal = file.getOriginalFilename();
        String nombreFisico = UUID.randomUUID().toString() + (nombreOriginal != null ? "_" + nombreOriginal : "");
        Path destino = uploadPath.resolve(nombreFisico);

        // Copiar archivo al filesystem
        try {
            Files.copy(file.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new Exception("Error al guardar el archivo en el servidor: " + e.getMessage(), e);
        }

        // Crear la entidad Documento
        Documento documento = Documento.builder()
                .tipoDocumento(tipoEnum)
                .nombreArchivo(nombreOriginal != null ? nombreOriginal : nombreFisico)
                .pathArchivo(destino.toString())
                .observacion(null)
                .build();

        Documento guardado = this.repository.save(documento);

        // Construir DTO de respuesta
        DocumentoDTO dto = new DocumentoDTO();
        dto.setId(guardado.getId());
        dto.setNombreArchivo(guardado.getNombreArchivo());
        dto.setTipoDocumento(guardado.getTipoDocumento()); // ✅ Enum, no String
        dto.setPathArchivo(guardado.getPathArchivo());
        // Opcional: si luego agregás descarga
        // dto.setUrlDescarga("/api/v1/documentos/descargar/" + guardado.getId());

        return dto;
    }

    private boolean esTipoPermitido(String tipo) {
        return tipo != null && (
                tipo.equals("application/pdf") ||
                tipo.equals("application/msword") ||
                tipo.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
        );
    }
}
