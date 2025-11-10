package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import nexora.proyectointegrador2.business.domain.entity.BaseEntity;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity<ID>, ID> extends JpaRepository<T, ID> {

  Optional<T> findByIdAndEliminadoFalse(ID id);

  Collection<T> findAllByEliminadoFalse();

}
