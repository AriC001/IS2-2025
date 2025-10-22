package com.practica.ej1b.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.ej1b.business.domain.dto.ProvinciaDTO;
import com.practica.ej1b.business.domain.entity.Pais;
import com.practica.ej1b.business.domain.entity.Provincia;
import com.practica.ej1b.business.persistence.repository.PaisRepositorio;
import com.practica.ej1b.business.persistence.repository.ProvinciaRepositorio;

@Service
public class ProvinciaService extends BaseService<Provincia, String, ProvinciaDTO> {

    private final PaisRepositorio paisRepositorio;

    public ProvinciaService(ProvinciaRepositorio repositorio, PaisRepositorio paisRepositorio) {
        super(repositorio);
        this.paisRepositorio = paisRepositorio;
    }

    @Override
    protected String getNombreEntidad() {
        return "Provincia";
    }

    @Override
    protected void validar(Provincia entidad) throws Exception {
        if (entidad.getNombre() == null || entidad.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre de la provincia es obligatorio");
        }
        if (entidad.getPais() == null) {
            throw new Exception("El país asociado es obligatorio");
        }
    }

    @Override
    protected Provincia dtoAEntidad(ProvinciaDTO dto) throws Exception {
        if (dto == null) {
            throw new Exception("El DTO de provincia no puede ser nulo");
        }
        Provincia provincia = new Provincia();
        provincia.setId(dto.getId());
        provincia.setNombre(dto.getNombre());
        Pais pais = paisRepositorio.findById(dto.getPaisId())
                .orElseThrow(() -> new Exception("No se encontró el país con ID: " + dto.getPaisId()));
        provincia.setPais(pais);
        return provincia;
    }

    @Override
    protected Provincia mapearDatosDto(Provincia entidadExistente, ProvinciaDTO dto) throws Exception {
        if (entidadExistente == null || dto == null) {
            throw new Exception("La entidad o el DTO no pueden ser nulos");
        }
        entidadExistente.setNombre(dto.getNombre());
        entidadExistente.setPais(paisRepositorio.findById(dto.getPaisId())
                .orElseThrow(() -> new Exception("No se encontró el país con ID: " + dto.getPaisId())));
        return entidadExistente;
    }

    
}
