# 🔐 Cómo funciona Auth0 con APIs y Tokens JWT

## 📋 Tabla de Contenidos
1. [Ver tu token actual](#1-ver-tu-token-actual)
2. [Anatomía de un JWT](#2-anatomía-de-un-jwt)
3. [Usar el token en APIs](#3-usar-el-token-en-apis)
4. [Escenarios de uso](#4-escenarios-de-uso)
5. [Ejemplos prácticos](#5-ejemplos-prácticos)

---

## 1. Ver tu token actual

### Opción A: Interfaz web (visual)
1. Inicia sesión: `http://localhost:9000/oauth2/authorization/auth0`
2. Ve a: `http://localhost:9000/auth0/token`
3. Verás tu token JWT y podrás copiarlo

### Opción B: JSON raw (para APIs)
1. Inicia sesión
2. Ve a: `http://localhost:9000/auth0/debug`
3. Verás toda la info en formato JSON

---

## ⚠️ IMPORTANTE: ID Token vs Access Token

### Diferencias clave:

| Aspecto | ID Token | Access Token |
|---------|----------|--------------|
| **Propósito** | Autenticar al usuario | Autorizar acceso a APIs |
| **Audience** | Tu aplicación web | Tu API backend |
| **Uso** | Sesión del navegador | Peticiones HTTP a APIs |
| **¿Funciona en APIs?** | ❌ NO | ✅ SÍ |
| **Dónde se usa** | Cookie/sesión | Header Authorization |

### ¿Qué token tienes ahora?

Cuando haces login con OAuth2 en el navegador, obtienes un **ID Token**.
Este token **NO funciona** para llamar a APIs con `Authorization: Bearer`.

### Solución: Usar el endpoint alternativo

Como tu aplicación usa sesión de navegador, usa este endpoint:

```
GET http://localhost:9000/api/v1/usuarios/me/session
```

Este endpoint **SÍ funciona** porque usa la sesión, no el token.

---

## 2. Anatomía de un JWT

Un JWT tiene **3 partes** separadas por puntos:

```
header.payload.signature
```

### Ejemplo real:
```
eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiJhdXRoMHwxMjM0NTYiLCJlbWFpbCI6InVzZXJAZXhhbXBsZS5jb20iLCJleHAiOjE2OTkwMDAwMDB9.
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

### ¿Qué contiene cada parte?

**Header (rojo):**
```json
{
  "alg": "RS256",
  "typ": "JWT"
}
```

**Payload (azul) - Los "claims":**
```json
{
  "sub": "auth0|123456",
  "email": "user@example.com",
  "name": "Juan Pérez",
  "iss": "https://dev-xxx.us.auth0.com/",
  "aud": "https://tinder-api",
  "iat": 1699000000,
  "exp": 1699086400
}
```

**Signature (verde):**
Firma criptográfica que verifica que el token no fue alterado.

### 🔍 Decodificar un JWT:
Ve a [jwt.io](https://jwt.io) y pega tu token para ver su contenido decodificado.

---

## 3. Usar el token en APIs

### Flujo típico:

```
Cliente → Obtiene token de Auth0 → Guarda token → Envía token en cada petición → Backend valida token
```

### Cómo enviar el token:

Todas las peticiones a tu API deben incluir el header:
```
Authorization: Bearer TU_TOKEN_JWT_AQUI
```

---

## 4. Escenarios de uso

### Escenario 1: Aplicación Web (tu caso actual)
- **Usuario:** Navega por tu web
- **Auth0:** Maneja login/logout
- **Token:** Spring Security lo gestiona automáticamente
- **Uso:** El token está en cookies/sesión, no lo manejas manualmente

### Escenario 2: Frontend SPA (React, Angular, Vue)
- **Frontend:** Obtiene token de Auth0
- **Token:** Se guarda en localStorage o memoria
- **API Backend:** Recibe token en cada petición y lo valida
- **Uso:** Frontend envía token explícitamente

### Escenario 3: App Móvil (Android, iOS)
- **App:** Obtiene token de Auth0 SDK
- **Token:** Se guarda en almacenamiento seguro
- **API Backend:** Recibe token y lo valida
- **Uso:** App envía token en header

---

## 4.5. Cómo obtener un Access Token real

### Opción 1: Desde Auth0 Dashboard (más fácil)

1. Ve a: https://manage.auth0.com
2. Applications > APIs > Tu API (`tinder-api`)
3. Pestaña **Test**
4. Copia el **Access Token** que te muestra
5. Úsalo en tus peticiones

### Opción 2: Con curl (Client Credentials)

```bash
curl --request POST \
  --url https://dev-2qujqqri8c7ewao0.us.auth0.com/oauth/token \
  --header 'content-type: application/json' \
  --data '{
    "client_id":"TU_CLIENT_ID",
    "client_secret":"TU_CLIENT_SECRET",
    "audience":"https://tinder-api",
    "grant_type":"client_credentials"
  }'
```

Respuesta:
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer",
  "expires_in": 86400
}
```

### Opción 3: Modificar tu app para obtener Access Token

Necesitas agregar `audience` al flujo OAuth2. Voy a crear un endpoint que lo haga automáticamente.

---

## 5. Ejemplos prácticos

### A. Con curl (Terminal)

```bash
# 1. Primero obtén tu token desde http://localhost:9000/auth0/token
TOKEN="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."

# 2. Haz una petición a tu API
curl -H "Authorization: Bearer $TOKEN" \
     http://localhost:9000/api/v1/usuarios/me
```

### B. Con Postman

1. Abre Postman
2. Crea una petición GET a: `http://localhost:9000/api/v1/usuarios/me`
3. Ve a la pestaña **Authorization**
4. Selecciona **Type:** `Bearer Token`
5. Pega tu token en el campo **Token**
6. Haz clic en **Send**

### C. Con JavaScript (fetch)

```javascript
// 1. Guardar token (por ejemplo, después de login)
const token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...";
localStorage.setItem('auth_token', token);

// 2. Hacer petición con el token
async function obtenerUsuario() {
  const token = localStorage.getItem('auth_token');
  
  const response = await fetch('http://localhost:9000/api/v1/usuarios/me', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });
  
  const data = await response.json();
  console.log(data);
}

obtenerUsuario();
```

### D. Con React (ejemplo completo)

```jsx
import { useState, useEffect } from 'react';

function MiComponente() {
  const [usuario, setUsuario] = useState(null);
  const token = localStorage.getItem('auth_token');

  useEffect(() => {
    fetch('http://localhost:9000/api/v1/usuarios/me', {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
    .then(res => res.json())
    .then(data => setUsuario(data))
    .catch(err => console.error(err));
  }, [token]);

  return (
    <div>
      {usuario ? (
        <p>Email: {usuario.email}</p>
      ) : (
        <p>Cargando...</p>
      )}
    </div>
  );
}
```

### E. Con Python (requests)

```python
import requests

# Token obtenido de Auth0
token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."

# Headers con el token
headers = {
    "Authorization": f"Bearer {token}"
}

# Petición GET
response = requests.get(
    "http://localhost:9000/api/v1/usuarios/me",
    headers=headers
)

print(response.json())
```

---

## 🎯 Flujo completo: Frontend + Backend

### 1. Frontend obtiene el token

```javascript
// En tu SPA (React, Angular, etc.)
async function login() {
  // Redirige a Auth0
  window.location.href = '/oauth2/authorization/auth0';
}

// Después del callback de Auth0, obtienes el token
// (normalmente desde una cookie o endpoint)
```

### 2. Backend valida el token automáticamente

Tu Spring Boot ya está configurado para:
- ✅ Recibir el token en el header `Authorization: Bearer <token>`
- ✅ Validar la firma con las claves de Auth0
- ✅ Verificar expiración, issuer, audience
- ✅ Extraer claims (email, permisos, etc.)

---

## 🔒 Seguridad del token

### ¿Es seguro enviar el token en cada petición?
**Sí**, siempre que:
- ✅ Uses **HTTPS** en producción
- ✅ No expongas el token en URLs (solo en headers)
- ✅ No lo guardes en lugares inseguros (evita localStorage si es posible, usa httpOnly cookies)
- ✅ Implementes refresh tokens para tokens de larga duración

### ¿Qué pasa si alguien roba mi token?
- Puede usarlo hasta que expire
- **Solución:** Tokens de corta duración (ej: 1 hora) + refresh tokens
- Auth0 maneja esto automáticamente

---

## 📱 Endpoints de prueba disponibles

### 1. Ver token (interfaz visual)
```
GET http://localhost:9000/auth0/token
```
Requiere: Estar autenticado
Respuesta: Página HTML con tu token

### 2. Debug JSON
```
GET http://localhost:9000/auth0/debug
```
Requiere: Estar autenticado
Respuesta: JSON con info del token

### 3. API protegida (ejemplo)
```
GET http://localhost:9000/api/v1/usuarios/me
Authorization: Bearer <token>
```
Requiere: Token JWT válido
Respuesta: JSON con info del usuario

---

## 🚀 Próximos pasos

### Para desarrollo local:
1. Inicia sesión en tu app
2. Ve a `/auth0/token` y copia tu token
3. Úsalo en Postman/curl para probar tus APIs

### Para un frontend SPA:
1. Instala Auth0 SDK en tu frontend
2. Implementa login con Auth0
3. Guarda el token que Auth0 te devuelve
4. Envía el token en cada petición a tu backend

### Para producción:
1. Cambia de HTTP a HTTPS
2. Configura CORS en Spring Boot
3. Usa refresh tokens para sesiones largas
4. Implementa rate limiting y otras medidas de seguridad

---

## 📚 Recursos útiles

- [JWT.io](https://jwt.io) - Decodificador de JWTs
- [Auth0 Docs](https://auth0.com/docs) - Documentación oficial
- [OAuth 2.0 Playground](https://www.oauth.com/playground/) - Probar flujos OAuth2

---

**¿Tienes más dudas?** Pregúntame sobre:
- Cómo implementar refresh tokens
- Cómo proteger endpoints específicos con roles/permisos
- Cómo integrar un frontend SPA (React, Angular, etc.)
- Cómo configurar CORS para APIs

