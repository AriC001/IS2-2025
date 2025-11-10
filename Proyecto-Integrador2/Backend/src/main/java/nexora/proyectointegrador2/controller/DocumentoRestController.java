package nexora.proyectointegrador2.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import nexora.proyectointegrador2.business.domain.entity.Documento;
import nexora.proyectointegrador2.business.enums.TipoDocumentacion;
import nexora.proyectointegrador2.business.logic.service.DocumentoService;
import nexora.proyectointegrador2.utils.dto.DocumentoDTO;
import nexora.proyectointegrador2.utils.mapper.impl.DocumentoMapper;

@RestController
@RequestMapping("api/v1/documentos")
public class DocumentoRestController extends BaseRestController<Documento, DocumentoDTO, String> {

    private final DocumentoService documentoService;

    public DocumentoRestController(DocumentoService service, DocumentoMapper mapper) {
        super(service, mapper);
        this.documentoService = service;
    }

    /**
     * Subir un documento (PDF / DOC / DOCX) y registrarlo en la BD.
     * Parámetros:
     *  - file: multipart file
     *  - tipoDocumento: "DNI" o "CARNET_DE_CONDUCIR" (los nombres del enum TipoDocumentacion)
     *  - clienteId / alquilerId: opcionales
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentoDTO> uploadDocumento(
            @RequestParam("file") MultipartFile file,
            @RequestParam("tipoDocumento") String tipoDocumento,
            @RequestParam(value = "clienteId", required = false) String clienteId,
            @RequestParam(value = "alquilerId", required = false) String alquilerId) throws Exception {

        // Llamamos al service con el tipoDocumento como String
        DocumentoDTO dto = documentoService.subirDocumento(file, tipoDocumento, clienteId, alquilerId);
        return ResponseEntity.ok(dto);
    }

    /**
     * Descargar documento por id (id es String porque BaseEntity usa String).
     */
    @GetMapping("/descargar/{id}")
    public ResponseEntity<Resource> descargarDocumento(@PathVariable String id) throws IOException, Exception {
        Documento documento = documentoService.findById(id);
        if (documento == null || documento.getPathArchivo() == null) {
            return ResponseEntity.notFound().build();
        }

        Path path = Paths.get(documento.getPathArchivo());
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = Files.probeContentType(path);
        MediaType mediaType = contentType != null
                ? MediaType.parseMediaType(contentType)
                : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + documento.getNombreArchivo() + "\"")
                .body(resource);
    }
}
