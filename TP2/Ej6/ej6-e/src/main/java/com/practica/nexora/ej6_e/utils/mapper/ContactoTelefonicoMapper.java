package com.practica.nexora.ej6_e.utils.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.practica.nexora.ej6_e.business.domain.dto.ContactoTelefonicoDTO;
import com.practica.nexora.ej6_e.business.domain.entity.ContactoTelefonico;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {PersonaMapper.class}
)
public interface ContactoTelefonicoMapper extends BaseMapper<ContactoTelefonico, ContactoTelefonicoDTO, Long> {

}
