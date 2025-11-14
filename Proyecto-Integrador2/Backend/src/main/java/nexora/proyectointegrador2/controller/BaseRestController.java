package nexora.proyectointegrador2.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import nexora.proyectointegrador2.business.domain.entity.BaseEntity;
import nexora.proyectointegrador2.business.logic.service.BaseService;
import nexora.proyectointegrador2.utils.dto.BaseDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Controlador REST base con operaciones CRUD genéricas.
 * Utiliza el patrón DTO para la comunicación con el cliente.
 * Las excepciones son manejadas globalmente por GlobalExceptionHandler.
 * 
 * @param <E> Tipo de la entidad
 * @param <D> Tipo del DTO
 * @param <ID> Tipo del identificador
 */
public abstract class BaseRestController<E extends BaseEntity<ID>, D extends BaseDTO, ID> {
  
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  protected final BaseService<E, ID> service;
  protected final BaseMapper<E, D, ID> mapper;

  /**
   * Constructor que recibe el service y el mapper.
   * 
   * @param service servicio de la entidad
   * @param mapper mapper para convertir entre Entity y DTO
   */
  protected BaseRestController(BaseService<E, ID> service, BaseMapper<E, D, ID> mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  /**
   * Obtiene todas las entidades activas y las convierte a DTOs.
   * 
   * @return ResponseEntity con lista de DTOs
   * @throws Exception manejada por GlobalExceptionHandler
   */
  @GetMapping
  public ResponseEntity<List<D>> findAll() throws Exception {
    String entityType = service.getClass().getSimpleName().replace("Service", "");
    logger.debug("Obteniendo todos los registros activos de tipo: {}", entityType);
    
    var entities = service.findAllActives();
    logger.debug("Se obtuvieron {} entidades desde el servicio", entities != null ? entities.size() : 0);
    
    List<D> dtos = mapper.toDTOList(entities);
    logger.info("Se obtuvieron {} DTOs de tipo {}", dtos != null ? dtos.size() : 0, entityType);
    
    // Logging específico para direcciones
    if (dtos != null && !dtos.isEmpty() && "Direccion".equals(entityType)) {
      logger.debug("Direcciones retornadas:");
      for (D dto : dtos) {
        if (dto instanceof nexora.proyectointegrador2.utils.dto.DireccionDTO) {
          nexora.proyectointegrador2.utils.dto.DireccionDTO dirDto = (nexora.proyectointegrador2.utils.dto.DireccionDTO) dto;
          logger.debug("  - ID: {}, Calle: {}, Número: {}, Localidad: {}", 
              dirDto.getId(), 
              dirDto.getCalle(), 
              dirDto.getNumero(),
              dirDto.getLocalidad() != null ? dirDto.getLocalidad().getNombre() : "NULL");
        }
      }
    } else if (dtos != null && dtos.isEmpty() && "Direccion".equals(entityType)) {
      logger.warn("No se encontraron direcciones activas en la base de datos");
    }
    
    return ResponseEntity.ok(dtos);
  }

  /**
   * Obtiene una entidad por ID y la convierte a DTO.
   * 
   * @param id identificador de la entidad
   * @return ResponseEntity con el DTO
   * @throws Exception manejada por GlobalExceptionHandler
   */
  @GetMapping("/{id}")
  public ResponseEntity<D> findById(@PathVariable ID id) {
    logger.debug("Buscando registro con ID: {}", id);
    try {
      E entity = service.findById(id);
      D dto = mapper.toDTO(entity);
      return ResponseEntity.ok(dto);
    } catch (Exception e) {
      // Service throws generic Exception when entity not found in this project.
      // Convert to 404 so clients that request an invalid id (or a path segment
      // accidentally sent to this endpoint) receive a friendly response instead
      // of an internal server error with a full stack trace.
      logger.warn("Entidad no encontrada o error al buscar ID {}: {}", id, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Crea una nueva entidad a partir de un DTO.
   * 
   * @param dto DTO recibido del cliente (validado con @Valid)
   * @return ResponseEntity con el DTO de la entidad creada
   * @throws Exception manejada por GlobalExceptionHandler
   */
  @PostMapping
  public ResponseEntity<D> create(@Valid @RequestBody D dto) throws Exception {
    logger.debug("Creando nuevo registro: {}", dto);
    E entity = mapper.toEntity(dto);
    E savedEntity = service.save(entity);
    D savedDto = mapper.toDTO(savedEntity);
    logger.info("Registro creado exitosamente con ID: {}", savedEntity.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
  }

  /**
   * Actualiza una entidad existente a partir de un DTO.
   * 
   * @param id identificador de la entidad a actualizar
   * @param dto DTO con los nuevos datos (validado con @Valid)
   * @return ResponseEntity con el DTO de la entidad actualizada
   * @throws Exception manejada por GlobalExceptionHandler
   */
  @PutMapping("/{id}")
  public ResponseEntity<D> update(@PathVariable ID id, @Valid @RequestBody D dto) throws Exception {
    logger.debug("Actualizando registro con ID: {}", id);
    E entity = mapper.toEntity(dto);
    E updatedEntity = service.update(id, entity);
    D updatedDto = mapper.toDTO(updatedEntity);
    logger.info("Registro actualizado exitosamente con ID: {}", id);
    return ResponseEntity.ok(updatedDto);
  }

  /**
   * Elimina lógicamente una entidad por ID.
   * 
   * @param id identificador de la entidad a eliminar
   * @return ResponseEntity vacío
   * @throws Exception manejada por GlobalExceptionHandler
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable ID id) throws Exception {
    logger.debug("Eliminando registro con ID: {}", id);
    service.logicDelete(id);
    logger.info("Registro eliminado exitosamente con ID: {}", id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * Verificar si existe un registro por ID.
   * 
   * @param id identificador de la entidad
   * @return ResponseEntity con boolean
   * @throws Exception manejada por GlobalExceptionHandler
   */
  @GetMapping("/{id}/exists")
  public ResponseEntity<Boolean> exists(@PathVariable ID id) throws Exception {
    logger.debug("Verificando existencia del registro con ID: {}", id);
    boolean exists = service.existsById(id);
    return ResponseEntity.ok(exists);
  }

  /**
   * Contar registros activos.
   * 
   * @return ResponseEntity con el conteo
   * @throws Exception manejada por GlobalExceptionHandler
   */
  @GetMapping("/count")
  public ResponseEntity<Long> count() throws Exception {
    logger.debug("Contando registros activos");
    long count = service.count();
    logger.debug("Total de registros activos: {}", count);
    return ResponseEntity.ok(count);
  }

}
