package edu.egg.tinder.repositorios;

import edu.egg.tinder.entidades.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaRepositorio extends JpaRepository<Mascota, Long> {

    @Query(value = "SELECT * FROM mascota m WHERE m.usuario_id = :usuario_id and m.activo = 1", nativeQuery = true)
    public List<Mascota> findAllMascotasByUsario(@Param("usuario_id") Long usuario_id);

    @Query(value = "SELECT * FROM mascota m WHERE m.usuario_id = :usuario_id and m.activo = 1", nativeQuery = true)
    public List<Mascota> findAllActivePetByUser(@Param("usuario_id") Long usuario_id);

    //@Query(value = "SELECT * FROM mascota m WHERE m.id = :id", nativeQuery = true)
    //public Mascota findById(@Param("id") Long id);

    @Query(value = "SELECT * FROM mascota m WHERE m.usuario_id = :usuario_id and m.activo = 0", nativeQuery = true)
    public List<Mascota> findAllMascotasInactivasByUsuario(@Param("usuario_id") Long usuario_id);

    //Mascota findByID(Long id);

    Mascota findByid(Long id);
    @Query(value="SELECT * FROM mascota m WHERE m.activo=1", nativeQuery = true)
    public List<Mascota> findAllMascotasActivas();

}
