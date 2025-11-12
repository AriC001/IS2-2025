package com.nexora.proyectointegrador2.front_cliente.business.persistence.dao;

import org.springframework.web.client.RestTemplate;
import com.nexora.proyectointegrador2.front_cliente.dto.AlquilerDTO;
import org.springframework.stereotype.Repository;

@Repository
public class AlquilerDAO extends BaseDAO<AlquilerDTO, String> {

  public AlquilerDAO(RestTemplate restTemplate) {
    super(restTemplate, "/alquiler");
  }

  @Override
  protected Class<AlquilerDTO> getEntityClass() {
    return AlquilerDTO.class;
  }
<<<<<<< HEAD

}

=======
    
}
>>>>>>> 7371089156af9abfa0195bc80f465e0e9363c84c
