package com.sport.proyecto.entidades;

import java.util.List;

import com.sport.proyecto.enums.tipoEmpleado;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("EMPLEADO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Empleado extends Persona {

    @Enumerated(EnumType.STRING)
    private tipoEmpleado tipoEmpleado;
    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rutina> rutinas;
}
