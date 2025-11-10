package com.practica.ej2b.controller.rest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.practica.ej2b.business.domain.dto.BaseDTO;
import com.practica.ej2b.business.domain.entity.BaseEntity;
import com.practica.ej2b.business.logic.adapter.EntityDTOAdapter;
import com.practica.ej2b.business.logic.service.BaseService;

/**
 * Controlador REST base que utiliza el patrón Adapter.
 * Trabaja con DTOs en la capa de presentación y Entities en la capa de negocio.
 * 
 * @param <E> Tipo de la entidad (extends BaseEntity)
 * @param <D> Tipo del DTO (extends BaseDTO)
 * @param <ID> Tipo del identificador
 */
public abstract class BaseRestController<E extends BaseEntity<ID>, D extends BaseDTO, ID> {
  
  protected final BaseService<E, ID> service;
  protected final EntityDTOAdapter<E, D> adapter;

  /**
   * Constructor que recibe el service y el adapter.
   * 
   * @param service servicio de la entidad
   * @param adapter adapter para convertir entre Entity y DTO
   */
  public BaseRestController(BaseService<E, ID> service, EntityDTOAdapter<E, D> adapter) {
    this.service = service;
    this.adapter = adapter;
  }

  /**
   * Obtiene todas las entidades activas y las convierte a DTOs.
   * 
   * @return ResponseEntity con lista de DTOs
   */
  @GetMapping
  public ResponseEntity<List<D>> getAllActives() {
    try {
      Iterable<E> entities = service.findAllActives();
      
      // Convertir entities a DTOs usando el adapter
      List<D> dtos = StreamSupport.stream(entities.spliterator(), false)
          .map(adapter::toDTO)
          .collect(Collectors.toList());
      
      return ResponseEntity.ok(dtos);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Obtiene una entidad por ID y la convierte a DTO.
   * 
   * @param id identificador de la entidad
   * @return ResponseEntity con el DTO
   */
  @GetMapping("/{id}")
  public ResponseEntity<D> getById(@PathVariable ID id) {
    try {
      E entity = service.findById(id);
      
      // Convertir entity a DTO usando el adapter
      D dto = adapter.toDTO(entity);
      
      return ResponseEntity.ok(dto);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Crea una nueva entidad a partir de un DTO.
   * 
   * @param dto DTO recibido del cliente
   * @return ResponseEntity con el DTO de la entidad creada
   */
  @PostMapping
  public ResponseEntity<D> create(@RequestBody D dto) {
    try {
      // Convertir DTO a entity usando el adapter
      E entity = adapter.toEntity(dto);
      
      // Guardar la entity
      E savedEntity = service.save(entity);
      
      // Convertir entity guardada a DTO para responder
      D savedDto = adapter.toDTO(savedEntity);
      
      return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Actualiza una entidad existente a partir de un DTO.
   * 
   * @param id identificador de la entidad a actualizar
   * @param dto DTO con los nuevos datos
   * @return ResponseEntity con el DTO de la entidad actualizada
   */
  @PutMapping("/{id}")
  public ResponseEntity<D> update(@PathVariable ID id, @RequestBody D dto) {
    try {
      // Convertir DTO a entity usando el adapter
      E entity = adapter.toEntity(dto);
      
      // Actualizar la entity
      E updatedEntity = service.update(entity, id);
      
      // Convertir entity actualizada a DTO para responder
      D updatedDto = adapter.toDTO(updatedEntity);
      
      return ResponseEntity.ok(updatedDto);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Elimina lógicamente una entidad por ID.
   * 
   * @param id identificador de la entidad a eliminar
   * @return ResponseEntity vacío
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable ID id) {
    try {
      service.logicDelete(id);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
