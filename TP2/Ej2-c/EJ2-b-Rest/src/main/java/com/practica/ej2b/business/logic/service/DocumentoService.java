package com.practica.ej2b.business.logic.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.practica.ej2b.business.domain.entity.Documento;
import com.practica.ej2b.business.persistence.repository.DocumentoRepository;

import jakarta.transaction.Transactional;

@Service
public class DocumentoService extends BaseService<Documento, Long> {
  
  // Ruta multiplataforma: usa el home del usuario + /biblioteca
  // En Windows: C:/Users/usuario/biblioteca
  // En Linux/Ubuntu: /home/usuario/biblioteca
  private static final String DIRECTORIO_BASE = System.getProperty("user.home") + "/biblioteca/";
  
  public DocumentoService(DocumentoRepository repository) {
    super(repository);
    // Log para debug: mostrar la ruta que se usará
    System.out.println("=================================================");
    System.out.println("📁 DocumentoService - Directorio de PDFs:");
    System.out.println("   " + DIRECTORIO_BASE);
    System.out.println("=================================================");
  }

  @Override
  protected void validar(Documento entity) throws Exception {
    if (entity.getNombreArchivo() == null || entity.getNombreArchivo().trim().isEmpty()) {
      throw new Exception("El nombre del archivo es obligatorio");
    }
    if (entity.getRutaArchivo() == null || entity.getRutaArchivo().trim().isEmpty()) {
      throw new Exception("La ruta del archivo es obligatoria");
    }
    if (entity.getTipoContenido() == null || entity.getTipoContenido().trim().isEmpty()) {
      throw new Exception("El tipo de contenido es obligatorio");
    }
  }

  /**
   * Crea un documento sin guardarlo en BD (para usar con cascade)
   * El archivo físico se guarda, pero la entidad no se persiste
   */
  public Documento crearDocumentoSinGuardar(MultipartFile archivo, String nombreLibro) throws Exception {
    if (archivo == null || archivo.isEmpty()) {
      throw new Exception("El archivo no puede estar vacío");
    }

    // Validar que sea un PDF
    String contentType = archivo.getContentType();
    if (contentType == null || !contentType.equals("application/pdf")) {
      throw new Exception("Solo se permiten archivos PDF");
    }

    // Crear directorio si no existe
    Path directorioPath = Paths.get(DIRECTORIO_BASE);
    System.out.println("📂 Verificando directorio: " + directorioPath.toAbsolutePath());
    
    if (!Files.exists(directorioPath)) {
      System.out.println("⚠️  El directorio no existe, creándolo...");
      try {
        Files.createDirectories(directorioPath);
        System.out.println("✅ Directorio creado exitosamente");
      } catch (IOException e) {
        System.err.println("❌ ERROR al crear directorio: " + e.getMessage());
        throw new Exception("No se pudo crear el directorio: " + e.getMessage(), e);
      }
    } else {
      System.out.println("✅ El directorio ya existe");
    }

    // Crear nombre del archivo: libro_nombrelibro_.pdf
    String nombreArchivo = "libro_" + sanitizarNombre(nombreLibro) + "_.pdf";
    Path rutaArchivo = directorioPath.resolve(nombreArchivo);
    System.out.println("💾 Guardando archivo en: " + rutaArchivo.toAbsolutePath());

    // Guardar el archivo
    try {
      Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
      System.out.println("✅ Archivo guardado exitosamente");
    } catch (IOException e) {
      System.err.println("❌ ERROR al guardar archivo: " + e.getMessage());
      throw new Exception("Error al guardar el archivo: " + e.getMessage(), e);
    }

    // Crear la entidad Documento (sin guardar en BD)
    Documento documento = new Documento();
    documento.setNombreArchivo(nombreArchivo);
    documento.setRutaArchivo(rutaArchivo.toString());
    documento.setTipoContenido(archivo.getContentType());
    documento.setEliminado(false);

    return documento;
  }

  @Transactional
  public Documento guardarDocumento(MultipartFile archivo, String nombreLibro) throws Exception {
    Documento documento = crearDocumentoSinGuardar(archivo, nombreLibro);
    return save(documento);
  }

  @Transactional
  public void eliminarArchivo(Long documentoId) throws Exception {
    Documento documento = findById(documentoId);
    
    // Eliminar el archivo físico
    try {
      Path rutaArchivo = Paths.get(documento.getRutaArchivo());
      Files.deleteIfExists(rutaArchivo);
    } catch (IOException e) {
      throw new Exception("Error al eliminar el archivo físico: " + e.getMessage(), e);
    }

    // Eliminar lógicamente el documento
    logicDelete(documentoId);
  }

  public byte[] obtenerArchivo(Long documentoId) throws Exception {
    Documento documento = findById(documentoId);
    
    try {
      Path rutaArchivo = Paths.get(documento.getRutaArchivo());
      if (!Files.exists(rutaArchivo)) {
        throw new Exception("El archivo no existe en el sistema de archivos");
      }
      return Files.readAllBytes(rutaArchivo);
    } catch (IOException e) {
      throw new Exception("Error al leer el archivo: " + e.getMessage(), e);
    }
  }

  /**
   * Actualiza/reemplaza el archivo PDF de un documento existente
   */
  @Transactional
  public Documento actualizarArchivo(Long documentoId, MultipartFile nuevoArchivo, String nombreLibro) throws Exception {
    if (nuevoArchivo == null || nuevoArchivo.isEmpty()) {
      throw new Exception("El archivo no puede estar vacío");
    }

    // Validar que sea un PDF
    String contentType = nuevoArchivo.getContentType();
    if (contentType == null || !contentType.equals("application/pdf")) {
      throw new Exception("Solo se permiten archivos PDF");
    }

    // Obtener el documento existente
    Documento documentoExistente = findById(documentoId);
    
    // Eliminar el archivo anterior
    try {
      Path rutaArchivoAnterior = Paths.get(documentoExistente.getRutaArchivo());
      Files.deleteIfExists(rutaArchivoAnterior);
      System.out.println("🗑️  Archivo anterior eliminado: " + rutaArchivoAnterior);
    } catch (IOException e) {
      System.err.println("⚠️  Advertencia: No se pudo eliminar el archivo anterior: " + e.getMessage());
      // Continuar aunque no se pueda eliminar el archivo anterior
    }

    // Crear el nuevo nombre del archivo
    String nombreArchivo = "libro_" + sanitizarNombre(nombreLibro) + "_.pdf";
    Path directorioPath = Paths.get(DIRECTORIO_BASE);
    Path rutaNuevaArchivo = directorioPath.resolve(nombreArchivo);

    // Guardar el nuevo archivo
    try {
      Files.copy(nuevoArchivo.getInputStream(), rutaNuevaArchivo, StandardCopyOption.REPLACE_EXISTING);
      System.out.println("💾 Nuevo archivo guardado: " + rutaNuevaArchivo.toAbsolutePath());
    } catch (IOException e) {
      throw new Exception("Error al guardar el nuevo archivo: " + e.getMessage(), e);
    }

    // Actualizar los datos del documento
    documentoExistente.setNombreArchivo(nombreArchivo);
    documentoExistente.setRutaArchivo(rutaNuevaArchivo.toString());
    documentoExistente.setTipoContenido(nuevoArchivo.getContentType());

    return update(documentoExistente, documentoId);
  }

  private String sanitizarNombre(String nombre) {
    if (nombre == null) {
      return "sin_nombre";
    }
    // Reemplazar caracteres no válidos para nombres de archivo
    return nombre.replaceAll("[^a-zA-Z0-9\\-_]", "_").toLowerCase();
  }

}
