package com.practica.ej1b.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.ej1b.business.domain.dto.DepartamentoDTO;
import com.practica.ej1b.business.domain.entity.Departamento;
import com.practica.ej1b.business.domain.entity.Provincia;
import com.practica.ej1b.business.persistence.repository.DepartamentoRepositorio;
import com.practica.ej1b.business.persistence.repository.ProvinciaRepositorio;

/**
 * Servicio para gestionar departamentos.
 * Implementa Template Method mediante BaseService.
 * Gestiona conversión de DepartamentoDTO a Departamento.
 */
@Service
public class DepartamentoServicio extends BaseService<Departamento, String, DepartamentoDTO> {

    private final ProvinciaRepositorio provinciaRepositorio;

    public DepartamentoServicio(DepartamentoRepositorio repositorio, ProvinciaRepositorio provinciaRepositorio) {
        super(repositorio);
        this.provinciaRepositorio = provinciaRepositorio;
    }

    @Override
    protected String getNombreEntidad() {
        return "Departamento";
    }

    /**
     * Valida los datos del departamento.
     * Hook method que verifica campos obligatorios.
     */
    @Override
    protected void validar(Departamento entidad) throws Exception {
        if (entidad.getNombre() == null || entidad.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre del departamento es obligatorio");
        }
        if (entidad.getProvincia() == null) {
            throw new Exception("La provincia asociada es obligatoria");
        }
    }

    /**
     * Convierte un DepartamentoDTO a una entidad Departamento para crear.
     * Hook method que implementa la conversión DTO -> Entidad.
     * 
     * @param dto el DepartamentoDTO con los datos
     * @return la entidad Departamento creada desde el DTO
     * @throws Exception si ocurre un error en la conversión
     */
    @Override
    protected Departamento dtoAEntidad(DepartamentoDTO dto) throws Exception {
        if (dto == null) {
            throw new Exception("El DTO de departamento no puede ser nulo");
        }

        Departamento departamento = new Departamento();
        departamento.setId(dto.getId());
        departamento.setNombre(dto.getNombre());

        // Obtener y asignar la provincia
        Provincia provincia = provinciaRepositorio.findById(dto.getProvinciaId())
                .orElseThrow(() -> new Exception("No se encontró la provincia con ID: " + dto.getProvinciaId()));
        departamento.setProvincia(provincia);

        return departamento;
    }

    /**
     * Mapea los datos de un DepartamentoDTO a una entidad Departamento existente para modificar.
     * Hook method que actualiza los campos de la entidad desde el DTO.
     * 
     * @param entidadExistente la entidad Departamento existente en la BD
     * @param dto el DepartamentoDTO con los nuevos datos
     * @return la entidad Departamento con los datos actualizados
     * @throws Exception si ocurre un error en el mapeo
     */
    @Override
    protected Departamento mapearDatosDto(Departamento entidadExistente, DepartamentoDTO dto) throws Exception {
        if (entidadExistente == null || dto == null) {
            throw new Exception("La entidad o el DTO no pueden ser nulos");
        }

        entidadExistente.setNombre(dto.getNombre());

        // Actualizar la provincia si cambió
        Provincia provincia = provinciaRepositorio.findById(dto.getProvinciaId())
                .orElseThrow(() -> new Exception("No se encontró la provincia con ID: " + dto.getProvinciaId()));
        entidadExistente.setProvincia(provincia);

        return entidadExistente;
    }
}
