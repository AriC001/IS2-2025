package nexora.proyectointegrador2.utils.mapper.impl;

import org.mapstruct.Mapper;

import nexora.proyectointegrador2.business.domain.entity.Alquiler;
import nexora.proyectointegrador2.utils.dto.AlquilerDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper para convertir entre Alquiler (entidad) y AlquilerDTO.
 * MapStruct genera automáticamente la implementación.
 */
@Mapper(componentModel = "spring", uses = {
    ClienteMapper.class,
    VehiculoMapper.class,
    DocumentoMapper.class
})
public interface AlquilerMapper extends BaseMapper<Alquiler, AlquilerDTO, String> {

  /**
   * Convierte Alquiler a AlquilerDTO.
   * Todas las relaciones se mapean como DTOs completos:
   * - Cliente
   * - Vehiculo
   * - Documento
   */
  @Override
  AlquilerDTO toDTO(Alquiler entity);

  /**
   * Convierte AlquilerDTO a Alquiler.
   * Todas las relaciones se mapean desde sus DTOs.
   */
  @Override
  Alquiler toEntity(AlquilerDTO dto);

}

