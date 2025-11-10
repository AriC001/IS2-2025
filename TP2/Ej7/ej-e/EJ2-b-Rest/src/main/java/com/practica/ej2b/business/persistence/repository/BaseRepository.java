package com.practica.ej2b.business.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.practica.ej2b.business.domain.entity.BaseEntity;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity<ID>, ID> extends JpaRepository<T, ID> {

  Optional<T> findByIdAndEliminadoFalse(ID id);

  List<T> findAllByEliminadoFalse();

}
