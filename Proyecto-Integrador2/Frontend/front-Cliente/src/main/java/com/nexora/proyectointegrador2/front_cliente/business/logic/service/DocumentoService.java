package com.nexora.proyectointegrador2.front_cliente.business.logic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.nexora.proyectointegrador2.front_cliente.business.persistence.dao.DocumentoDAO;
import com.nexora.proyectointegrador2.front_cliente.dto.DocumentoDTO;

@Service
public class DocumentoService extends BaseService<DocumentoDTO, String> {

  private static final Logger logger = LoggerFactory.getLogger(DocumentoService.class);
  private static final String DOCUMENTO_API_URL = "http://localhost:9000/api/v1/documentos/upload";

  @Autowired
  private RestTemplate restTemplate;

  public DocumentoService(DocumentoDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(DocumentoDTO entity) throws Exception {
    if (entity.getTipoDocumento() == null) {
      throw new Exception("El tipo de documento es obligatorio");
    }
    if (entity.getNombreArchivo() == null || entity.getNombreArchivo().trim().isEmpty()) {
      throw new Exception("El nombre del archivo es obligatorio");
    }
  }

  public DocumentoDTO uploadDocumento(MultipartFile file, String tipoDocumento, String alquilerId) throws Exception {
    try {
      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
      body.add("file", new ByteArrayResource(file.getBytes()) {
        @Override
        public String getFilename() {
          return file.getOriginalFilename();
        }
      });
      body.add("tipoDocumento", tipoDocumento);
      body.add("alquilerId", alquilerId);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);

      HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      ResponseEntity<DocumentoDTO> response = restTemplate.postForEntity(DOCUMENTO_API_URL, requestEntity, DocumentoDTO.class);

      if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
        return response.getBody();
      } else {
        throw new Exception("Error al subir documento. Código HTTP: " + response.getStatusCode());
      }

    } catch (Exception e) {
      logger.error("Error al subir documento: {}", e.getMessage(), e);
      throw new Exception("No se pudo subir el documento: " + e.getMessage(), e);
    }
  }
}

