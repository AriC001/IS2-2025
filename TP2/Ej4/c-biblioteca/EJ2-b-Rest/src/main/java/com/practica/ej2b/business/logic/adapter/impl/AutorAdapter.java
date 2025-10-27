package com.practica.ej2b.business.logic.adapter.impl;

import org.springframework.stereotype.Component;

import com.practica.ej2b.business.domain.dto.AutorDTO;
import com.practica.ej2b.business.domain.entity.Autor;
import com.practica.ej2b.business.logic.adapter.EntityDTOAdapter;

/**
 * Patrón Adapter para convertir entre AutorDTO y Autor Entity.
 * Utiliza el patrón Builder para construir los objetos.
 */
@Component
public class AutorAdapter implements EntityDTOAdapter<Autor, AutorDTO> {

    /**
     * Convierte una entidad Autor a AutorDTO.
     * 
     * @param autor entidad a convertir
     * @return AutorDTO correspondiente
     */
    @Override
    public AutorDTO toDTO(Autor autor) {
        if (autor == null) {
            return null;
        }

        AutorDTO dto = AutorDTO.builder()
                .nombre(autor.getNombre())
                .apellido(autor.getApellido())
                .biografia(autor.getBiografia())
                .build();
        
        dto.setId(autor.getId());
        dto.setEliminado(autor.isEliminado());
        
        return dto;
    }

    /**
     * Convierte un AutorDTO a entidad Autor.
     * 
     * @param dto DTO a convertir
     * @return Autor correspondiente
     */
    @Override
    public Autor toEntity(AutorDTO dto) {
        if (dto == null) {
            return null;
        }

        Autor autor = Autor.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .biografia(dto.getBiografia())
                .build();
        
        autor.setId(dto.getId());
        autor.setEliminado(dto.isEliminado());
        
        return autor;
    }
}
