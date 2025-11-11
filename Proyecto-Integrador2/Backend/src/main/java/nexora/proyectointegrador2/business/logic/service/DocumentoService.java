package nexora.proyectointegrador2.business.logic.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import nexora.proyectointegrador2.business.domain.entity.Documento;
import nexora.proyectointegrador2.business.persistence.repository.DocumentoRepository;

@Service
public class DocumentoService extends BaseService<Documento, String> {

  private static final Logger logger = LoggerFactory.getLogger(DocumentoService.class);
  
  // Ruta fija en disco C
  private static final String DIRECTORIO_BASE = "C:/documentos_alquileres/";

  public DocumentoService(DocumentoRepository repository) {
    super(repository);
    logger.info("=================================================");
    logger.info("📁 DocumentoService - Directorio de documentos:");
    logger.info("   {}", DIRECTORIO_BASE);
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
   * Guarda un archivo en disco y retorna el Path completo del archivo guardado.
   * Valida que sea PDF o WORD.
   * 
   * @param archivo Archivo a guardar
   * @param nombreCliente Nombre del cliente para el nombre del archivo
   * @param nombreVehiculo Nombre/patente del vehículo para el nombre del archivo
   * @return Path completo del archivo guardado
   * @throws Exception Si el archivo es inválido o hay error al guardar
   */
  public Path guardarArchivoEnDisco(MultipartFile archivo, String nombreCliente, String nombreVehiculo) throws Exception {
    if (archivo == null || archivo.isEmpty()) {
      throw new Exception("El archivo no puede estar vacío");
    }

    // Validar tipo de contenido (PDF o WORD)
    String contentType = archivo.getContentType();
    if (contentType == null) {
      throw new Exception("No se pudo determinar el tipo de contenido del archivo");
    }
    
    boolean esPdf = contentType.equals("application/pdf");
    boolean esWord = contentType.equals("application/msword") || 
                     contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    
    if (!esPdf && !esWord) {
      throw new Exception("Solo se permiten archivos PDF o WORD. Tipo recibido: " + contentType);
    }

    // Crear directorio si no existe
    Path directorioPath = Paths.get(DIRECTORIO_BASE);
    logger.info("📂 Verificando directorio: {}", directorioPath.toAbsolutePath());
    
    if (!Files.exists(directorioPath)) {
      logger.info("⚠️  El directorio no existe, creándolo...");
      try {
        Files.createDirectories(directorioPath);
        logger.info("✅ Directorio creado exitosamente");
      } catch (IOException e) {
        logger.error("❌ ERROR al crear directorio: {}", e.getMessage());
        throw new Exception("No se pudo crear el directorio: " + e.getMessage(), e);
      }
    } else {
      logger.info("✅ El directorio ya existe");
    }

    // Generar nombre del archivo: alquiler_{cliente}_{vehiculo}_{timestamp}.{ext}
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    String nombreClienteSanitizado = sanitizarNombre(nombreCliente);
    String nombreVehiculoSanitizado = sanitizarNombre(nombreVehiculo);
    
    // Determinar extensión según el tipo
    String extension = esPdf ? "pdf" : "docx";
    if (contentType.equals("application/msword")) {
      extension = "doc"; // Word antiguo
    }
    
    String nombreArchivo = String.format("alquiler_%s_%s_%s.%s", 
        nombreClienteSanitizado, nombreVehiculoSanitizado, timestamp, extension);
    
    Path rutaArchivo = directorioPath.resolve(nombreArchivo);
    logger.info("💾 Guardando archivo en: {}", rutaArchivo.toAbsolutePath());

    // Guardar el archivo
    try {
      Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
      logger.info("✅ Archivo guardado exitosamente: {}", nombreArchivo);
    } catch (IOException e) {
      logger.error("❌ ERROR al guardar archivo: {}", e.getMessage());
      throw new Exception("Error al guardar el archivo: " + e.getMessage(), e);
    }

    return rutaArchivo;
  }

  /**
   * Sanitiza un nombre para usarlo en nombres de archivo.
   * Reemplaza caracteres no válidos por guiones bajos.
   */
  private String sanitizarNombre(String nombre) {
    if (nombre == null || nombre.trim().isEmpty()) {
      return "sin_nombre";
    }
    // Reemplazar caracteres no válidos para nombres de archivo
    return nombre.replaceAll("[^a-zA-Z0-9\\-_]", "_").toLowerCase();
  }

  /**
   * Lee un archivo del disco y retorna su contenido como array de bytes.
   * 
   * @param documentoId ID del documento
   * @return Contenido del archivo
   * @throws Exception Si el documento no existe o hay error al leer
   */
  public byte[] obtenerArchivo(String documentoId) throws Exception {
    Documento documento = findById(documentoId);
    
    if (documento.getPathArchivo() == null || documento.getPathArchivo().trim().isEmpty()) {
      throw new Exception("El documento no tiene ruta de archivo asociada");
    }
    
    try {
      Path rutaArchivo = Paths.get(documento.getPathArchivo());
      if (!Files.exists(rutaArchivo)) {
        throw new Exception("El archivo no existe en el sistema de archivos: " + rutaArchivo);
      }
      return Files.readAllBytes(rutaArchivo);
    } catch (IOException e) {
      logger.error("❌ ERROR al leer archivo: {}", e.getMessage());
      throw new Exception("Error al leer el archivo: " + e.getMessage(), e);
    }
  }

  /**
   * Elimina un archivo físico del disco.
   * 
   * @param pathArchivo Ruta del archivo a eliminar
   * @throws Exception Si hay error al eliminar
   */
  public void eliminarArchivoFisico(String pathArchivo) throws Exception {
    if (pathArchivo == null || pathArchivo.trim().isEmpty()) {
      return; // No hay archivo que eliminar
    }
    
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

