# 🔐 Integración OAuth2 + JWT con Auth0

Este proyecto ahora está integrado con **Auth0** para autenticación y autorización usando **OAuth2** y **JWT (JSON Web Tokens)**.

---

## ✅ ¿Qué se implementó?

1. **Dependencias OAuth2**: Se agregaron `spring-boot-starter-oauth2-client` y `spring-boot-starter-oauth2-resource-server` al `pom.xml`.
2. **Configuración de seguridad**: `SecurityConfiguration.java` ahora soporta:
   - **OAuth2 Login**: Permite login con Auth0 (flujo Authorization Code).
   - **JWT Resource Server**: Valida JWTs en cada petición a APIs protegidas.
3. **Validador de Audience**: `AudienceValidator.java` verifica que el JWT está destinado a esta API.
4. **Propiedades de Auth0**: Archivo `application.properties` con configuración para Auth0.

---

## 🧠 ¿JWT tiene que ver con esto?

**Sí, totalmente.** 

- **OAuth2** es el protocolo/framework de autorización.
- **OpenID Connect (OIDC)** extiende OAuth2 para autenticación (saber quién es el usuario).
- **JWT (JSON Web Token)** es el formato que Auth0 usa para emitir los tokens (access token e ID token).

Cuando un usuario se autentica con Auth0:
1. Auth0 emite un **ID Token (JWT)** con info del usuario (nombre, email, etc.).
2. Auth0 emite un **Access Token (JWT)** para acceder a APIs protegidas.
3. Tu backend Spring Boot **valida el JWT** en cada petición (firma, expiración, issuer, audience).

---

## 📋 Pasos para configurar Auth0

### 1. Crear cuenta en Auth0
1. Ve a [https://auth0.com](https://auth0.com) y crea una cuenta gratuita.
2. Selecciona tu región (ej: US, EU).

### 2. Crear una "Application"
1. En el dashboard de Auth0, ve a **Applications** > **Create Application**.
2. Nombre: `Tinder App` (o el que prefieras).
3. Tipo: **Regular Web Application**.
4. Haz clic en **Create**.
5. Ve a la pestaña **Settings** y anota:
   - **Domain** (ej: `tu-tenant.us.auth0.com`)
   - **Client ID**
   - **Client Secret**
6. En **Allowed Callback URLs**, agrega:
   ```
   http://localhost:8080/login/oauth2/code/auth0
   ```
7. En **Allowed Logout URLs**, agrega:
   ```
   http://localhost:8080/
   ```
8. Guarda los cambios.

### 3. Crear una "API"
1. En el dashboard de Auth0, ve a **Applications** > **APIs** > **Create API**.
2. Nombre: `Tinder API` (o el que prefieras).
3. Identifier (audience): `https://tinder-api` (puede ser cualquier URI único, no necesita existir).
4. Signing Algorithm: **RS256**.
5. Haz clic en **Create**.
6. Anota el **Identifier** (este será tu `audience`).

### 4. Configurar `application.properties`
Abre `src/main/resources/application.properties` y reemplaza los valores:

```properties
# Domain de Auth0 (sin https://)
auth0.domain=tu-tenant.us.auth0.com

# Audience (Identifier de tu API)
auth0.audience=https://tinder-api

# Client ID de tu Application
spring.security.oauth2.client.registration.auth0.client-id=TU_CLIENT_ID_AQUI

# Client Secret de tu Application
spring.security.oauth2.client.registration.auth0.client-secret=TU_CLIENT_SECRET_AQUI
```

**Importante**: No subas el `application.properties` con credenciales reales a Git. Usa variables de entorno o un archivo `.env` para producción.

---

## 🚀 Cómo funciona el flujo OAuth2/JWT

### Flujo de Login (Authorization Code Flow)
1. Usuario visita `http://localhost:8080` y hace clic en "Login".
2. La app redirige al usuario a Auth0 (`https://tu-tenant.auth0.com/authorize`).
3. Usuario se autentica en Auth0 (email/password, Google, etc.).
4. Auth0 redirige de vuelta a tu app con un **authorization code**.
5. Tu backend intercambia el code por un **access token (JWT)** y un **ID token (JWT)**.
6. El usuario queda autenticado en tu app con una sesión.

### Flujo de API (JWT Validation)
1. Cliente (ej: frontend SPA, app móvil) obtiene un JWT de Auth0.
2. Cliente envía peticiones a tu API con el JWT en el header:
   ```
   Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
   ```
3. Tu backend Spring Boot:
   - Verifica la **firma** del JWT (usando las claves públicas de Auth0).
   - Verifica el **issuer** (`https://tu-tenant.auth0.com/`).
   - Verifica el **audience** (`https://tinder-api`).
   - Verifica la **expiración** (`exp` claim).
4. Si el JWT es válido, la petición se procesa. Si no, responde `401 Unauthorized`.

---

## 🧪 Probar la integración

### Opción 1: Ejecutar con Maven
```bash
./mvnw clean install
./mvnw spring-boot:run
```

### Opción 2: Ejecutar en IDE
1. Abre el proyecto en IntelliJ IDEA o Eclipse.
2. Ejecuta `WebApplication.java`.

### Opción 3: Ver logs de Spring Security
Agrega en `application.properties`:
```properties
logging.level.org.springframework.security=DEBUG
```

### Probar el login
1. Abre `http://localhost:8080`.
2. Haz clic en "Login".
3. Deberías ser redirigido a Auth0 para autenticarte.
4. Después de autenticarte, serás redirigido de vuelta a tu app.

---

## 📦 Estructura del JWT

Un JWT tiene 3 partes separadas por puntos:
```
header.payload.signature
```

Ejemplo de **payload** decodificado (claims):
```json
{
  "iss": "https://tu-tenant.auth0.com/",
  "sub": "auth0|123456789",
  "aud": "https://tinder-api",
  "iat": 1699000000,
  "exp": 1699086400,
  "email": "usuario@example.com",
  "permissions": ["read:mascotas", "write:mascotas"]
}
```

- **iss** (issuer): Quién emitió el token (Auth0).
- **sub** (subject): ID único del usuario.
- **aud** (audience): Para quién está destinado el token (tu API).
- **iat** (issued at): Cuándo se emitió.
- **exp** (expiration): Cuándo expira.
- **permissions**: Permisos/scopes del usuario (puedes configurarlos en Auth0).

---

## 🔒 Seguridad: ¿Por qué JWT es seguro?

1. **Firma criptográfica**: El JWT está firmado con la clave privada de Auth0. Tu backend verifica la firma con la clave pública de Auth0 (descargada automáticamente desde `https://tu-tenant.auth0.com/.well-known/jwks.json`).
2. **Sin estado (stateless)**: No necesitas almacenar sesiones en base de datos. El JWT contiene toda la info necesaria.
3. **Expiración**: Los JWTs expiran automáticamente (ej: 1 hora). Puedes usar refresh tokens para renovarlos.
4. **Audience validation**: Tu API solo acepta JWTs destinados a ella (claim `aud`).

---

## 🎯 Próximos pasos opcionales

### 1. Configurar roles y permisos en Auth0
1. En Auth0, ve a **User Management** > **Roles** > **Create Role**.
2. Crea roles como `admin`, `user`, etc.
3. En **APIs** > **Tu API** > **Permissions**, crea permisos como `read:mascotas`, `write:mascotas`.
4. Asigna roles a usuarios.
5. En Spring Boot, usa `@PreAuthorize("hasAuthority('ROLE_read:mascotas')")` en tus controladores.

### 2. Proteger endpoints específicos
Modifica `SecurityConfiguration.java`:
```java
.authorizeHttpRequests(auth -> auth
  .requestMatchers("/api/admin/**").hasAuthority("ROLE_admin")
  .requestMatchers("/api/mascotas/**").hasAnyAuthority("ROLE_read:mascotas", "ROLE_write:mascotas")
  .anyRequest().authenticated()
)
```

### 3. Obtener info del usuario autenticado
En tus controladores:
```java
@GetMapping("/perfil")
public String perfil(@AuthenticationPrincipal Jwt jwt) {
    String email = jwt.getClaimAsString("email");
    String userId = jwt.getSubject();
    // ...
}
```

### 4. Configurar refresh tokens
En Auth0, habilita "Refresh Token Rotation" en tu Application para mayor seguridad.

### 5. Migrar usuarios existentes
Si ya tienes usuarios en tu base de datos con BCrypt, puedes:
- Usar Auth0 Database Connections con Custom Scripts.
- Migrar usuarios a Auth0 mediante su API.

---

## 📚 Recursos útiles

- [Documentación de Auth0](https://auth0.com/docs)
- [Spring Security OAuth2](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)
- [JWT.io - Decodificador de JWTs](https://jwt.io)
- [Auth0 Quickstart - Spring Boot](https://auth0.com/docs/quickstart/backend/java-spring-security5)

---

## ❓ FAQ

**P: ¿Necesito tener usuarios en mi base de datos?**  
R: Depende. Puedes usar solo Auth0 para gestionar usuarios, o sincronizar usuarios de Auth0 a tu BD local cuando se loguean por primera vez.

**P: ¿Puedo seguir usando mi formulario de login existente?**  
R: Sí, pero necesitarías implementar un flujo "Resource Owner Password Grant" (no recomendado) o migrar a OAuth2. Lo ideal es usar el login de Auth0.

**P: ¿Qué pasa si Auth0 está caído?**  
R: Tu app no podrá autenticar nuevos usuarios. Los usuarios con sesiones activas seguirán funcionando. Para producción, considera un SLA de Auth0 (planes de pago) o tener un plan de contingencia.

**P: ¿Es gratis Auth0?**  
R: Sí, el plan gratuito incluye hasta 7,000 usuarios activos mensuales y características básicas. Para más, necesitas un plan de pago.

---

## 🛠️ Troubleshooting

### Error: "Cannot resolve configuration property 'auth0.domain'"
- Es normal antes de ejecutar Maven. Los errores desaparecerán después de `mvn clean install`.

### Error: "Invalid issuer"
- Verifica que `auth0.domain` en `application.properties` sea correcto (sin `https://`).

### Error: "Invalid audience"
- Verifica que `auth0.audience` coincida con el Identifier de tu API en Auth0.

### Error: "Unauthorized" al llamar a tu API
- Verifica que el JWT tenga el `aud` (audience) correcto.
- Verifica que el JWT no haya expirado.
- Usa [jwt.io](https://jwt.io) para decodificar el JWT y ver sus claims.

### No se redirige a Auth0
- Verifica que las Callback URLs estén configuradas en Auth0.
- Revisa los logs de Spring Boot con `logging.level.org.springframework.security=DEBUG`.

---

## 📝 Notas finales

Esta integración te permite:
- ✅ Delegar autenticación a Auth0 (login con email, Google, Facebook, etc.).
- ✅ Proteger tu API con JWTs validados automáticamente.
- ✅ Gestionar usuarios, roles y permisos desde el dashboard de Auth0.
- ✅ Evitar almacenar contraseñas en tu base de datos.
- ✅ Implementar MFA (Multi-Factor Authentication) fácilmente.

Si tienes dudas, revisa la documentación oficial de Auth0 o pregunta en los foros de Spring Security.

¡Feliz codificación! 🚀
