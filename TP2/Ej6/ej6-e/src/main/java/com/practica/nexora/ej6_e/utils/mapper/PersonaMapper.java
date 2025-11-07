package com.practica.nexora.ej6_e.utils.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.practica.nexora.ej6_e.business.domain.dto.PersonaDTO;
import com.practica.nexora.ej6_e.business.domain.entity.Persona;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {UsuarioMapper.class}
)
public interface PersonaMapper extends BaseMapper<Persona, PersonaDTO, Long> {

    @Override
    @Mapping(target = "usuario.persona", ignore = true)
    PersonaDTO toDTO(Persona entity);

}
