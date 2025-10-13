package com.example.etemplate.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Usuario {
    @Id
    @Generated
    private String id;
    private String name;
    private String key;
    private boolean deleted;


}
