package com.example.taller.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public abstract class Persona extends BaseEntity<String> {


    protected String nombre;
    protected String apellido;
    protected boolean eliminado;
    
}
