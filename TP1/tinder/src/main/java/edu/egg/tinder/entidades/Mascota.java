package edu.egg.tinder.entidades;

import edu.egg.tinder.enumeracion.Sexo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "mascota")
public class Mascota implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Temporal(TemporalType.TIMESTAMP)
    private Date alta;

    @Temporal(TemporalType.TIMESTAMP)
    private Date baja;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Builder.Default
    private boolean activo = true;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "mascota1", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Voto> votosOriginados = new ArrayList<Voto>();

    @OneToMany(mappedBy = "mascota2", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Voto> votosRecibidos = new ArrayList<Voto>();

    @OneToOne
    @JoinColumn(name = "foto_id")
    private Foto foto;
    //Getters y Setters
    public Long getId(){
        return this.id;
    }
    public boolean isActivo(){
        return this.activo;
    }
    public void desactivar(){
        this.activo=false;
    }
    public String getNombre(){
        return this.nombre;
    }
    public void setNombre(String nombre){
        this.nombre=nombre;
    }
    public Date getAlta(){
        return this.alta;
    }
    public Date getBaja(){
        return this.baja;
    }
    public Sexo getSexo(){
        return this.sexo;
    }
    public void setSexo(Sexo sexo){
        this.sexo=sexo;
    }
    public Usuario getUsuario(){
        return this.usuario;
    }
    public void setUsuario(Usuario usuario){
        this.usuario=usuario;
    }
    public Foto getFoto(){
        return this.foto;
    }
    public void setFoto(Foto foto){
        this.foto=foto;
    }
}
