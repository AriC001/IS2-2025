package com.practica.ej2b.business.logic.adapter.impl;

import org.springframework.stereotype.Component;

import com.practica.ej2b.business.domain.dto.LocalidadDTO;
import com.practica.ej2b.business.domain.entity.Localidad;
import com.practica.ej2b.business.logic.adapter.EntityDTOAdapter;

/**
 * Patrón Adapter para convertir entre LocalidadDTO y Localidad Entity.
 * Utiliza el patrón Builder para construir los objetos.
 */
@Component
public class LocalidadAdapter implements EntityDTOAdapter<Localidad, LocalidadDTO> {

    /**
     * Convierte una entidad Localidad a LocalidadDTO.
     * 
     * @param localidad entidad a convertir
     * @return LocalidadDTO correspondiente
     */
    @Override
    public LocalidadDTO toDTO(Localidad localidad) {
        if (localidad == null) {
            return null;
        }

        LocalidadDTO dto = LocalidadDTO.builder()
                .nombre(localidad.getNombre())
                .build();
        
        dto.setId(localidad.getId());
        dto.setEliminado(localidad.isEliminado());
        
        return dto;
    }

    /**
     * Convierte un LocalidadDTO a entidad Localidad.
     * 
     * @param dto DTO a convertir
     * @return Localidad correspondiente
     */
    @Override
    public Localidad toEntity(LocalidadDTO dto) {
        if (dto == null) {
            return null;
        }

        Localidad localidad = Localidad.builder()
                .nombre(dto.getNombre())
                .build();
        
        localidad.setId(dto.getId());
        localidad.setEliminado(dto.isEliminado());
        
        return localidad;
    }
}
