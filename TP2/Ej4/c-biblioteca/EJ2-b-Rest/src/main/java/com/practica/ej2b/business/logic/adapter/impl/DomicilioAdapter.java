package com.practica.ej2b.business.logic.adapter.impl;

import org.springframework.stereotype.Component;

import com.practica.ej2b.business.domain.dto.DomicilioDTO;
import com.practica.ej2b.business.domain.entity.Domicilio;
import com.practica.ej2b.business.domain.entity.Localidad;
import com.practica.ej2b.business.logic.adapter.EntityDTOAdapter;

import lombok.RequiredArgsConstructor;

/**
 * Patrón Adapter para convertir entre DomicilioDTO y Domicilio Entity.
 * Utiliza el patrón Builder para construir los objetos.
 */
@Component
@RequiredArgsConstructor
public class DomicilioAdapter implements EntityDTOAdapter<Domicilio, DomicilioDTO> {

    private final LocalidadAdapter localidadAdapter;

    /**
     * Convierte una entidad Domicilio a DomicilioDTO.
     * 
     * @param domicilio entidad a convertir
     * @return DomicilioDTO correspondiente
     */
    @Override
    public DomicilioDTO toDTO(Domicilio domicilio) {
        if (domicilio == null) {
            return null;
        }

        DomicilioDTO dto = DomicilioDTO.builder()
                .calle(domicilio.getCalle())
                .numero(domicilio.getNumero())
                .localidad(localidadAdapter.toDTO(domicilio.getLocalidad()))
                .build();
        
        dto.setId(domicilio.getId());
        dto.setEliminado(domicilio.isEliminado());
        
        return dto;
    }

    /**
     * Convierte un DomicilioDTO a entidad Domicilio.
     * Convierte la localidad usando el adapter.
     * 
     * @param dto DTO a convertir
     * @return Domicilio correspondiente
     */
    @Override
    public Domicilio toEntity(DomicilioDTO dto) {
        if (dto == null) {
            return null;
        }

        Domicilio.DomicilioBuilder builder = Domicilio.builder()
                .calle(dto.getCalle())
                .numero(dto.getNumero());

        // Convertir localidad si existe
        if (dto.getLocalidad() != null) {
            Localidad localidad = localidadAdapter.toEntity(dto.getLocalidad());
            builder.localidad(localidad);
        }

        Domicilio domicilio = builder.build();
        domicilio.setId(dto.getId());
        domicilio.setEliminado(dto.isEliminado());
        
        return domicilio;
    }
}
