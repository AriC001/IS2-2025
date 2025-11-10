package com.practica.nexora.ej6_e.utils.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper para Contacto (clase base abstracta).
 * 
 * NOTA: Contacto es una clase abstracta, por lo que no se puede mapear directamente.
 * Use los mappers concretos:
 * - ContactoCorreoElectronicoMapper
 * - ContactoTelefonicoMapper
 * 
 * Este mapper solo existe como marcador para configuración común.
 * NO debe usarse directamente. Use los mappers de las subclases concretas.
 */
@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {PersonaMapper.class}
)
public interface ContactoMapper {
  // Interfaz vacía - solo para configuración base
  // Los mappers concretos (ContactoCorreoElectronicoMapper, ContactoTelefonicoMapper) 
  // heredan esta configuración
}
