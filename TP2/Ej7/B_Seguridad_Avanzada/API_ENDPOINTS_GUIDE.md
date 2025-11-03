# 🚀 Guía de Endpoints API REST con Access Token

Esta guía muestra cómo usar todos los endpoints API protegidos con JWT Access Token de Auth0.

## 📋 Índice
- [Obtener Access Token](#obtener-access-token)
- [Endpoints de Usuarios](#endpoints-de-usuarios)
- [Endpoints de Mascotas](#endpoints-de-mascotas)
- [Endpoints de Votos](#endpoints-de-votos)
- [Health Check](#health-check)

---

## 🔑 Obtener Access Token

### Opción 1: Con Postman
```
POST https://dev-2qujqqri8c7ewao0.us.auth0.com/oauth/token
Content-Type: application/json

{
  "client_id": "sOpvhNrec0pGyRreRXGChh4kkXN40jVO",
  "client_secret": "YPj2C7wDCzgoZH3r0RQAlH8FRFvRECfcYkb25wrY95Hb1H3h07QD7cswtGGkC0aj",
  "audience": "https://tinder-api",
  "grant_type": "client_credentials"
}
```

### Opción 2: Con tu aplicación
```
GET http://localhost:9000/auth0/get-access-token
```

**Respuesta:**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIs...",
  "token_type": "Bearer",
  "expires_in": 86400
}
```

**Copia el `access_token` y úsalo en todos los endpoints a continuación.**

---

## 👤 Endpoints de Usuarios

### 1. Información del usuario autenticado (desde JWT)

```http
GET http://localhost:9000/api/v1/usuarios/me
Authorization: Bearer <tu_access_token>
```

**Respuesta:**
```json
{
  "sub": "auth0|123456",
  "email": "usuario@ejemplo.com",
  "name": "Juan Pérez",
  "permissions": [],
  "exp": "2024-11-04T12:00:00Z",
  "iat": "2024-11-03T12:00:00Z",
  "aud": ["https://tinder-api"]
}
```

**Ejemplo con curl:**
```bash
curl -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..." \
  http://localhost:9000/api/v1/usuarios/me
```

---

### 2. Obtener usuario por ID

```http
GET http://localhost:9000/api/v1/usuarios/{id}
Authorization: Bearer <tu_access_token>
```

**Ejemplo:**
```http
GET http://localhost:9000/api/v1/usuarios/1
Authorization: Bearer <tu_access_token>
```

**Respuesta:**
```json
{
  "success": true,
  "usuario": {
    "id": 1,
    "nombre": "Juan",
    "apellido": "Pérez",
    "mail": "juan@ejemplo.com",
    "alta": "2024-01-01T10:00:00Z",
    "mascotas": [...]
  },
  "authenticated_as": "usuario@ejemplo.com"
}
```

**Ejemplo con Postman:**
1. Método: `GET`
2. URL: `http://localhost:9000/api/v1/usuarios/1`
3. Headers: `Authorization: Bearer <tu_access_token>`
4. Send

---

### 3. Buscar usuario por email

```http
GET http://localhost:9000/api/v1/usuarios/email/{email}
Authorization: Bearer <tu_access_token>
```

**Ejemplo:**
```http
GET http://localhost:9000/api/v1/usuarios/email/juan@ejemplo.com
Authorization: Bearer <tu_access_token>
```

**Respuesta:**
```json
{
  "success": true,
  "usuario": {
    "id": 1,
    "nombre": "Juan",
    "apellido": "Pérez",
    "mail": "juan@ejemplo.com"
  },
  "authenticated_as": "usuario@ejemplo.com"
}
```

---

## 🐾 Endpoints de Mascotas

### 4. Listar todas las mascotas activas

```http
GET http://localhost:9000/api/v1/mascotas
Authorization: Bearer <tu_access_token>
```

**Respuesta:**
```json
{
  "success": true,
  "total": 5,
  "mascotas": [
    {
      "id": 1,
      "nombre": "Firulais",
      "sexo": "MACHO",
      "alta": "2024-01-15T10:00:00Z",
      "activo": true,
      "usuario": {...}
    },
    ...
  ],
  "authenticated_as": "usuario@ejemplo.com"
}
```

**Ejemplo con JavaScript (fetch):**
```javascript
const accessToken = "eyJhbGciOiJSUzI1NiIs...";

fetch('http://localhost:9000/api/v1/mascotas', {
  headers: {
    'Authorization': `Bearer ${accessToken}`
  }
})
.then(response => response.json())
.then(data => {
  console.log('Mascotas:', data.mascotas);
  console.log('Total:', data.total);
});
```

---

### 5. Obtener mascota por ID

```http
GET http://localhost:9000/api/v1/mascotas/{id}
Authorization: Bearer <tu_access_token>
```

**Ejemplo:**
```http
GET http://localhost:9000/api/v1/mascotas/1
Authorization: Bearer <tu_access_token>
```

**Respuesta:**
```json
{
  "success": true,
  "mascota": {
    "id": 1,
    "nombre": "Firulais",
    "sexo": "MACHO",
    "alta": "2024-01-15T10:00:00Z",
    "activo": true,
    "usuario": {
      "id": 1,
      "nombre": "Juan",
      "mail": "juan@ejemplo.com"
    },
    "foto": {...}
  },
  "authenticated_as": "usuario@ejemplo.com"
}
```

---

### 6. Listar mascotas de un usuario específico

```http
GET http://localhost:9000/api/v1/mascotas/usuario/{id}
Authorization: Bearer <tu_access_token>
```

**Ejemplo:**
```http
GET http://localhost:9000/api/v1/mascotas/usuario/1
Authorization: Bearer <tu_access_token>
```

**Respuesta:**
```json
{
  "success": true,
  "total": 2,
  "usuario_id": 1,
  "mascotas": [
    {
      "id": 1,
      "nombre": "Firulais",
      "sexo": "MACHO"
    },
    {
      "id": 2,
      "nombre": "Laika",
      "sexo": "HEMBRA"
    }
  ],
  "authenticated_as": "usuario@ejemplo.com"
}
```

---

### 7. Listar votos recibidos por una mascota

```http
GET http://localhost:9000/api/v1/mascotas/{id}/votos
Authorization: Bearer <tu_access_token>
```

**Ejemplo:**
```http
GET http://localhost:9000/api/v1/mascotas/1/votos
Authorization: Bearer <tu_access_token>
```

**Respuesta:**
```json
{
  "success": true,
  "total": 3,
  "mascota_id": 1,
  "mascota_nombre": "Firulais",
  "votos": [
    {
      "id": 1,
      "fecha": "2024-01-20T14:30:00Z",
      "mascota1": {...},
      "mascota2": {...},
      "respuesta": null
    },
    ...
  ],
  "authenticated_as": "usuario@ejemplo.com"
}
```

---

## ❤️ Endpoints de Votos

### 8. Crear un nuevo voto

```http
POST http://localhost:9000/api/v1/votos
Authorization: Bearer <tu_access_token>
Content-Type: application/json

{
  "idUsuario": 1,
  "idMascota1": 1,
  "idMascota2": 2
}
```

**Descripción:** La mascota con ID 1 vota a la mascota con ID 2.

**Respuesta exitosa:**
```json
{
  "success": true,
  "message": "Voto registrado exitosamente",
  "authenticated_as": "usuario@ejemplo.com"
}
```

**Respuesta con error:**
```json
{
  "success": false,
  "error": "Ya votaste a esta mascota"
}
```

**Ejemplo con Postman:**
1. Método: `POST`
2. URL: `http://localhost:9000/api/v1/votos`
3. Headers:
   - `Authorization: Bearer <tu_access_token>`
   - `Content-Type: application/json`
4. Body (raw, JSON):
   ```json
   {
     "idUsuario": 1,
     "idMascota1": 1,
     "idMascota2": 2
   }
   ```
5. Send

**Ejemplo con curl:**
```bash
curl -X POST http://localhost:9000/api/v1/votos \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..." \
  -H "Content-Type: application/json" \
  -d '{
    "idUsuario": 1,
    "idMascota1": 1,
    "idMascota2": 2
  }'
```

---

### 9. Responder a un voto

```http
POST http://localhost:9000/api/v1/votos/{id}/responder
Authorization: Bearer <tu_access_token>
Content-Type: application/json

{
  "idUsuario": 2
}
```

**Ejemplo:**
```http
POST http://localhost:9000/api/v1/votos/5/responder
Authorization: Bearer <tu_access_token>
Content-Type: application/json

{
  "idUsuario": 2
}
```

**Respuesta:**
```json
{
  "success": true,
  "message": "Voto respondido exitosamente",
  "authenticated_as": "usuario@ejemplo.com"
}
```

---

## 🏥 Health Check

### 10. Verificar estado de la API

```http
GET http://localhost:9000/api/v1/health
Authorization: Bearer <tu_access_token>
```

**Respuesta:**
```json
{
  "status": "UP",
  "message": "API está funcionando correctamente",
  "authenticated": true,
  "authenticated_as": "usuario@ejemplo.com",
  "token_expires_at": "2024-11-04T12:00:00Z"
}
```

---

## 📝 Notas Importantes

### ⚠️ Autenticación requerida
Todos estos endpoints **requieren** el header `Authorization: Bearer <access_token>`.

Sin este header, obtendrás:
```json
{
  "error": "Unauthorized",
  "status": 401
}
```

### ⏰ Expiración del token
El Access Token expira en **24 horas** (86400 segundos).

Después de ese tiempo, debes obtener un nuevo token.

### 🔄 Manejo de errores comunes

| Código | Error | Solución |
|--------|-------|----------|
| 401 | Unauthorized | Token inválido o expirado. Obtén uno nuevo |
| 403 | Forbidden | No tienes permisos para esta operación |
| 404 | Not Found | El recurso (usuario, mascota, voto) no existe |
| 400 | Bad Request | Datos incorrectos en el body (POST) |
| 500 | Internal Server Error | Error en el servidor |

### 🔗 Testing con Postman - Colección Completa

Crea una colección en Postman con estos endpoints:

1. **Crear variable de entorno:**
   - Variable: `access_token`
   - Variable: `base_url` = `http://localhost:9000`

2. **Script para obtener token automáticamente:**
   En la petición POST al endpoint de Auth0, agrega en "Tests":
   ```javascript
   const response = pm.response.json();
   pm.environment.set("access_token", response.access_token);
   ```

3. **Usar en todas las peticiones:**
   - Authorization: `Bearer Token`
   - Token: `{{access_token}}`

---

## 🎯 Resumen de Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/usuarios/me` | Info del usuario autenticado |
| GET | `/api/v1/usuarios/{id}` | Obtener usuario por ID |
| GET | `/api/v1/usuarios/email/{email}` | Buscar usuario por email |
| GET | `/api/v1/mascotas` | Listar todas las mascotas |
| GET | `/api/v1/mascotas/{id}` | Obtener mascota por ID |
| GET | `/api/v1/mascotas/usuario/{id}` | Mascotas de un usuario |
| GET | `/api/v1/mascotas/{id}/votos` | Votos de una mascota |
| POST | `/api/v1/votos` | Crear nuevo voto |
| POST | `/api/v1/votos/{id}/responder` | Responder voto |
| GET | `/api/v1/health` | Health check |

---

## 🚀 ¡Listo para probar!

1. **Obtén tu Access Token** (Postman o `/auth0/get-access-token`)
2. **Copia el token**
3. **Agrega el header** `Authorization: Bearer <token>` en todas las peticiones
4. **Prueba los endpoints** con Postman, curl o JavaScript

¡Disfruta tu API protegida con Auth0! 🎉
