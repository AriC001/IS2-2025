package com.example.medicine.model;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import com.example.medicine.errors.ErrorServicio;

import java.util.ArrayList;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "historias_clinicas")
public class HistoriaClinica implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "historiaClinica", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<DetalleHistoriaClinica> detallesHistoriaClinica;
    
    
    @Column(name = "eliminado")
    private boolean eliminado;
    
    public boolean isActivo() {
        return !eliminado;
    }
    public List<Medico> getMedicos() {
        List<Medico> medicos = new ArrayList<>();
        for (DetalleHistoriaClinica detalle : detallesHistoriaClinica) {
            medicos.add(detalle.getMedico());
        }
        return medicos;
    }
    public List<DetalleHistoriaClinica> crearDetalleHistoriaClinica(List<String> detalles, List<LocalDate> fechas, List<Medico> medicos) throws ErrorServicio{
        if(detalles.size()!=fechas.size() || detalles.size()!=medicos.size() || medicos.size()!=fechas.size()){
            System.out.println("error 1");
            throw new ErrorServicio("Los tamaños de fechas, detalles y médicos no son iguales");
        }
        List<DetalleHistoriaClinica> detalleHistoria = new ArrayList<>();
        for(int i = 0; i < detalles.size(); i++){
            if(detalles.get(i)!=null){
                DetalleHistoriaClinica d = new DetalleHistoriaClinica();
                d.setDetalleHistoria(detalles.get(i));
                if(fechas.get(i)!=null && fechas.get(i).isBefore(LocalDate.now())){
                    d.setFechaHistoria(fechas.get(i));
                
                }else{
                    System.out.println("error 2");
                    throw new ErrorServicio("Fecha inválida");
                }
                if(medicos.get(i).getId()!=null && !medicos.get(i).isEliminado()){
                    d.setMedico(medicos.get(i));
                }else{
                    System.out.println("error 3");
                    throw new ErrorServicio("Médico inválido");
                }
                detalleHistoria.add(d);
                d.setHistoriaClinica(this);
            }else{
                    System.out.println("error 4");
                    throw new ErrorServicio("Detalle inválido");
                }
        }
        System.out.println("detalles creados");
        return detalleHistoria;
    }

}
