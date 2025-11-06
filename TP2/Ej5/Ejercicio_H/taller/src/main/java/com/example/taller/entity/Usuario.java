package com.example.taller.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import com.example.taller.entity.Rol;;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="usuario")
public class Usuario extends BaseEntity<String>{


    @OneToOne
     @JoinColumn(name = "mecanico_id")
    private Mecanico mecanico;
    
    private String nombreUsuario;

    private String clave;

    private Boolean eliminado;


    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Boolean getEliminado() {
        return this.eliminado;
    }

    @Override
    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
    
}
