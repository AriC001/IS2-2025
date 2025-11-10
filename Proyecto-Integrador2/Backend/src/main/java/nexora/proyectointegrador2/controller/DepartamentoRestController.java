package nexora.proyectointegrador2.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Departamento;
import nexora.proyectointegrador2.business.logic.service.DepartamentoService;
import nexora.proyectointegrador2.business.persistence.repository.DepartamentoRepository;
import nexora.proyectointegrador2.utils.dto.DepartamentoDTO;
import nexora.proyectointegrador2.utils.mapper.impl.DepartamentoMapper;

@RestController
@RequestMapping("api/v1/departamentos")
public class DepartamentoRestController extends BaseRestController<Departamento, DepartamentoDTO, String> {
  
  private final DepartamentoRepository departamentoRepository;

  public DepartamentoRestController(DepartamentoService service, DepartamentoMapper adapter, DepartamentoRepository departamentoRepository) {
    super(service, adapter);
    this.departamentoRepository = departamentoRepository;
  }

  /**
   * Endpoint para obtener departamentos por provincia (para selects dependientes)
   */
  @GetMapping("/by-provincia")
  public List<DepartamentoDTO> getDepartamentosByProvincia(@RequestParam String provinciaId) {
    logger.debug("Obteniendo departamentos para provincia ID: {}", provinciaId);
    List<Departamento> departamentos = departamentoRepository.findAllByProvincia_IdAndEliminadoFalse(provinciaId)
        .stream()
        .collect(Collectors.toList());
    List<DepartamentoDTO> dtos = mapper.toDTOList(departamentos);
    logger.debug("Se encontraron {} departamentos para la provincia {}", dtos.size(), provinciaId);
    return dtos;
  }
}
