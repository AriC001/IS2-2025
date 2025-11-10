package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.Factura;

@Repository
public interface FacturaRepository extends BaseRepository<Factura, String> {

  Optional<Factura> findByNumeroFactura(Long numeroFactura);

  boolean existsByNumeroFactura(Long numeroFactura);
}
