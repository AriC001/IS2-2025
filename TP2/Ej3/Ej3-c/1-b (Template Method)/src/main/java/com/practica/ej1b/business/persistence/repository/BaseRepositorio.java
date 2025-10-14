package com.practica.ej1b.business.persistence.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.practica.ej1b.business.domain.entity.BaseEntity;

/**
 * Repositorio base genérico que proporciona operaciones CRUD comunes.
 * Extiende JpaRepository y agrega métodos específicos del dominio.
 * 
 * @param <T> el tipo de entidad que extiende BaseEntity
 * @param <ID> el tipo del identificador
 */
@NoRepositoryBean
public interface BaseRepositorio<T extends BaseEntity<ID>, ID> extends JpaRepository<T, ID> {

    Collection<T> findAllByEliminadoFalse();
    
}
