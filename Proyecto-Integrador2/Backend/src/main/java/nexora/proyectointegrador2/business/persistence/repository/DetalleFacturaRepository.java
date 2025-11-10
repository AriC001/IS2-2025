package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.DetalleFactura;

@Repository
public interface DetalleFacturaRepository extends BaseRepository<DetalleFactura, String> {

  Collection<DetalleFactura> findAllByFacturaId(String facturaId);
}


