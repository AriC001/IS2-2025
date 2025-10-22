package com.practica.ej1b.business.logic.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.practica.ej1b.business.domain.entity.BaseEntity;
import com.practica.ej1b.business.persistence.repository.BaseRepositorio;

import jakarta.transaction.Transactional;

/**
 * Servicio base abstracto que implementa el patrón Template Method.
 * Proporciona operaciones CRUD comunes y permite que las subclases personalicen
 * el comportamiento específico mediante hooks.
 * 
 * @param <T> el tipo de entidad
 * @param <ID> el tipo del identificador
 * @param <D> el tipo del DTO
 */
public abstract class BaseService<T extends BaseEntity<ID>, ID, D> {

    protected BaseRepositorio<T, ID> repositorio;

    public BaseService(BaseRepositorio<T, ID> repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Obtiene el nombre de la entidad para mensajes de error.
     * Hook method que debe ser implementado por las subclases.
     * 
     * @return el nombre de la entidad
     */
    protected abstract String getNombreEntidad();

    /**
     * Valida una entidad antes de crear o modificar.
     * Hook method que puede ser sobrescrito por las subclases.
     * 
     * @param entidad la entidad a validar
     * @throws Exception si la validación falla
     */
    protected abstract void validar(T entidad) throws Exception;

    /**
     * Convierte un DTO a una entidad para crear.
     * Hook method que debe ser implementado por las subclases.
     * 
     * @param dto el DTO a convertir
     * @return la entidad creada desde el DTO
     * @throws Exception si ocurre un error en la conversión
     */
    protected abstract T dtoAEntidad(D dto) throws Exception;

    /**
     * Mapea los datos de un DTO a una entidad existente para modificar.
     * Hook method que debe ser implementado por las subclases.
     * 
     * @param entidadExistente la entidad existente en la BD
     * @param dto el DTO con los nuevos datos
     * @return la entidad con los datos actualizados
     * @throws Exception si ocurre un error en el mapeo
     */
    protected abstract T mapearDatosDto(T entidadExistente, D dto) throws Exception;

    // ========== TEMPLATE METHODS ==========

    /**
     * Lista todas las entidades.
     * Template Method que define el flujo de listado.
     * 
     * @return lista de todas las entidades
     * @throws Exception si ocurre un error
     */
    @Transactional
    public List<T> listar() throws Exception {
        try {
            return repositorio.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al listar " + getNombreEntidad());
        }
    }

    /**
     * Lista todas las entidades activas (no eliminadas).
     * Template Method que define el flujo de listado de activos.
     * 
     * @return colección de entidades activas
     * @throws Exception si ocurre un error
     */
    @Transactional
    public Collection<T> listarActivos() throws Exception {
        try {
            return repositorio.findAllByEliminadoFalse();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al listar " + getNombreEntidad() + " activos");
        }
    }

    /**
     * Busca una entidad por su identificador.
     * Template Method que define el flujo de búsqueda.
     * 
     * @param id el identificador de la entidad
     * @return la entidad encontrada
     * @throws Exception si no se encuentra la entidad
     */
    @Transactional
    public T buscar(ID id) throws Exception {
        Optional<T> respuesta = repositorio.findById(id);
        if (respuesta.isEmpty()) {
            throw new Exception("No se encontró " + getNombreEntidad() + " solicitado");
        }
        return respuesta.get();
    }

    /**
     * Crea una nueva entidad desde un DTO.
     * Template Method que define el flujo de creación.
     * 
     * @param dto el DTO con los datos para crear la entidad
     * @return la entidad creada
     * @throws Exception si ocurre un error
     */
    @Transactional
    public T crear(D dto) throws Exception {
        try {
            // Convertir DTO a entidad
            T entidad = dtoAEntidad(dto);
            
            // Validar la entidad
            validar(entidad);
            
            // Establecer como no eliminado y guardar
            entidad.setEliminado(false);
            T entidadGuardada = repositorio.save(entidad);
            
            return entidadGuardada;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al crear " + getNombreEntidad() + ": " + e.getMessage());
        }
    }

    /**
     * Modifica una entidad existente desde un DTO.
     * Template Method que define el flujo de modificación.
     * 
     * @param id el identificador de la entidad
     * @param dto el DTO con los nuevos datos
     * @return la entidad modificada
     * @throws Exception si ocurre un error
     */
    @Transactional
    public T modificar(ID id, D dto) throws Exception {
        try {
            // Buscar la entidad existente
            T entidadExistente = buscar(id);
            
            // Mapear los datos del DTO a la entidad existente
            T entidadModificada = mapearDatosDto(entidadExistente, dto);
            
            // Validar la entidad modificada
            validar(entidadModificada);
            
            // Guardar y retornar
            T entidadGuardada = repositorio.save(entidadModificada);
            return entidadGuardada;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al modificar " + getNombreEntidad() + ": " + e.getMessage());
        }
    }

    /**
     * Elimina lógicamente una entidad (soft delete).
     * Template Method que define el flujo de eliminación.
     * 
     * @param id el identificador de la entidad
     * @throws Exception si ocurre un error
     */
    @Transactional
    public void eliminar(ID id) throws Exception {
        try {
            if (id == null) {
                throw new Exception("El id no puede ser nulo");
            }
            T entidad = buscar(id);
            entidad.setEliminado(true);
            repositorio.save(entidad);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al eliminar " + getNombreEntidad() + ": " + e.getMessage());
        }
    }
}
