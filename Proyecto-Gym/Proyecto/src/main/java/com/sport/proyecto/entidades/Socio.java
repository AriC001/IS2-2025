package com.sport.proyecto.entidades;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Socio extends Persona {

    @Column(unique = true, nullable = true)
    private Long numeroSocio;
    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rutina> rutinas;
}
