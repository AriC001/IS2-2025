package nexora.proyectointegrador2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import nexora.proyectointegrador2.business.domain.entity.Alquiler;
import nexora.proyectointegrador2.business.logic.service.AlquilerService;
import nexora.proyectointegrador2.utils.dto.AlquilerDTO;
import nexora.proyectointegrador2.utils.mapper.impl.AlquilerMapper;

@RestController
@RequestMapping("api/v1/alquiler")
public class AlquilerRestController extends BaseRestController<Alquiler, AlquilerDTO, String> {
  
  private final AlquilerService alquilerService;
  
  public AlquilerRestController(AlquilerService service, AlquilerMapper mapper) {
    super(service, mapper);
    this.alquilerService = service;
  }

  /**
   * Crea un alquiler con documento adjunto (multipart/form-data).
   * Recibe el DTO como JSON string y el archivo como MultipartFile.
   */
  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<AlquilerDTO> createWithDocument(
      @RequestParam("alquiler") String alquilerJson,
      @RequestParam(value = "archivoDocumento", required = false) MultipartFile archivoDocumento) throws Exception {
    // El DTO viene como JSON string, necesitamos parsearlo
    // Por simplicidad, usaremos ObjectMapper o similar
    AlquilerDTO dto = parseJsonToAlquilerDTO(alquilerJson);
    
    // Procesar el documento con el archivo
    if (archivoDocumento != null && !archivoDocumento.isEmpty()) {
      alquilerService.procesarDocumentoConArchivo(dto, archivoDocumento);
    }
    
    Alquiler entity = mapper.toEntity(dto);
    Alquiler savedEntity = alquilerService.save(entity);
    AlquilerDTO savedDto = mapper.toDTO(savedEntity);
    
    return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
  }

  /**
   * Actualiza un alquiler con documento adjunto (multipart/form-data).
   */
  @PutMapping(value = "/{id}", consumes = "multipart/form-data")
  public ResponseEntity<AlquilerDTO> updateWithDocument(
      @org.springframework.web.bind.annotation.PathVariable String id,
      @RequestParam("alquiler") String alquilerJson,
      @RequestParam(value = "archivoDocumento", required = false) MultipartFile archivoDocumento) throws Exception {
    AlquilerDTO dto = parseJsonToAlquilerDTO(alquilerJson);
    
    // Procesar el documento con el archivo
    if (archivoDocumento != null && !archivoDocumento.isEmpty()) {
      alquilerService.procesarDocumentoConArchivo(dto, archivoDocumento);
    }
    
    Alquiler entity = mapper.toEntity(dto);
    Alquiler updatedEntity = alquilerService.update(id, entity);
    AlquilerDTO updatedDto = mapper.toDTO(updatedEntity);
    
    return ResponseEntity.ok(updatedDto);
  }

  /**
   * Parsea un JSON string a AlquilerDTO.
   * Por simplicidad, usaremos Jackson ObjectMapper.
   */
  private AlquilerDTO parseJsonToAlquilerDTO(String json) throws Exception {
    // Necesitamos importar ObjectMapper de Jackson
    com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
    objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return objectMapper.readValue(json, AlquilerDTO.class);
  }
}

