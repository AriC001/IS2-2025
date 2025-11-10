package com.practica.nexora.ej6_e.business.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.practica.nexora.ej6_e.business.domain.entity.ContactoCorreoElectronico;

@Repository
public interface ContactoCorreoElectronicoRepository extends BaseRepository<ContactoCorreoElectronico, Long> {

  Optional<ContactoCorreoElectronico> findByEmailAndEliminadoFalse(String email);

}
