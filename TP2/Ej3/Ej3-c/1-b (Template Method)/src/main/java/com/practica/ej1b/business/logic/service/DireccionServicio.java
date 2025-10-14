package com.practica.ej1b.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.ej1b.business.domain.dto.DireccionDTO;
import com.practica.ej1b.business.domain.entity.Direccion;
import com.practica.ej1b.business.domain.entity.Localidad;
import com.practica.ej1b.business.persistence.repository.DireccionRepositorio;
import com.practica.ej1b.business.persistence.repository.LocalidadRepositorio;

/**
 * Servicio para gestionar direcciones.
 * Implementa Template Method mediante BaseService.
 * Gestiona conversión de DireccionDTO a Direccion.
 */
@Service
public class DireccionServicio extends BaseService<Direccion, String, DireccionDTO> {

    private final LocalidadRepositorio localidadRepositorio;

    public DireccionServicio(DireccionRepositorio repositorio, LocalidadRepositorio localidadRepositorio) {
        super(repositorio);
        this.localidadRepositorio = localidadRepositorio;
    }

    @Override
    protected String getNombreEntidad() {
        return "Dirección";
    }

    /**
     * Valida los datos de la dirección.
     * Hook method que verifica campos obligatorios.
     */
    @Override
    protected void validar(Direccion entidad) throws Exception {
        if (entidad.getCalle() == null || entidad.getCalle().trim().isEmpty()) {
            throw new Exception("La calle es obligatoria");
        }
        if (entidad.getLocalidad() == null) {
            throw new Exception("La localidad asociada es obligatoria");
        }
    }

    /**
     * Convierte un DireccionDTO a una entidad Direccion para crear.
     * Hook method que implementa la conversión DTO -> Entidad.
     * 
     * @param dto el DireccionDTO con los datos
     * @return la entidad Direccion creada desde el DTO
     * @throws Exception si ocurre un error en la conversión
     */
    @Override
    protected Direccion dtoAEntidad(DireccionDTO dto) throws Exception {
        if (dto == null) {
            throw new Exception("El DTO de dirección no puede ser nulo");
        }

        Direccion direccion = new Direccion();
        direccion.setId(dto.getId());
        direccion.setCalle(dto.getCalle());
        direccion.setBarrio(dto.getBarrio());
        direccion.setNumeracion(dto.getNumeracion());
        direccion.setManzanaPiso(dto.getManzanaPiso());
        direccion.setCasaDepartamento(dto.getCasaDepartamento());
        direccion.setReferencia(dto.getReferencia());
        direccion.setLatitud(dto.getLatitud());
        direccion.setLongitud(dto.getLongitud());

        // Obtener y asignar la localidad
        Localidad localidad = localidadRepositorio.findById(dto.getLocalidadId())
                .orElseThrow(() -> new Exception("No se encontró la localidad con ID: " + dto.getLocalidadId()));
        direccion.setLocalidad(localidad);

        return direccion;
    }

    /**
     * Mapea los datos de un DireccionDTO a una entidad Direccion existente para modificar.
     * Hook method que actualiza los campos de la entidad desde el DTO.
     * 
     * @param entidadExistente la entidad Direccion existente en la BD
     * @param dto el DireccionDTO con los nuevos datos
     * @return la entidad Direccion con los datos actualizados
     * @throws Exception si ocurre un error en el mapeo
     */
    @Override
    protected Direccion mapearDatosDto(Direccion entidadExistente, DireccionDTO dto) throws Exception {
        if (entidadExistente == null || dto == null) {
            throw new Exception("La entidad o el DTO no pueden ser nulos");
        }

        entidadExistente.setCalle(dto.getCalle());
        entidadExistente.setBarrio(dto.getBarrio());
        entidadExistente.setNumeracion(dto.getNumeracion());
        entidadExistente.setManzanaPiso(dto.getManzanaPiso());
        entidadExistente.setCasaDepartamento(dto.getCasaDepartamento());
        entidadExistente.setReferencia(dto.getReferencia());
        entidadExistente.setLatitud(dto.getLatitud());
        entidadExistente.setLongitud(dto.getLongitud());

        // Actualizar la localidad si cambió
        Localidad localidad = localidadRepositorio.findById(dto.getLocalidadId())
                .orElseThrow(() -> new Exception("No se encontró la localidad con ID: " + dto.getLocalidadId()));
        entidadExistente.setLocalidad(localidad);

        return entidadExistente;
    }
}
