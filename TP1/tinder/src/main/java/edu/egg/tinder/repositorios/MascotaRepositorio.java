package edu.egg.tinder.repositorios;

import edu.egg.tinder.entidades.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaRepositorio extends JpaRepository<Mascota, Long> {

    @Query(value = "SELECT * FROM mascota m WHERE m.usuario_id = :usuario_id", nativeQuery = true)
    public List<Mascota> findAllMascotasByUsario(@Param("usuario_id") Long usuario_id);

    @Query(value = "SELECT * FROM mascota m WHERE m.usuario_id = :usuario_id and m.activo = true", nativeQuery = true)
    public List<Mascota> findAllActivePetByUser(@Param("usuario_id") Long usuario_id);

}
