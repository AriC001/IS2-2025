package nexora.proyectointegrador2.business.domain.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity<ID> implements Serializable{

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(length = 36)
  protected ID id;
  
  @Column(nullable = false)
  protected boolean eliminado;

}