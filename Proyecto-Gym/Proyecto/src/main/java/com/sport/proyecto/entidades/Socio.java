package com.sport.proyecto.entidades;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Socio extends Persona {

    @Column(unique = true, nullable = true)
    private Long numeroSocio;
}
