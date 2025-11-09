package nexora.proyectointegrador2.utils.mapper.impl;

import org.mapstruct.Mapper;

import nexora.proyectointegrador2.business.domain.entity.Imagen;
import nexora.proyectointegrador2.utils.dto.ImagenDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper para convertir entre Imagen (entidad) y ImagenDTO.
 * MapStruct genera automáticamente la implementación.
 */
@Mapper(componentModel = "spring")
public interface ImagenMapper extends BaseMapper<Imagen, ImagenDTO, String> {

  /**
   * Convierte Imagen a ImagenDTO.
   */
  @Override
  ImagenDTO toDTO(Imagen entity);

  /**
   * Convierte ImagenDTO a Imagen.
   */
  @Override
  Imagen toEntity(ImagenDTO dto);

}

