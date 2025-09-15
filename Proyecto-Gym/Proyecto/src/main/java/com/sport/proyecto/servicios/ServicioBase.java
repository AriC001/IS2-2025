package com.sport.proyecto.servicios;

import java.util.List;

public interface ServicioBase<E> {
  List<E> buscarTodos() throws Exception;
  E buscarPorId(Long id) throws Exception;
  void guardar(E entity) throws Exception;
  E actualizar(E entity, Long id) throws Exception;
  void eliminarPorId(Long id) throws Exception;
}
