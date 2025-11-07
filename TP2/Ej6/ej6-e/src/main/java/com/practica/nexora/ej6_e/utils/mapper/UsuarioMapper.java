package com.practica.nexora.ej6_e.utils.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.practica.nexora.ej6_e.business.domain.dto.UsuarioDTO;
import com.practica.nexora.ej6_e.business.domain.entity.Usuario;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {PersonaMapper.class}
)
public interface UsuarioMapper extends BaseMapper<Usuario, UsuarioDTO, Long> {

    @Override
    @Mapping(target = "persona.usuario", ignore = true)
    UsuarioDTO toDTO(Usuario entity);

}
