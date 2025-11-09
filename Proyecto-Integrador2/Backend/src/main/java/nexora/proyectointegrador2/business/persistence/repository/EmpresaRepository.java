package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.Empresa;

@Repository
public interface EmpresaRepository extends BaseRepository<Empresa, String> {

  Optional<Empresa> findByEmailAndEliminadoFalse(String email);

  boolean existsByEmailAndEliminadoFalse(String email);

}
