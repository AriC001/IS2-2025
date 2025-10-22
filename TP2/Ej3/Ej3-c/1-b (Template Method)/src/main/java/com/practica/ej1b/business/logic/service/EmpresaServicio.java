package com.practica.ej1b.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.ej1b.business.domain.dto.EmpresaDTO;
import com.practica.ej1b.business.domain.entity.Direccion;
import com.practica.ej1b.business.domain.entity.Empresa;
import com.practica.ej1b.business.persistence.repository.EmpresaRepositorio;

/**
 * Servicio para gestionar empresas.
 * Implementa Template Method mediante BaseService.
 * Gestiona conversión de EmpresaDTO a Empresa y gestión de direcciones.
 */
@Service
public class EmpresaServicio extends BaseService<Empresa, String, EmpresaDTO> {

    private final DireccionServicio direccionServicio;

    public EmpresaServicio(EmpresaRepositorio repositorio, DireccionServicio direccionServicio) {
        super(repositorio);
        this.direccionServicio = direccionServicio;
    }

    @Override
    protected String getNombreEntidad() {
        return "Empresa";
    }

    /**
     * Valida los datos de la empresa.
     * Hook method que verifica campos obligatorios.
     */
    @Override
    protected void validar(Empresa entidad) throws Exception {
        if (entidad.getRazonSocial() == null || entidad.getRazonSocial().trim().isEmpty()) {
            throw new Exception("La razón social es obligatoria");
        }
    }

    /**
     * Convierte un EmpresaDTO a una entidad Empresa para crear.
     * Hook method que implementa la conversión DTO -> Entidad.
     * 
     * @param dto el EmpresaDTO con los datos
     * @return la entidad Empresa creada desde el DTO
     * @throws Exception si ocurre un error en la conversión
     */
    @Override
    protected Empresa dtoAEntidad(EmpresaDTO dto) throws Exception {
        if (dto == null) {
            throw new Exception("El DTO de empresa no puede ser nulo");
        }

        Empresa empresa = new Empresa();
        empresa.setId(dto.getId());
        empresa.setRazonSocial(dto.getRazonSocial());

        // Gestionar la dirección si está presente
        if (dto.getDireccionDTO() != null) {
            Direccion direccion = direccionServicio.crear(dto.getDireccionDTO());
            empresa.setDireccion(direccion);
        }

        return empresa;
    }

    /**
     * Mapea los datos de un EmpresaDTO a una entidad Empresa existente para modificar.
     * Hook method que actualiza los campos de la entidad desde el DTO.
     * 
     * @param entidadExistente la entidad Empresa existente en la BD
     * @param dto el EmpresaDTO con los nuevos datos
     * @return la entidad Empresa con los datos actualizados
     * @throws Exception si ocurre un error en el mapeo
     */
    @Override
    protected Empresa mapearDatosDto(Empresa entidadExistente, EmpresaDTO dto) throws Exception {
        if (entidadExistente == null || dto == null) {
            throw new Exception("La entidad o el DTO no pueden ser nulos");
        }

        entidadExistente.setRazonSocial(dto.getRazonSocial());

        // Gestionar la dirección
        if (dto.getDireccionDTO() != null) {
            if (entidadExistente.getDireccion() != null) {
                // Actualizar dirección existente
                Direccion direccionActualizada = direccionServicio.modificar(
                    entidadExistente.getDireccion().getId(), 
                    dto.getDireccionDTO()
                );
                entidadExistente.setDireccion(direccionActualizada);
            } else {
                // Crear nueva dirección
                Direccion nuevaDireccion = direccionServicio.crear(dto.getDireccionDTO());
                entidadExistente.setDireccion(nuevaDireccion);
            }
        }

        return entidadExistente;
    }
}
