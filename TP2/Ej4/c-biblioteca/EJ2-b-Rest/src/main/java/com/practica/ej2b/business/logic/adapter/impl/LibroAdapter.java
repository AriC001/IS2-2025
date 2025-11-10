package com.practica.ej2b.business.logic.adapter.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.practica.ej2b.business.domain.dto.AutorDTO;
import com.practica.ej2b.business.domain.dto.LibroDTO;
import com.practica.ej2b.business.domain.entity.Autor;
import com.practica.ej2b.business.domain.entity.Documento;
import com.practica.ej2b.business.domain.entity.Libro;
import com.practica.ej2b.business.domain.entity.Persona;
import com.practica.ej2b.business.logic.adapter.EntityDTOAdapter;
import com.practica.ej2b.business.logic.service.AutorService;
import com.practica.ej2b.business.logic.service.DocumentoService;
import com.practica.ej2b.business.logic.service.PersonaService;

import lombok.RequiredArgsConstructor;

/**
 * Patrón Adapter para convertir entre LibroDTO y Libro Entity.
 * Utiliza el patrón Builder para construir los objetos.
 */
@Component
@RequiredArgsConstructor
public class LibroAdapter implements EntityDTOAdapter<Libro, LibroDTO> {

    private final AutorAdapter autorAdapter;
    private final PersonaAdapter personaAdapter;
    private final DocumentoAdapter documentoAdapter;
    
    // Servicios para cargar entidades persistidas
    private final AutorService autorService;
    private final PersonaService personaService;
    private final DocumentoService documentoService;

    /**
     * Convierte una entidad Libro a LibroDTO.
     * 
     * @param libro entidad a convertir
     * @return LibroDTO correspondiente
     */
    @Override
    public LibroDTO toDTO(Libro libro) {
        if (libro == null) {
            return null;
        }

        // Convertir autores a DTOs
        Set<AutorDTO> autoresDTO = libro.getAutores() != null ? 
            libro.getAutores().stream()
                .map(autorAdapter::toDTO)
                .collect(Collectors.toSet()) : new HashSet<>();

        LibroDTO dto = LibroDTO.builder()
                .titulo(libro.getTitulo())
                .genero(libro.getGenero())
                .paginas(libro.getPaginas())
                .fecha(libro.getFecha())
                .autores(autoresDTO)
                .persona(personaAdapter.toDTO(libro.getPersona()))
                .documento(documentoAdapter.toDTO(libro.getDocumento()))
                .build();
        
        dto.setId(libro.getId());
        dto.setEliminado(libro.isEliminado());
        
        return dto;
    }

    /**
     * Convierte un LibroDTO a entidad Libro.
     * Carga las entidades relacionadas desde la BD por ID para evitar problemas con JPA.
     * 
     * @param dto DTO a convertir
     * @return Libro correspondiente
     */
    @Override
    public Libro toEntity(LibroDTO dto) {
        if (dto == null) {
            return null;
        }

        try {
            // Cargar autores persistidos desde la BD por ID
            Set<Autor> autores = new HashSet<>();
            if (dto.getAutores() != null) {
                for (AutorDTO autorDTO : dto.getAutores()) {
                    if (autorDTO.getId() != null) {
                        // Cargar el autor completo desde la BD
                        Autor autorPersistido = autorService.findById(autorDTO.getId());
                        autores.add(autorPersistido);
                    }
                }
            }

            Libro.LibroBuilder builder = Libro.builder()
                    .titulo(dto.getTitulo())
                    .genero(dto.getGenero())
                    .paginas(dto.getPaginas())
                    .fecha(dto.getFecha())
                    .autores(autores);

            // Cargar persona persistida si existe
            if (dto.getPersona() != null && dto.getPersona().getId() != null) {
                Persona personaPersistida = personaService.findById(dto.getPersona().getId());
                builder.persona(personaPersistida);
            }

            // Cargar documento persistido si existe
            if (dto.getDocumento() != null && dto.getDocumento().getId() != null) {
                Documento documentoPersistido = documentoService.findById(dto.getDocumento().getId());
                builder.documento(documentoPersistido);
            }

            Libro libro = builder.build();
            libro.setId(dto.getId());
            libro.setEliminado(dto.isEliminado());
            
            return libro;
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir LibroDTO a Libro: " + e.getMessage(), e);
        }
    }
}
