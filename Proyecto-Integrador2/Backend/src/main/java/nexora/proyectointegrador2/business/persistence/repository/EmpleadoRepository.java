package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.Empleado;
import nexora.proyectointegrador2.business.enums.TipoEmpleado;

@Repository
public interface EmpleadoRepository extends BaseRepository<Empleado, String> {

  Optional<Empleado> findByNumeroDocumentoAndEliminadoFalse(String numeroDocumento);

  boolean existsByNumeroDocumentoAndEliminadoFalse(String numeroDocumento);

  Collection<Empleado> findAllByTipoEmpleadoAndEliminadoFalse(TipoEmpleado tipoEmpleado);

}
