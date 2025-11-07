package com.practica.nexora.ej6_e.business.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.practica.nexora.ej6_e.business.domain.entity.ContactoTelefonico;

@Repository
public interface ContactoTelefonicoRepository extends BaseRepository<ContactoTelefonico, Long> {

  Optional<ContactoTelefonico> findByNumeroTelefonoAndEliminadoFalse(String numeroTelefono);

}
