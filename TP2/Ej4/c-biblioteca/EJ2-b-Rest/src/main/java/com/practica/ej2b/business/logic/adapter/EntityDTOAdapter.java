package com.practica.ej2b.business.logic.adapter;

import com.practica.ej2b.business.domain.dto.BaseDTO;
import com.practica.ej2b.business.domain.entity.BaseEntity;

/**
 * Interfaz genérica para adapters que convierten entre Entities y DTOs.
 * Implementa el patrón Adapter para separar la capa de presentación de la capa de dominio.
 * 
 * @param <E> Tipo de la entidad (extends BaseEntity)
 * @param <D> Tipo del DTO (extends BaseDTO)
 */
public interface EntityDTOAdapter<E extends BaseEntity<?>, D extends BaseDTO> {
    
    /**
     * Convierte una entidad a DTO.
     * 
     * @param entity entidad a convertir
     * @return DTO correspondiente
     */
    D toDTO(E entity);
    
    /**
     * Convierte un DTO a entidad.
     * 
     * @param dto DTO a convertir
     * @return entidad correspondiente
     */
    E toEntity(D dto);
}
