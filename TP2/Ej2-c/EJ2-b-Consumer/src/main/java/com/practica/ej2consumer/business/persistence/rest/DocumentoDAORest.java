package com.practica.ej2consumer.business.persistence.rest;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.practica.ej2consumer.business.domain.dto.DocumentoDTO;

@Repository
public class DocumentoDAORest extends BaseDAORest<DocumentoDTO, Long> {
  
  public DocumentoDAORest(RestTemplate restTemplate) {
    super(restTemplate, "/documentos");
  }

  @Override
  protected Class<DocumentoDTO> getEntityClass() {
    return DocumentoDTO.class;
  }

  public DocumentoDTO uploadDocumento(MultipartFile archivo, String nombreLibro) throws Exception {
    try {
      String url = baseUrl + entityPath + "/upload";
      
      System.out.println("\n🌐 DocumentoDAORest.uploadDocumento() - Llamada HTTP");
      System.out.println("   - URL: " + url);
      System.out.println("   - Método: POST");
      System.out.println("   - Archivo: " + archivo.getOriginalFilename());
      System.out.println("   - Tamaño: " + archivo.getBytes().length + " bytes");
      System.out.println("   - Nombre libro: " + nombreLibro);
      
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);

      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
      body.add("archivo", new ByteArrayResource(archivo.getBytes()) {
        @Override
        public String getFilename() {
          return archivo.getOriginalFilename();
        }
      });
      body.add("nombreLibro", nombreLibro);

      HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
      
      System.out.println("   ⏳ Enviando request al servidor REST...");
      ResponseEntity<DocumentoDTO> response = restTemplate.exchange(
        url,
        HttpMethod.POST,
        requestEntity,
        DocumentoDTO.class
      );
      
      System.out.println("   ✅ Respuesta HTTP: " + response.getStatusCode());
      System.out.println("   📄 Body: " + (response.getBody() != null ? "OK" : "NULL"));
      
      return response.getBody();
    } catch (Exception e) {
      System.err.println("   ❌ Error HTTP: " + e.getClass().getSimpleName() + " - " + e.getMessage());
      throw new Exception("Error al subir el documento: " + e.getMessage(), e);
    }
  }

  public byte[] downloadDocumento(Long id) throws Exception {
    try {
      String url = baseUrl + entityPath + "/download/" + id;
      ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
      return response.getBody();
    } catch (Exception e) {
      throw new Exception("Error al descargar el documento: " + e.getMessage(), e);
    }
  }

  public DocumentoDTO actualizarDocumento(Long id, MultipartFile archivo, String nombreLibro) throws Exception {
    try {
      String url = baseUrl + entityPath + "/actualizar/" + id;
      
      System.out.println("\n🌐 DocumentoDAORest.actualizarDocumento() - Llamada HTTP");
      System.out.println("   - URL: " + url);
      System.out.println("   - Método: POST");
      System.out.println("   - ID: " + id);
      System.out.println("   - Archivo: " + archivo.getOriginalFilename());
      System.out.println("   - Tamaño: " + archivo.getBytes().length + " bytes");
      System.out.println("   - Nombre libro: " + nombreLibro);
      
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);

      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
      body.add("archivo", new ByteArrayResource(archivo.getBytes()) {
        @Override
        public String getFilename() {
          return archivo.getOriginalFilename();
        }
      });
      body.add("nombreLibro", nombreLibro);

      HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
      
      System.out.println("   ⏳ Enviando request al servidor REST...");
      ResponseEntity<DocumentoDTO> response = restTemplate.exchange(
        url,
        HttpMethod.POST,
        requestEntity,
        DocumentoDTO.class
      );
      
      System.out.println("   ✅ Respuesta HTTP: " + response.getStatusCode());
      System.out.println("   📄 Documento actualizado correctamente");
      
      return response.getBody();
    } catch (Exception e) {
      System.err.println("   ❌ Error HTTP: " + e.getClass().getSimpleName() + " - " + e.getMessage());
      throw new Exception("Error al actualizar el documento: " + e.getMessage(), e);
    }
  }

}
