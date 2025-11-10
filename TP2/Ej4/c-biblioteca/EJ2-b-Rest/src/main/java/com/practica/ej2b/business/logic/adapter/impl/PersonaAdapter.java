package com.practica.ej2b.business.logic.adapter.impl;

import org.springframework.stereotype.Component;

import com.practica.ej2b.business.domain.dto.PersonaDTO;
import com.practica.ej2b.business.domain.entity.Domicilio;
import com.practica.ej2b.business.domain.entity.Persona;
import com.practica.ej2b.business.logic.adapter.EntityDTOAdapter;

import lombok.RequiredArgsConstructor;

/**
 * Patrón Adapter para convertir entre PersonaDTO y Persona Entity.
 * Utiliza el patrón Builder para construir los objetos.
 */
@Component
@RequiredArgsConstructor
public class PersonaAdapter implements EntityDTOAdapter<Persona, PersonaDTO> {

    private final DomicilioAdapter domicilioAdapter;

    /**
     * Convierte una entidad Persona a PersonaDTO.
     * 
     * @param persona entidad a convertir
     * @return PersonaDTO correspondiente
     */
    @Override
    public PersonaDTO toDTO(Persona persona) {
        if (persona == null) {
            return null;
        }

        PersonaDTO dto = PersonaDTO.builder()
                .nombre(persona.getNombre())
                .apellido(persona.getApellido())
                .dni(persona.getDni())
                .domicilio(domicilioAdapter.toDTO(persona.getDomicilio()))
                .build();
        
        dto.setId(persona.getId());
        dto.setEliminado(persona.isEliminado());
        
        return dto;
    }

    /**
     * Convierte un PersonaDTO a entidad Persona.
     * Convierte el domicilio usando el adapter.
     * 
     * @param dto DTO a convertir
     * @return Persona correspondiente
     */
    @Override
    public Persona toEntity(PersonaDTO dto) {
        if (dto == null) {
            return null;
        }

        Persona.PersonaBuilder builder = Persona.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .dni(dto.getDni());

        // Convertir domicilio si existe
        if (dto.getDomicilio() != null) {
            Domicilio domicilio = domicilioAdapter.toEntity(dto.getDomicilio());
            builder.domicilio(domicilio);
        }

        Persona persona = builder.build();
        persona.setId(dto.getId());
        persona.setEliminado(dto.isEliminado());
        
        return persona;
    }
}
