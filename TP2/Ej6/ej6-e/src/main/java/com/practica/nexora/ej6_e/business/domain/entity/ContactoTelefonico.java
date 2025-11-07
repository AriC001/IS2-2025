package com.practica.nexora.ej6_e.business.domain.entity;

import com.practica.nexora.ej6_e.business.enums.TipoTelefono;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "contactos_telefonicos")
public class ContactoTelefonico extends Contacto {

  private String numeroTelefono;

  private TipoTelefono tipoTelefono;

}
