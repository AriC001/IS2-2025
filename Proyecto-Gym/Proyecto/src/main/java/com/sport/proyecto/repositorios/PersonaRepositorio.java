package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Pais;
import com.sport.proyecto.entidades.Persona;
import com.sport.proyecto.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaRepositorio extends JpaRepository<Persona,String> {

    @Query(value = "SELECT * FROM persona p WHERE p.email = :email AND p.clave = :clave AND p.eliminado = false", nativeQuery = true)
    Optional<Persona> findByEmailyClave(@Param("email") String email, @Param("clave") String clave);

    @Query("SELECT p FROM Persona p WHERE p.usuario = :usuario AND p.eliminado = false")
    Persona findByUser(@Param("usuario") Usuario usuario);

    @Query("SELECT p FROM Persona p WHERE p.usuario.rol = 'Socio' AND FUNCTION('MONTH', p.fechaNacimiento) = :mes AND FUNCTION('DAY', p.fechaNacimiento) = :dia AND p.eliminado = false")
    List<Persona> findByDiaYMes(@Param("dia") int dia, @Param("mes") int mes);

}

