package com.example.etemplate.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Proveedor {
    @Id
    @Generated
    private String id;
    private String name;
    private boolean deleted;

}
