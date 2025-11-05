package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.ConfiguracionCorreoElectronico;

@Repository
public interface ConfiguracionCorreoElectronicoRepository extends BaseRepository<ConfiguracionCorreoElectronico, String> {

  Optional<ConfiguracionCorreoElectronico> findByEmpresa_IdAndEliminadoFalse(String empresaId);

}
