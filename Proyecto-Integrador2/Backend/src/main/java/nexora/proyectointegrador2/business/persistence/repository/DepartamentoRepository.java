package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.Departamento;

@Repository
public interface DepartamentoRepository extends BaseRepository<Departamento, String> {

  Optional<Departamento> findByNombreAndEliminadoFalse(String nombre);

  Collection<Departamento> findAllByProvincia_IdAndEliminadoFalse(String provinciaId);

}
