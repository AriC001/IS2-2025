package com.sport.proyecto.entidades;

import java.util.List;

import com.sport.proyecto.enums.tipoEmpleado;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
public class Empleado extends Persona {

    @Enumerated(EnumType.STRING)
    private tipoEmpleado tipoEmpleado;
    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rutina> rutinas;
    
    public tipoEmpleado getTipoEmpleado() {
        return tipoEmpleado;
    }
    public void setTipoEmpleado(tipoEmpleado tipoEmpleado) {
        this.tipoEmpleado = tipoEmpleado;
    }
    public List<Rutina> getRutinas() {
        return rutinas;
    }
    public void setRutinas(List<Rutina> rutinas) {
        this.rutinas = rutinas;
    }

    
}
