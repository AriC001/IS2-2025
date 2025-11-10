package com.practica.ej2b.controller.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.practica.ej2b.business.domain.dto.DocumentoDTO;
import com.practica.ej2b.business.domain.entity.Documento;
import com.practica.ej2b.business.logic.adapter.impl.DocumentoAdapter;
import com.practica.ej2b.business.logic.service.DocumentoService;

@RestController
@RequestMapping("api/v1/documentos")
public class DocumentoRestController extends BaseRestController<Documento, DocumentoDTO, Long> {

  private final DocumentoService documentoService;

  public DocumentoRestController(DocumentoService service, DocumentoAdapter adapter) {
    super(service, adapter);
    this.documentoService = service;
  }

  @PostMapping("/upload")
  public ResponseEntity<DocumentoDTO> uploadDocumento(
      @RequestParam("archivo") MultipartFile archivo,
      @RequestParam("nombreLibro") String nombreLibro) {
    try {
      Documento documento = documentoService.guardarDocumento(archivo, nombreLibro);
      return ResponseEntity.ok(adapter.toDTO(documento));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/download/{id}")
  public ResponseEntity<byte[]> downloadDocumento(@PathVariable Long id) {
    try {
      Documento documento = documentoService.findById(id);
      byte[] archivo = documentoService.obtenerArchivo(id);
      
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_PDF);
      headers.setContentDispositionFormData("inline", documento.getNombreArchivo());
      headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
      
      return ResponseEntity.ok()
          .headers(headers)
          .body(archivo);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/actualizar/{id}")
  public ResponseEntity<DocumentoDTO> actualizarDocumento(
      @PathVariable Long id,
      @RequestParam("archivo") MultipartFile archivo,
      @RequestParam("nombreLibro") String nombreLibro) {
    try {
      Documento documentoActualizado = documentoService.actualizarArchivo(id, archivo, nombreLibro);
      return ResponseEntity.ok(adapter.toDTO(documentoActualizado));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

}
