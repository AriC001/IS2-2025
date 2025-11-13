package nexora.proyectointegrador2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    try {
      AlquilerDTO dto = parseJsonToAlquilerDTO(alquilerJson);
      
      if (archivoDocumento == null || archivoDocumento.isEmpty()) {
        throw new IllegalArgumentException("El archivo del documento es obligatorio");
      }
      
      alquilerService.procesarDocumentoConArchivo(dto, archivoDocumento);
      Alquiler entity = mapper.toEntity(dto);
      Alquiler savedEntity = alquilerService.save(entity);
      AlquilerDTO savedDto = mapper.toDTO(savedEntity);
      
      return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
    } catch (Exception e) {
      logger.error("Error al crear alquiler: {}", e.getMessage(), e);
      throw e;
    }
  }

  /**
   * Actualiza un alquiler con documento adjunto (multipart/form-data).
   */
  @PutMapping(value = "/{id}", consumes = "multipart/form-data")
  public ResponseEntity<AlquilerDTO> updateWithDocument(
      @org.springframework.web.bind.annotation.PathVariable String id,
      @RequestParam("alquiler") String alquilerJson,
      @RequestParam(value = "archivoDocumento", required = false) MultipartFile archivoDocumento) throws Exception {
    try {
      AlquilerDTO dto = parseJsonToAlquilerDTO(alquilerJson);
      
      if (archivoDocumento != null && !archivoDocumento.isEmpty()) {
        alquilerService.procesarDocumentoConArchivo(dto, archivoDocumento);
      }
      
      Alquiler entity = mapper.toEntity(dto);
      Alquiler updatedEntity = alquilerService.update(id, entity);
      AlquilerDTO updatedDto = mapper.toDTO(updatedEntity);
      
      return ResponseEntity.ok(updatedDto);
    } catch (Exception e) {
      logger.error("Error al actualizar alquiler con ID {}: {}", id, e.getMessage(), e);
      throw e;
    }
  }

  private AlquilerDTO parseJsonToAlquilerDTO(String json) throws Exception {
    com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
    objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return objectMapper.readValue(json, AlquilerDTO.class);
  }

  /**
   * Endpoint para filtrar alquileres por usuarioId.
   * 
   * @param usuarioId ID del usuario
   * @return Lista de alquileres del usuario
   * @throws Exception si ocurre un error
   */
  @GetMapping("/filter")
  public ResponseEntity<List<AlquilerDTO>> filterByUsuarioId(
      @RequestParam(value = "usuarioId", required = false) String usuarioId) throws Exception {
    if (usuarioId == null || usuarioId.trim().isEmpty()) {
      // Si no se proporciona usuarioId, retornar todos los alquileres activos
      var entities = service.findAllActives();
      List<AlquilerDTO> dtos = mapper.toDTOList(entities);
      return ResponseEntity.ok(dtos);
    }
    
    var entities = alquilerService.findByUsuarioId(usuarioId);
    List<AlquilerDTO> dtos = mapper.toDTOList(entities);
    return ResponseEntity.ok(dtos);
  }
}

