package com.practica.ej1b.business.persistence.repository;

import com.practica.ej1b.business.domain.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProveedorRepositorio extends JpaRepository<Proveedor, String> {

  Collection<Proveedor> findAllByEliminadoFalse();

  Proveedor findByNombreAndApellido(String nombre, String apellido);

  Proveedor findByCuit(String cuit);
}
