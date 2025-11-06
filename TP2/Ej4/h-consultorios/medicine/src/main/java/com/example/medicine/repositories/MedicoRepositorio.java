package com.example.medicine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.medicine.model.Medico;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepositorio extends JpaRepository<Medico, String> {

  @Query(value = "SELECT * FROM medico WHERE medico.eliminado = false", nativeQuery = true)
  List<Medico> findAllActives();

  @Query(value = "SELECT * FROM medico WHERE medico.id =:id AND medico.eliminado = false", nativeQuery = true)
  Optional<Medico> findById(@Param("id") String id);

  @Query(value = "SELECT * FROM medico WHERE medico.documento =:documento AND medico.eliminado = false", nativeQuery = true)
  Optional<Medico> findByDocumento(@Param("documento") String documento);

}