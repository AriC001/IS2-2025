# Informe de Desarrollo

## 1. Estimación de Tiempos con Planning Poker

| Tarea | Estimación | Tiempo Real |
|-------|-----------|-------------|
| ABM Usuarios | 1 hora | 40 min | 
| ABM Personas | 1 hora | 40 min | 
| ABM Empresas | 1 hora | 40 min | 
| ABM Contactos Email | 1 hora | 40 min |
| ABM Contactos Teléfono | 1 hora | 40 min | 
| **Total** | **5 horas** | **3h 20min** | 

### Conclusiones
El desarrollo fue más rápido de lo estimado. Esto se debe a que las estimaciones no consideraron la reutilización de patrones base (BaseController, BaseService, BaseMapper). Una vez configurado el primer ABM, los siguientes se implementaron más rápido gracias a la estructura genérica establecida.

---

## 2. Patrones de Diseño Utilizados

### 2.1 Template Method

**Concepto Teórico:**
Define el esqueleto de un algoritmo en una clase base, delegando algunos pasos específicos a las subclases. Permite que las subclases redefinan ciertos pasos sin cambiar la estructura general del algoritmo.

**Dónde se utilizó en el ejercicio:**

**BaseService** - Define el flujo común de las operaciones de ABM:
```java
public abstract class BaseService<T extends BaseEntity<ID>, ID> {
    
    // Template method - define el algoritmo general
    public T save(T entity) {
        validate(entity);  // Paso delegado a subclases
        return repository.save(entity);
    }
    
    // Método abstracto - cada subclase implementa su validación
    protected abstract void validate(T entity);
}
```

**Implementaciones específicas:**
```java
// UsuarioService implementa el paso de validación
public class UsuarioService extends BaseService<Usuario, Long> {
    @Override
    protected void validate(Usuario entity) {
        if (entity.getNombreUsuario() == null) {
            throw new IllegalArgumentException("Usuario requerido");
        }
    }
}

// PersonaService implementa su propia validación
public class PersonaService extends BaseService<Persona, Long> {
    @Override
    protected void validate(Persona entity) {
        if (entity.getNombre() == null || entity.getApellido() == null) {
            throw new IllegalArgumentException("Nombre y apellido requeridos");
        }
    }
}
```
---

### 2.2 Builder

**Concepto:** Construye objetos complejos paso a paso, evitando constructores con muchos parámetros.

**Uso en el ejercicio:**

**Lombok `@Builder` en entidades:**
```java
@Entity
@Builder
public class Usuario extends BaseEntity<Long> {
    private String nombreUsuario;
    private String clave;
}

// Construcción fluida
Usuario usuario = Usuario.builder()
    .nombreUsuario("john.doe")
    .clave("encrypted")
    .build();
```

**SecurityConfig con builder pattern:**
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login", "/register").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/")
        )
        .build();  // Builder pattern de Spring Security
}
```
---

### 2.3 Adapter/Mapper

**Concepto:** Convierte objetos de un tipo a otro. Adapta entidades JPA ↔ DTOs.

**Uso:** MapStruct con `BaseMapper<E, D, ID>` - genera código automáticamente
```java
public interface UsuarioMapper extends BaseMapper<Usuario, UsuarioDTO, Long> {
    @Mapping(target = "persona.usuario", ignore = true)
    UsuarioDTO toDTO(Usuario entity);
}
```

**Mappers creados:** UsuarioMapper, PersonaMapper, EmpresaMapper, ContactoCorreoElectronicoMapper, ContactoTelefonicoMapper

**Uso en controllers:**
```java
// DTO → Entidad al crear
ContactoTelefonico entity = mapper.toEntity(dto);

// Entidad → DTO al listar
model.addAttribute("entities", mapper.toDTOList(entities));
```

### 2.4 Facade

**Patrón Facade aplicado:**
MapStruct actúa como facade simplificando la complejidad del mapeo manual entre objetos. Proporciona una interfaz unificada (`BaseMapper`) que oculta los detalles de conversión de tipos, manejo de nulos, y referencias circulares.
