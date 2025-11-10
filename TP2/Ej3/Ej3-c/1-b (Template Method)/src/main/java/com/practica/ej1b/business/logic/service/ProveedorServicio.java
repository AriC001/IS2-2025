package com.practica.ej1b.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.ej1b.business.domain.dto.ProveedorDTO;
import com.practica.ej1b.business.domain.entity.Direccion;
import com.practica.ej1b.business.domain.entity.Proveedor;
import com.practica.ej1b.business.persistence.repository.ProveedorRepositorio;

/**
 * Servicio para gestionar proveedores.
 * Implementa Template Method mediante BaseService.
 * Gestiona conversión de ProveedorDTO a Proveedor (que hereda de Persona).
 */
@Service
public class ProveedorServicio extends BaseService<Proveedor, String, ProveedorDTO> {

    private final DireccionServicio direccionServicio;

    public ProveedorServicio(ProveedorRepositorio repositorio, DireccionServicio direccionServicio) {
        super(repositorio);
        this.direccionServicio = direccionServicio;
    }

    @Override
    protected String getNombreEntidad() {
        return "Proveedor";
    }

    /**
     * Valida los datos del proveedor.
     * Hook method que verifica campos obligatorios.
     */
    @Override
    protected void validar(Proveedor entidad) throws Exception {
        if (entidad.getNombre() == null || entidad.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre del proveedor es obligatorio");
        }
        if (entidad.getApellido() == null || entidad.getApellido().trim().isEmpty()) {
            throw new Exception("El apellido del proveedor es obligatorio");
        }
        if (entidad.getCuit() == null || entidad.getCuit().trim().isEmpty()) {
            throw new Exception("El CUIT del proveedor es obligatorio");
        }
        if (entidad.getCorreoElectronico() == null || entidad.getCorreoElectronico().trim().isEmpty()) {
            throw new Exception("El correo electrónico del proveedor es obligatorio");
        }
    }

    /**
     * Convierte un ProveedorDTO a una entidad Proveedor para crear.
     * Hook method que implementa la conversión DTO -> Entidad.
     * 
     * @param dto el ProveedorDTO con los datos
     * @return la entidad Proveedor creada desde el DTO
     * @throws Exception si ocurre un error en la conversión
     */
    @Override
    protected Proveedor dtoAEntidad(ProveedorDTO dto) throws Exception {
        if (dto == null) {
            throw new Exception("El DTO de proveedor no puede ser nulo");
        }

        Proveedor proveedor = new Proveedor();
        
        // Mapear campos heredados de Persona (de PersonaDTO)
        proveedor.setId(dto.getId());
        proveedor.setNombre(dto.getNombre());
        proveedor.setApellido(dto.getApellido());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setCorreoElectronico(dto.getCorreoElectronico());
        
        // Mapear campos específicos de Proveedor
        proveedor.setCuit(dto.getCuit());

        // Gestionar la dirección si está presente
        if (dto.getDireccionDTO() != null) {
            Direccion direccion = direccionServicio.crear(dto.getDireccionDTO());
            proveedor.setDireccion(direccion);
        }

        return proveedor;
    }

    /**
     * Mapea los datos de un ProveedorDTO a una entidad Proveedor existente para modificar.
     * Hook method que actualiza los campos de la entidad desde el DTO.
     * 
     * @param entidadExistente la entidad Proveedor existente en la BD
     * @param dto el ProveedorDTO con los nuevos datos
     * @return la entidad Proveedor con los datos actualizados
     * @throws Exception si ocurre un error en el mapeo
     */
    @Override
    protected Proveedor mapearDatosDto(Proveedor entidadExistente, ProveedorDTO dto) throws Exception {
        if (entidadExistente == null || dto == null) {
            throw new Exception("La entidad o el DTO no pueden ser nulos");
        }

        // Mapear campos heredados de Persona (de PersonaDTO)
        entidadExistente.setNombre(dto.getNombre());
        entidadExistente.setApellido(dto.getApellido());
        entidadExistente.setTelefono(dto.getTelefono());
        entidadExistente.setCorreoElectronico(dto.getCorreoElectronico());
        
        // Mapear campos específicos de Proveedor
        entidadExistente.setCuit(dto.getCuit());

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
