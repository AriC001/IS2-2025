package nexora.proyectointegrador2.utils.mapper;

import java.util.Collection;
import java.util.List;

import nexora.proyectointegrador2.business.domain.entity.BaseEntity;
import nexora.proyectointegrador2.utils.dto.BaseDTO;

/**
 * Interface base para mappers entre entidades y DTOs.
 * Utiliza MapStruct para la implementación automática.
 * 
 * @param <E> Tipo de la entidad
 * @param <D> Tipo del DTO
 * @param <ID> Tipo del identificador
 */

public interface BaseMapper<E extends BaseEntity<ID>, D extends BaseDTO, ID> {
  D toDTO(E entity);

  E toEntity(D dto);

  List<D> toDTOList(Collection<E> entities);

  List<E> toEntityList(List<D> dtos);
}
