package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PromocionRepositorio extends JpaRepository<Promocion, String> {

  @Query(value = "SELECT * FROM promocion WHERE promocion.titulo = :titulo and promocion.eliminado = false", nativeQuery = true)
  Promocion findByTitle(@Param("titulo")String titulo);

  @Query(value = "SELECT * FROM promocion WHERE promocion.eliminado = false", nativeQuery = true)
  Collection<Promocion> findAllActives();

}
