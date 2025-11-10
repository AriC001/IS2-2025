package com.practica.ej2consumer.business.logic.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.practica.ej2consumer.business.domain.dto.DocumentoDTO;
import com.practica.ej2consumer.business.persistence.rest.DocumentoDAORest;

@Service
public class DocumentoService extends BaseService<DocumentoDTO, Long> {

  private final DocumentoDAORest documentoDao;

  public DocumentoService(DocumentoDAORest dao) {
    super(dao);
    this.documentoDao = dao;
  }

  @Override
  protected void validateEntity(DocumentoDTO entity) throws Exception {
    if (entity.getNombreArchivo() == null || entity.getNombreArchivo().trim().isEmpty()) {
      throw new Exception("El nombre del archivo no puede ser nulo o vacío");
    }
    if (entity.getRutaArchivo() == null || entity.getRutaArchivo().trim().isEmpty()) {
      throw new Exception("La ruta del archivo no puede ser nula o vacía");
    }
    if (entity.getTipoContenido() == null || entity.getTipoContenido().trim().isEmpty()) {
      throw new Exception("El tipo de contenido no puede ser nulo o vacío");
    }
  }

  public DocumentoDTO uploadDocumento(MultipartFile archivo, String nombreLibro) throws Exception {
    System.out.println("\n📤 DocumentoService.uploadDocumento() [CONSUMER]");
    System.out.println("   - Nombre libro: " + nombreLibro);
    System.out.println("   - Archivo: " + (archivo != null ? archivo.getOriginalFilename() : "NULL"));
    System.out.println("   - Tamaño: " + (archivo != null ? archivo.getSize() + " bytes" : "0"));
    
    try {
      if (archivo == null || archivo.isEmpty()) {
        throw new Exception("El archivo no puede estar vacío");
      }
      
      System.out.println("   ➡️  Llamando a REST API /documentos/upload...");
      DocumentoDTO resultado = documentoDao.uploadDocumento(archivo, nombreLibro);
      System.out.println("   ✅ Respuesta recibida del servidor REST:");
      System.out.println("      - ID: " + resultado.getId());
      System.out.println("      - Nombre: " + resultado.getNombreArchivo());
      System.out.println("      - Ruta: " + resultado.getRutaArchivo());
      
      return resultado;
    } catch (Exception e) {
      System.err.println("   ❌ Error en uploadDocumento: " + e.getMessage());
      throw new Exception("Error al subir el documento: " + e.getMessage(), e);
    }
  }

  public byte[] downloadDocumento(Long id) throws Exception {
    try {
      if (id == null) {
        throw new IllegalArgumentException("ID no puede ser null");
      }
      return documentoDao.downloadDocumento(id);
    } catch (Exception e) {
      throw new Exception("Error al descargar el documento: " + e.getMessage(), e);
    }
  }

  public DocumentoDTO actualizarDocumento(Long id, MultipartFile archivo, String nombreLibro) throws Exception {
    System.out.println("\n📤 DocumentoService.actualizarDocumento() [CONSUMER]");
    System.out.println("   - ID documento: " + id);
    System.out.println("   - Nombre libro: " + nombreLibro);
    System.out.println("   - Archivo: " + (archivo != null ? archivo.getOriginalFilename() : "NULL"));
    
    try {
      if (archivo == null || archivo.isEmpty()) {
        throw new Exception("El archivo no puede estar vacío");
      }
      
      System.out.println("   ➡️  Llamando a REST API /documentos/actualizar/{id}...");
      DocumentoDTO resultado = documentoDao.actualizarDocumento(id, archivo, nombreLibro);
      System.out.println("   ✅ Documento actualizado en el servidor REST");
      
      return resultado;
    } catch (Exception e) {
      System.err.println("   ❌ Error en actualizarDocumento: " + e.getMessage());
      throw new Exception("Error al actualizar el documento: " + e.getMessage(), e);
    }
  }

}
