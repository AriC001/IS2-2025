package com.practica.nexora.ej6_e.utils.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import com.practica.nexora.ej6_e.business.domain.dto.ContactoCorreoElectronicoDTO;
import com.practica.nexora.ej6_e.business.domain.dto.ContactoDTO;
import com.practica.nexora.ej6_e.business.domain.dto.ContactoTelefonicoDTO;
import com.practica.nexora.ej6_e.business.domain.dto.EmpresaDTO;
import com.practica.nexora.ej6_e.business.domain.entity.Contacto;
import com.practica.nexora.ej6_e.business.domain.entity.ContactoCorreoElectronico;
import com.practica.nexora.ej6_e.business.domain.entity.ContactoTelefonico;
import com.practica.nexora.ej6_e.business.domain.entity.Empresa;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class EmpresaMapper implements BaseMapper<Empresa, EmpresaDTO, Long> {

  @Autowired
  protected ContactoCorreoElectronicoMapper contactoCorreoElectronicoMapper;

  @Autowired
  protected ContactoTelefonicoMapper contactoTelefonicoMapper;

  // Mapeo polimórfico de ContactoDTO a Contacto
  protected Contacto contactoDTOToContacto(ContactoDTO dto) {
    if (dto == null) {
      return null;
    }
    if (dto instanceof ContactoCorreoElectronicoDTO correoDTO) {
      return contactoCorreoElectronicoMapper.toEntity(correoDTO);
    }
    if (dto instanceof ContactoTelefonicoDTO telefonoDTO) {
      return contactoTelefonicoMapper.toEntity(telefonoDTO);
    }
    throw new IllegalArgumentException("Tipo de ContactoDTO no soportado: " + dto.getClass().getName());
  }

  // Mapeo polimórfico de Contacto a ContactoDTO
  protected ContactoDTO contactoToContactoDTO(Contacto entity) {
    if (entity == null) {
      return null;
    }
    if (entity instanceof ContactoCorreoElectronico correo) {
      return contactoCorreoElectronicoMapper.toDTO(correo);
    }
    if (entity instanceof ContactoTelefonico telefono) {
      return contactoTelefonicoMapper.toDTO(telefono);
    }
    throw new IllegalArgumentException("Tipo de Contacto no soportado: " + entity.getClass().getName());
  }

}

