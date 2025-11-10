package nexora.proyectointegrador2.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Provincia;
import nexora.proyectointegrador2.business.logic.service.ProvinciaService;
import nexora.proyectointegrador2.business.persistence.repository.ProvinciaRepository;
import nexora.proyectointegrador2.utils.dto.ProvinciaDTO;
import nexora.proyectointegrador2.utils.mapper.impl.ProvinciaMapper;

@RestController
@RequestMapping("api/v1/provincias")
public class ProvinciaRestController extends BaseRestController<Provincia, ProvinciaDTO, String> {

  private final ProvinciaRepository provinciaRepository;

  public ProvinciaRestController(ProvinciaService service, ProvinciaMapper adapter, ProvinciaRepository provinciaRepository) {
    super(service, adapter);
    this.provinciaRepository = provinciaRepository;
  }

  /**
   * Endpoint para obtener provincias por país (para selects dependientes)
   */
  @GetMapping("/by-pais")
  public List<ProvinciaDTO> getProvinciasByPais(@RequestParam String paisId) {
    logger.debug("Obteniendo provincias para país ID: {}", paisId);
    List<Provincia> provincias = provinciaRepository.findAllByPais_IdAndEliminadoFalse(paisId)
        .stream()
        .collect(Collectors.toList());
    List<ProvinciaDTO> dtos = mapper.toDTOList(provincias);
    logger.debug("Se encontraron {} provincias para el país {}", dtos.size(), paisId);
    return dtos;
  }
}

