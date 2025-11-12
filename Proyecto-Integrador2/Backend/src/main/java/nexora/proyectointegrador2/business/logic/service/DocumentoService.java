package nexora.proyectointegrador2.business.logic.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import nexora.proyectointegrador2.business.domain.entity.Documento;
import nexora.proyectointegrador2.business.enums.TipoDocumentacion;
import nexora.proyectointegrador2.business.persistence.repository.DocumentoRepository;
import nexora.proyectointegrador2.utils.dto.DocumentoDTO;

@Service
public class DocumentoService extends BaseService<Documento, String> {

  private static final Logger logger = LoggerFactory.getLogger(DocumentoService.class);

  @Value("${documentos.dir:uploads/documentos}")
  private String directorioBase;

  public DocumentoService(DocumentoRepository repository) {
    super(repository);
    logger.info("=================================================");
    logger.info("📁 DocumentoService - Directorio de documentos: {}", "(inicializando)");
    logger.info("=================================================");
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
   * Sube un documento (PDF o Word) al filesystem y lo persiste en la BD.
   * Devuelve un DTO con metadatos.
   */
  @Transactional
  public DocumentoDTO subirDocumento(MultipartFile file, String tipoDocumentoStr, String clienteId, String alquilerId) throws Exception {
    if (file == null || file.isEmpty()) {
      throw new Exception("El archivo está vacío o es nulo.");
    }

    String contentType = file.getContentType();
    if (!esTipoPermitido(contentType)) {
      throw new Exception("Tipo de archivo no permitido. Sólo PDF y Word.");
    }

    TipoDocumentacion tipoEnum;
    try {
      tipoEnum = TipoDocumentacion.valueOf(tipoDocumentoStr);
    } catch (Exception e) {
      throw new Exception("Tipo de documentación inválido: " + tipoDocumentoStr);
    }

    Path uploadPath = Paths.get(directorioBase);
    logger.info("📂 Verificando directorio de subida: {}", uploadPath.toAbsolutePath());
    try {
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
        logger.info("✅ Directorio creado: {}", uploadPath.toAbsolutePath());
      }
    } catch (IOException e) {
      logger.error("❌ Error creando directorio de upload: {}", e.getMessage());
      throw new Exception("No se pudo crear el directorio: " + e.getMessage(), e);
    }

    String originalName = file.getOriginalFilename();
    String safeOriginal = (originalName == null) ? "archivo" : Paths.get(originalName).getFileName().toString();
    safeOriginal = sanitizarNombre(safeOriginal);

    String nombreFisico = UUID.randomUUID().toString() + "_" + safeOriginal;
    Path destino = uploadPath.resolve(nombreFisico);

    try {
      Files.copy(file.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
      logger.info("✅ Archivo guardado en disco: {} ({} bytes)", destino.toAbsolutePath(), Files.size(destino));
    } catch (IOException e) {
      logger.error("❌ Error guardando archivo en disco: {}", e.getMessage());
      throw new Exception("Error al guardar el archivo en disco: " + e.getMessage(), e);
    }

    // Crear entidad Documento y persistir
    Documento documento = Documento.builder()
        .tipoDocumento(tipoEnum)
        .nombreArchivo(originalName != null ? originalName : nombreFisico)
        .pathArchivo(destino.toString())
        .observacion(null)
        .build();

    Documento guardado;
    try {
      guardado = this.repository.save(documento);
    } catch (Exception e) {
      // Intentar compensación: borrar archivo físico si persistencia falla
      try {
        Files.deleteIfExists(destino);
        logger.warn("⚠ Archivo borrado del disco por fallo en persistencia: {}", destino);
      } catch (IOException ex) {
        logger.error("❌ No se pudo borrar archivo tras fallo en persistencia: {}", ex.getMessage());
      }
      throw new Exception("Error al persistir documento: " + e.getMessage(), e);
    }

    DocumentoDTO dto = new DocumentoDTO();
    dto.setId(guardado.getId());
    dto.setNombreArchivo(guardado.getNombreArchivo());
    dto.setTipoDocumento(guardado.getTipoDocumento());
    dto.setPathArchivo(guardado.getPathArchivo());

    logger.info("📄 Documento persistido: id={} path={}", guardado.getId(), guardado.getPathArchivo());
    return dto;
  }

  /**
   * Método auxiliar: guarda un archivo en disco con el esquema de nombres seguro.
   * Mantiene compatibilidad con el código previo que usaba `guardarArchivoEnDisco`.
   */
  public Path guardarArchivoEnDisco(MultipartFile archivo, String nombreCliente, String nombreVehiculo) throws Exception {
    // Delegar a subirDocumento pero sin persistir la entidad: simplificamos y reutilizamos lógica
    if (archivo == null || archivo.isEmpty()) {
      throw new Exception("El archivo no puede estar vacío");
    }
    String originalName = archivo.getOriginalFilename();
    String safeOriginal = (originalName == null) ? "archivo" : Paths.get(originalName).getFileName().toString();
    safeOriginal = sanitizarNombre(safeOriginal);
    String nombreFisico = UUID.randomUUID().toString() + "_" + safeOriginal;

    Path uploadPath = Paths.get(directorioBase);
    try {
      if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
      Path destino = uploadPath.resolve(nombreFisico);
      Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
      logger.info("✅ Archivo guardado exitosamente: {}", destino.toAbsolutePath());
      return destino;
    } catch (IOException e) {
      logger.error("❌ ERROR al guardar archivo: {}", e.getMessage());
      throw new Exception("Error al guardar el archivo: " + e.getMessage(), e);
    }
  }

  private boolean esTipoPermitido(String tipo) {
    return tipo != null && (
        tipo.equals("application/pdf") ||
        tipo.equals("application/msword") ||
        tipo.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    );
  }

  private String sanitizarNombre(String nombre) {
    if (nombre == null || nombre.trim().isEmpty()) return "archivo";
      // In a Java string literal we don't need to escape '.' or '-' inside a character class;
      // using [^a-zA-Z0-9._-] avoids illegal escape sequences like "\-" in the source.
      return nombre.replaceAll("[^a-zA-Z0-9._-]", "_");
  }

  public byte[] obtenerArchivo(String documentoId) throws Exception {
    Documento documento = findById(documentoId);
    if (documento.getPathArchivo() == null || documento.getPathArchivo().trim().isEmpty()) {
      throw new Exception("El documento no tiene ruta de archivo asociada");
    }
    try {
      Path rutaArchivo = Paths.get(documento.getPathArchivo());
      if (!Files.exists(rutaArchivo)) throw new Exception("El archivo no existe en el sistema de archivos: " + rutaArchivo);
      return Files.readAllBytes(rutaArchivo);
    } catch (IOException e) {
      logger.error("❌ ERROR al leer archivo: {}", e.getMessage());
      throw new Exception("Error al leer el archivo: " + e.getMessage(), e);
    }
  }

  public void eliminarArchivoFisico(String pathArchivo) throws Exception {
    if (pathArchivo == null || pathArchivo.trim().isEmpty()) return;
    try {
      Path rutaArchivo = Paths.get(pathArchivo);
      if (Files.exists(rutaArchivo)) {
        Files.deleteIfExists(rutaArchivo);
        logger.info("🗑️  Archivo eliminado: {}", rutaArchivo);
      }
    } catch (IOException e) {
      logger.error("❌ ERROR al eliminar archivo: {}", e.getMessage());
      throw new Exception("Error al eliminar el archivo físico: " + e.getMessage(), e);
    }
  }

}

