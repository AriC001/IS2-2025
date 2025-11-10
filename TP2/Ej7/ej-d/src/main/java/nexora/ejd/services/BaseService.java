package nexora.ejd.services;

import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * BaseService genérico usando Template Method.
 * Extender para proveer mergeForUpdate y validaciones específicas.
 */
public abstract class BaseService<T, ID> {

    protected final JpaRepository<T, ID> repository;

    protected BaseService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    // CREATE
    public T saveOne(T entity) throws Exception {
        try {
            //beforeValidate(entity);
            //validateEntity(entity);
            //beforeSave(entity);
            T saved = repository.save(entity);
            //afterSave(saved);
            return saved;
        } catch (Exception e) {
            throw new Exception("Error creando entidad: " + e.getMessage(), e);
        }
    }

    // READ
    public List<T> findAll() throws Exception {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new Exception("Error obteniendo lista: " + e.getMessage(), e);
        }
    }

    public T findById(ID id) throws Exception {
        try {
            requireId(id);
            Optional<T> opt = repository.findById(id);
            return opt.orElseThrow(() -> new Exception("No existe la entidad con id=" + id));
        } catch (Exception e) {
            throw new Exception("Error buscando entidad: " + e.getMessage(), e);
        }
    }

    // UPDATE
    public T updateOne(T incoming, ID id) throws Exception {
        try {
            requireId(id);
            T current = findById(id);
            T merged = mergeForUpdate(current, incoming);
            validateEntity(merged);
            beforeUpdate(current, merged);
            T saved = repository.save(merged);
            afterUpdate(saved);
            return saved;
        } catch (Exception e) {
            throw new Exception("Error actualizando entidad: " + e.getMessage(), e);
        }
    }

    // DELETE (soft delete si existe setActivo/isActivo)
    public boolean deleteById(ID id) throws Exception {
        try {
            requireId(id);
            T entity = findById(id);
            if (supportsSoftDelete(entity)) {
                toggleActivo(entity);
                repository.save(entity);
                afterDelete(entity);
                return true;
            } else {
                beforePhysicalDelete(entity);
                repository.deleteById(id);
                afterDelete(entity);
                return true;
            }
        } catch (Exception e) {
            throw new Exception("Error eliminando entidad: " + e.getMessage(), e);
        }
    }

    // Hooks - por defecto no hacen nada
    protected void beforeValidate(T entity) throws Exception {}
    protected void validateEntity(T entity) throws Exception {}
    protected void beforeSave(T entity) throws Exception {}
    protected void afterSave(T entity) throws Exception {}
    protected void beforeUpdate(T current, T merged) throws Exception {}
    protected void afterUpdate(T entity) throws Exception {}
    protected void beforePhysicalDelete(T entity) throws Exception {}
    protected void afterDelete(T entity) throws Exception {}

    // Debe implementar merge específico
    protected abstract T mergeForUpdate(T current, T incoming);

    // Utilidades
    private void requireId(ID id) {
        if (id == null) throw new IllegalArgumentException("ID no puede ser null");
    }

    private boolean supportsSoftDelete(T entity) {
        // soporta propiedades en español (isActivo/setActivo) o inglés (isActive/setActive)
        try {
            Method getter = null;
            Method setter = null;
            try {
                getter = entity.getClass().getMethod("isActivo");
                setter = entity.getClass().getMethod("setActivo", boolean.class);
            } catch (NoSuchMethodException ex) {
                // probar variante en inglés
                getter = entity.getClass().getMethod("isActive");
                setter = entity.getClass().getMethod("setActive", boolean.class);
            }
            return getter != null && setter != null;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private void toggleActivo(T entity) throws Exception {
        try {
            Method getter = null;
            Method setter = null;
            try {
                getter = entity.getClass().getMethod("isActivo");
                setter = entity.getClass().getMethod("setActivo", boolean.class);
            } catch (NoSuchMethodException ex) {
                getter = entity.getClass().getMethod("isActive");
                setter = entity.getClass().getMethod("setActive", boolean.class);
            }
            boolean current = (boolean) getter.invoke(entity);
            setter.invoke(entity, !current);
        } catch (Exception e) {
            throw new Exception("No se pudo alternar el campo activo: " + e.getMessage(), e);
        }
    }
}
