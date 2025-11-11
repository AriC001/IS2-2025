package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.Imagen;
import nexora.proyectointegrador2.utils.dto.ImagenDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre Imagen (entidad) y ImagenDTO.
 */
@Component
public class ImagenMapper implements BaseMapper<Imagen, ImagenDTO, String> {

  @Override
  public ImagenDTO toDTO(Imagen entity) {
    if (entity == null) {
      return null;
    }

    return ImagenDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .nombre(entity.getNombre())
        .mime(entity.getMime())
        .contenido(entity.getContenido())
        .tipoImagen(entity.getTipoImagen())
        .build();
  }

  @Override
  public Imagen toEntity(ImagenDTO dto) {
    if (dto == null) {
      return null;
    }
    
    Imagen imagen = new Imagen();
    imagen.setId(dto.getId());
    imagen.setEliminado(dto.isEliminado());
    imagen.setNombre(dto.getNombre());
    imagen.setMime(dto.getMime());
    imagen.setContenido(dto.getContenido());
    imagen.setTipoImagen(dto.getTipoImagen());
    return imagen;
  }

  @Override
  public List<ImagenDTO> toDTOList(Collection<Imagen> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<Imagen> toEntityList(List<ImagenDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }
}
