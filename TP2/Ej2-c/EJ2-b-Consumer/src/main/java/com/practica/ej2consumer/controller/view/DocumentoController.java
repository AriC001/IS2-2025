package com.practica.ej2consumer.controller.view;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.practica.ej2consumer.business.domain.dto.DocumentoDTO;
import com.practica.ej2consumer.business.logic.service.DocumentoService;

@Controller
@RequestMapping("/documentos")
public class DocumentoController extends BaseController<DocumentoDTO, Long> {

  private final DocumentoService documentoService;

  public DocumentoController(DocumentoService service) {
    super(service, "documentos");
    this.documentoService = service;
  }

  @Override
  protected DocumentoDTO createNewEntity() {
    return new DocumentoDTO();
  }

  @GetMapping("/ver/{id}")
  public ResponseEntity<byte[]> verDocumento(@PathVariable Long id) {
    try {
      DocumentoDTO documento = documentoService.findById(id);
      byte[] archivo = documentoService.downloadDocumento(id);
      
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_PDF);
      headers.setContentDispositionFormData("inline", documento.getNombreArchivo());
      headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
      
      return ResponseEntity.ok()
          .headers(headers)
          .body(archivo);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

}
