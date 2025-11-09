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

  /**
   * Convierte una entidad a DTO
   * @param entity Entidad a convertir
   * @return DTO correspondiente
   */
  D toDTO(E entity);

  /**
   * Convierte un DTO a entidad
   * @param dto DTO a convertir
   * @return Entidad correspondiente
   */
  E toEntity(D dto);

  /**
   * Convierte una colección de entidades a lista de DTOs
   * @param entities Colección de entidades
   * @return Lista de DTOs
   */
  List<D> toDTOList(Collection<E> entities);

  /**
   * Convierte una lista de DTOs a lista de entidades
   * @param dtos Lista de DTOs
   * @return Lista de entidades
   */
  List<E> toEntityList(List<D> dtos);

}
