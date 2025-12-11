# Pruebas de Carga con k6

Este directorio contiene los scripts de pruebas de carga usando [k6](https://k6.io/).

## ¿Qué es k6?

k6 es una herramienta de código abierto para pruebas de carga y rendimiento, diseñada para desarrolladores y equipos de DevOps. Utiliza JavaScript (ES6) para escribir los scripts de prueba.

## Instalación de k6

### Linux (Ubuntu/Debian)

```bash
# Opción 1: Usando apt (recomendado)
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys C5AD17C747E3415A3642D57D77C6C491D6AC1D69
echo "deb https://dl.k6.io/deb stable main" | sudo tee /etc/apt/sources.list.d/k6.list
sudo apt-get update
sudo apt-get install k6

# Opción 2: Usando snap
sudo snap install k6
```

### macOS

```bash
# Usando Homebrew
brew install k6
```

### Windows

```bash
# Usando Chocolatey
choco install k6

# O descargar el binario desde: https://github.com/grafana/k6/releases
```

### Verificar instalación

```bash
k6 version
```

## Scripts Disponibles

### `load-test.js`

Script para crear usuarios de tipo CONCESIONARIA y sus concesionarias asociadas.

**Patrón de creación:**
- **Usuarios:**
  - Email: `user_{indice}@compraauto.com`
  - Password: `uservendedor{indice}`
  - Nombre: `user`
  - Apellido: `vendedor {indice}`
  - Tipo: `CONCESIONARIA`

- **Concesionarias:**
  - Nombre: `Concesionaria_{indice}`
  - CUIT: `20-12345678-{indice}`
  - Asociada al usuario con el mismo índice

## Uso

### Ejecución básica (valores por defecto)

```bash
cd k6-tests
k6 run load-test.js
```

Esto creará 10 usuarios y 10 concesionarias usando las credenciales por defecto.

### Ejecución con variables de entorno personalizadas

```bash
BASE_URL=http://localhost:8080/api/1/compra-tu-auto \
ADMIN_EMAIL=admin@compraauto.com \
ADMIN_PASSWORD=admin123 \
ITERATIONS=10 \
k6 run load-test.js
```

### Variables de entorno disponibles

| Variable | Descripción | Valor por defecto |
|----------|-------------|-------------------|
| `BASE_URL` | URL base del API backend | `http://localhost:8080/api/1/compra-tu-auto` |
| `ADMIN_EMAIL` | Email del usuario ADMIN para autenticación | `admin@compraauto.com` |
| `ADMIN_PASSWORD` | Password del usuario ADMIN | `admin123` |
| `VUS` | Número de usuarios virtuales (concurrencia) | `1` |
| `ITERATIONS` | Número de usuarios/concesionarias a crear | `10` |

### Ejemplos de uso

#### Crear 20 usuarios y concesionarias

```bash
ITERATIONS=20 k6 run load-test.js
```

#### Usar un servidor remoto

```bash
BASE_URL=http://192.168.1.100:8080/api/1/compra-tu-auto \
ADMIN_EMAIL=admin@compraauto.com \
ADMIN_PASSWORD=miPassword123 \
k6 run load-test.js
```

#### Crear con mayor concurrencia (3 usuarios virtuales)

```bash
VUS=3 ITERATIONS=10 k6 run load-test.js
```

## Requisitos Previos

1. **Backend ejecutándose**: El servidor backend debe estar corriendo y accesible en la URL especificada.

2. **Usuario ADMIN existente**: Debe existir un usuario ADMIN en la base de datos con las credenciales que se usarán para autenticarse.

   Si no tienes un usuario ADMIN, puedes crearlo manualmente o usando el script de migración de Flyway.

3. **k6 instalado**: Ver sección de instalación arriba.

## Flujo de Ejecución

1. **Autenticación**: El script hace login como ADMIN y obtiene un token JWT.

2. **Creación secuencial**: Para cada índice (0 a ITERATIONS-1):
   - Crea un usuario de tipo CONCESIONARIA
   - Obtiene el ID del usuario creado
   - Crea una concesionaria asociada a ese usuario
   - Registra el resultado en consola

3. **Métricas**: k6 recopila métricas de rendimiento:
   - Tiempo de respuesta promedio
   - Tiempo de respuesta P95 y P99
   - Tasa de errores
   - Total de requests

## Salida del Script

El script mostrará en consola:

- Progreso de creación: `✓ Created usuario X (ID: Y) and Concesionaria_X`
- Errores si ocurren: `Failed to create usuario X: status - body`
- Resumen al final con métricas de rendimiento

### Ejemplo de salida

```
✓ Created usuario 0 (ID: 15) and Concesionaria_0
✓ Created usuario 1 (ID: 16) and Concesionaria_1
✓ Created usuario 2 (ID: 17) and Concesionaria_2
...

  ====================
  Test Summary
  ====================
  Total Requests: 21
  Failed Requests: 0.00%
  Avg Response Time: 245.32ms
  Min Response Time: 120.45ms
  Max Response Time: 450.67ms
  P95 Response Time: 380.12ms
  P99 Response Time: 420.34ms
  ====================
```

## Troubleshooting

### Error: "Login failed"

- Verifica que el usuario ADMIN existe en la base de datos
- Verifica que las credenciales (`ADMIN_EMAIL` y `ADMIN_PASSWORD`) son correctas
- Verifica que el backend está ejecutándose y accesible

### Error: "Failed to create usuario"

- Verifica que no existe un usuario con el mismo email
- Verifica que el backend está respondiendo correctamente
- Revisa los logs del backend para más detalles

### Error: "Failed to create concesionaria"

- Verifica que el token de autenticación es válido
- Verifica que el usuario ADMIN tiene permisos
- Verifica que no existe una concesionaria con el mismo CUIT

### Error: "k6: command not found"

- Verifica que k6 está instalado: `k6 version`
- Si está instalado, verifica que está en el PATH

## Recursos Adicionales

- [Documentación oficial de k6](https://k6.io/docs/)
- [Guía de inicio rápido de k6](https://k6.io/docs/getting-started/running-k6/)
- [Referencia de API de k6](https://k6.io/docs/javascript-api/)

## Notas

- El script crea usuarios y concesionarias de forma secuencial (no en paralelo) para evitar conflictos.
- Se incluyen pausas (`sleep`) entre requests para no sobrecargar el servidor.
- Los umbrales de rendimiento están configurados para fallar si más del 10% de las requests fallan o si el P95 es mayor a 2 segundos.

