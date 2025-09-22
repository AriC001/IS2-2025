package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DireccionRepositorio extends JpaRepository<Direccion, String> {

  @Query(value = "SELECT * FROM direccion WHERE direccion.eliminado = false", nativeQuery = true)
  List<Direccion> findAllActives();

  @Query(value = "SELECT * FROM direccion WHERE direccion.calle = :calle and direccion.numeracion = :numero and direccion.eliminado = false", nativeQuery = true)
  Direccion findByStreetAndNumber(@Param("calle") String calle,@Param("numero") String numero);

}
