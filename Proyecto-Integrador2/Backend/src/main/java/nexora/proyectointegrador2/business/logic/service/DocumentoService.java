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

  @Override
  protected void preAlta(Documento entity) throws Exception {
    // Establecer fecha de carga si no está establecida
    if (entity.getFechaCarga() == null) {
      entity.setFechaCarga(LocalDateTime.now());
    }
    // Establecer mime type por defecto si no está establecido
    if (entity.getMimeType() == null || entity.getMimeType().trim().isEmpty()) {
      String nombreArchivo = entity.getNombreArchivo();
      if (nombreArchivo != null && nombreArchivo.contains(".")) {
        int lastDot = nombreArchivo.lastIndexOf('.');
        if (lastDot >= 0 && lastDot < nombreArchivo.length() - 1) {
          String extension = nombreArchivo.substring(lastDot + 1).toLowerCase();
          if (extension.equals("pdf")) {
            entity.setMimeType("application/pdf");
          } else if (extension.equals("doc") || extension.equals("docx")) {
            entity.setMimeType("application/msword");
          }
        }
      }
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

    // Obtener nombre original y sanitizarlo
    String originalName = file.getOriginalFilename();
    String nombreSanitizado = (originalName == null) ? "archivo" : Paths.get(originalName).getFileName().toString();
    nombreSanitizado = sanitizarNombre(nombreSanitizado);
    
    // El archivo físico se guarda con UUID para evitar colisiones
    String extension = "";
    int lastDot = nombreSanitizado.lastIndexOf('.');
    if (lastDot > 0) {
      extension = nombreSanitizado.substring(lastDot);
      nombreSanitizado = nombreSanitizado.substring(0, lastDot);
    }
    String nombreFisico = UUID.randomUUID().toString() + extension;
    Path destino = uploadPath.resolve(nombreFisico);

    try {
      Files.copy(file.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
      logger.info("✅ Archivo guardado en disco: {} ({} bytes)", destino.toAbsolutePath(), Files.size(destino));
    } catch (IOException e) {
      logger.error("❌ Error guardando archivo en disco: {}", e.getMessage());
      throw new Exception("Error al guardar el archivo en disco: " + e.getMessage(), e);
    }

    // Normalizar mime type
    String mimeTypeNormalizado = normalizarMimeType(contentType);

    // Crear entidad Documento y persistir
    // El nombre en BD es el sanitizado sin UUID, el path contiene el UUID
    Documento documento = Documento.builder()
        .tipoDocumento(tipoEnum)
        .nombreArchivo(nombreSanitizado + extension) // Nombre sanitizado sin UUID
        .pathArchivo(destino.toString()) // Path completo con UUID
        .mimeType(mimeTypeNormalizado) // Mime type normalizado
        .fechaCarga(LocalDateTime.now()) // Fecha de carga
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
    dto.setMimeType(guardado.getMimeType());

    logger.info("📄 Documento persistido: id={} nombre={} path={} mimeType={}", 
        guardado.getId(), guardado.getNombreArchivo(), guardado.getPathArchivo(), guardado.getMimeType());
    return dto;
  }

  /**
   * Método auxiliar: guarda un archivo en disco con el esquema de nombres seguro.
   * El archivo físico se guarda con formato: alquiler_{nombre_cliente}_{marca}_{modelo}_{fecha}_{hora}.{ext}
   * El nombre que se retorna (y se guarda en BD) es el mismo nombre del archivo físico.
   * 
   * @param archivo Archivo a guardar
   * @param nombreCliente Nombre del cliente (ej: "Juan Perez")
   * @param marca Marca del vehículo (ej: "Toyota")
   * @param modelo Modelo del vehículo (ej: "Corolla")
   * @return Información del archivo guardado: [0] = Path completo, [1] = Nombre del archivo, [2] = MimeType
   */
  public Object[] guardarArchivoEnDisco(MultipartFile archivo, String nombreCliente, String marca, String modelo) throws Exception {
    if (archivo == null || archivo.isEmpty()) {
      throw new Exception("El archivo no puede estar vacío");
    }
    
    // Validar tipo de contenido
    String contentType = archivo.getContentType();
    if (contentType == null) {
      throw new Exception("No se pudo determinar el tipo de contenido del archivo");
    }
    
    if (!esTipoPermitido(contentType)) {
      throw new Exception("Tipo de archivo no permitido. Solo se permiten PDF o WORD. Tipo recibido: " + contentType);
    }
    
    // Obtener extensión del archivo original
    String originalName = archivo.getOriginalFilename();
    String extension = ".pdf"; // Por defecto PDF
    if (originalName != null && originalName.contains(".")) {
      int lastDot = originalName.lastIndexOf('.');
      if (lastDot >= 0 && lastDot < originalName.length() - 1) {
        String ext = originalName.substring(lastDot + 1).toLowerCase();
        if (ext.equals("pdf")) {
          extension = ".pdf";
        } else if (ext.equals("doc") || ext.equals("docx")) {
          extension = ".doc";
        }
      }
    }
    
    // Sanitizar componentes del nombre
    String nombreClienteSanitizado = sanitizarNombre(nombreCliente != null ? nombreCliente : "cliente");
    String marcaSanitizada = sanitizarNombre(marca != null ? marca : "marca");
    String modeloSanitizado = sanitizarNombre(modelo != null ? modelo : "modelo");
    
    // Generar fecha y hora en formato YYYYMMDD_HHMMSS
    LocalDateTime ahora = LocalDateTime.now();
    DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HHmmss");
    String fecha = ahora.format(fechaFormatter);
    String hora = ahora.format(horaFormatter);
    
    // Construir nombre del archivo: alquiler_{nombre_cliente}_{marca}_{modelo}_{fecha}_{hora}.{ext}
    String nombreArchivo = String.format("alquiler_%s_%s_%s_%s_%s%s",
        nombreClienteSanitizado,
        marcaSanitizada,
        modeloSanitizado,
        fecha,
        hora,
        extension);

    // Configurar directorio base (uploads/documentos)
    Path uploadPath = Paths.get(directorioBase);
    try {
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
        logger.info("✅ Directorio creado: {}", uploadPath.toAbsolutePath());
      }
    } catch (IOException e) {
      logger.error("❌ Error creando directorio de upload: {}", e.getMessage());
      throw new Exception("No se pudo crear el directorio: " + e.getMessage(), e);
    }

    // Guardar archivo físico con el nombre formateado
    Path destino = uploadPath.resolve(nombreArchivo);
    try {
      Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
      logger.info("✅ Archivo guardado en disco: {} ({} bytes)", destino.toAbsolutePath(), Files.size(destino));
    } catch (IOException e) {
      logger.error("❌ ERROR al guardar archivo: {}", e.getMessage());
      throw new Exception("Error al guardar el archivo: " + e.getMessage(), e);
    }
    
    // Normalizar mime type
    String mimeTypeNormalizado = normalizarMimeType(contentType);
    
    // Retornar: [0] = Path completo, [1] = Nombre del archivo, [2] = MimeType
    return new Object[] { destino, nombreArchivo, mimeTypeNormalizado };
  }
  
  /**
   * Normaliza el mime type a valores estándar: application/pdf o application/msword
   */
  private String normalizarMimeType(String contentType) {
    if (contentType == null) {
      return "application/octet-stream";
    }
    if (contentType.equals("application/pdf")) {
      return "application/pdf";
    }
    if (contentType.equals("application/msword") || 
        contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
      return "application/msword";
    }
    return contentType;
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
    // Reemplazar espacios y caracteres especiales con guiones bajos
    // Mantener letras, números, puntos, guiones y guiones bajos
    String sanitizado = nombre.trim().replaceAll("[^a-zA-Z0-9._-]", "_");
    // Reemplazar múltiples guiones bajos consecutivos con uno solo
    sanitizado = sanitizado.replaceAll("_{2,}", "_");
    // Eliminar guiones bajos al inicio y final
    sanitizado = sanitizado.replaceAll("^_+|_+$", "");
    return sanitizado.isEmpty() ? "archivo" : sanitizado;
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

