package com.practica.ej1b.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.ej1b.business.domain.dto.LocalidadDTO;
import com.practica.ej1b.business.domain.entity.Departamento;
import com.practica.ej1b.business.domain.entity.Localidad;
import com.practica.ej1b.business.persistence.repository.DepartamentoRepositorio;
import com.practica.ej1b.business.persistence.repository.LocalidadRepositorio;

/**
 * Servicio para gestionar localidades.
 * Implementa Template Method mediante BaseService.
 * Gestiona conversión de LocalidadDTO a Localidad.
 */
@Service
public class LocalidadServicio extends BaseService<Localidad, String, LocalidadDTO> {

    private final DepartamentoRepositorio departamentoRepositorio;

    public LocalidadServicio(LocalidadRepositorio repositorio, DepartamentoRepositorio departamentoRepositorio) {
        super(repositorio);
        this.departamentoRepositorio = departamentoRepositorio;
    }

    @Override
    protected String getNombreEntidad() {
        return "Localidad";
    }

    /**
     * Valida los datos de la localidad.
     * Hook method que verifica campos obligatorios.
     */
    @Override
    protected void validar(Localidad entidad) throws Exception {
        if (entidad.getNombre() == null || entidad.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre de la localidad es obligatorio");
        }
        if (entidad.getDepartamento() == null) {
            throw new Exception("El departamento asociado es obligatorio");
        }
    }

    /**
     * Convierte un LocalidadDTO a una entidad Localidad para crear.
     * Hook method que implementa la conversión DTO -> Entidad.
     * 
     * @param dto el LocalidadDTO con los datos
     * @return la entidad Localidad creada desde el DTO
     * @throws Exception si ocurre un error en la conversión
     */
    @Override
    protected Localidad dtoAEntidad(LocalidadDTO dto) throws Exception {
        if (dto == null) {
            throw new Exception("El DTO de localidad no puede ser nulo");
        }

        Localidad localidad = new Localidad();
        localidad.setId(dto.getId());
        localidad.setNombre(dto.getNombre());
        localidad.setCodigoPostal(dto.getCodigoPostal());

        // Obtener y asignar el departamento
        Departamento departamento = departamentoRepositorio.findById(dto.getDepartamentoId())
                .orElseThrow(() -> new Exception("No se encontró el departamento con ID: " + dto.getDepartamentoId()));
        localidad.setDepartamento(departamento);

        return localidad;
    }

    /**
     * Mapea los datos de un LocalidadDTO a una entidad Localidad existente para modificar.
     * Hook method que actualiza los campos de la entidad desde el DTO.
     * 
     * @param entidadExistente la entidad Localidad existente en la BD
     * @param dto el LocalidadDTO con los nuevos datos
     * @return la entidad Localidad con los datos actualizados
     * @throws Exception si ocurre un error en el mapeo
     */
    @Override
    protected Localidad mapearDatosDto(Localidad entidadExistente, LocalidadDTO dto) throws Exception {
        if (entidadExistente == null || dto == null) {
            throw new Exception("La entidad o el DTO no pueden ser nulos");
        }

        entidadExistente.setNombre(dto.getNombre());
        entidadExistente.setCodigoPostal(dto.getCodigoPostal());

        // Actualizar el departamento si cambió
        Departamento departamento = departamentoRepositorio.findById(dto.getDepartamentoId())
                .orElseThrow(() -> new Exception("No se encontró el departamento con ID: " + dto.getDepartamentoId()));
        entidadExistente.setDepartamento(departamento);

        return entidadExistente;
    }
}
