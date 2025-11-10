package com.practica.ej2b.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.practica.ej2b.business.domain.entity.BaseEntity;
import com.practica.ej2b.business.logic.service.BaseService;

public abstract class BaseRestController<T extends BaseEntity<ID>, ID> {
  
  protected final BaseService<T, ID> service;

  public BaseRestController(BaseService<T, ID> service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<Iterable<T>> getAllActives() {
    try {
      return ResponseEntity.ok(service.findAllActives());
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<T> getById(@PathVariable ID id) {
    try {
      return ResponseEntity.ok(service.findById(id));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  public ResponseEntity<T> create(@RequestBody T entity) {
    try {
      return ResponseEntity.ok(service.save(entity));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<T> update(@PathVariable ID id, @RequestBody T entity) {
    try {
      return ResponseEntity.ok(service.update(entity, id));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable ID id) {
    try {
      service.logicDelete(id);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.notFound().build();
    }
  }
}
