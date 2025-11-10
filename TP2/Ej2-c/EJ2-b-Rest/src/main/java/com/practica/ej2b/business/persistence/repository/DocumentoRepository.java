package com.practica.ej2b.business.persistence.repository;

import org.springframework.stereotype.Repository;

import com.practica.ej2b.business.domain.entity.Documento;

@Repository
public interface DocumentoRepository extends BaseRepository<Documento, Long> {

}
