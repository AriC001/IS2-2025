package com.practica.nexora.ej6_e.utils.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.practica.nexora.ej6_e.business.domain.dto.ContactoCorreoElectronicoDTO;
import com.practica.nexora.ej6_e.business.domain.entity.ContactoCorreoElectronico;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {PersonaMapper.class}
)
public interface ContactoCorreoElectronicoMapper extends BaseMapper<ContactoCorreoElectronico, ContactoCorreoElectronicoDTO, Long> {

}
