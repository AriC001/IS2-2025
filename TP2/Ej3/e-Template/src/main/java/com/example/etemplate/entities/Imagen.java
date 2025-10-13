package com.example.etemplate.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import javax.management.openmbean.ArrayType;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Imagen {
    @Id
    @Generated
    private String id;
    private String name;
    private String mime;
    private byte[] content;
    private boolean deleted;
}
