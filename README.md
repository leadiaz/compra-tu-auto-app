# compra-tu-auto-app
Practicas de Desarrollo de Software | Universidad Nacional de Quilmes 2025

## Estructura del Proyecto

Este proyecto contiene la configuración de Docker Compose para orquestar los servicios de la aplicación Compra Tu Auto.

### Directorios

- `k6-tests/`: Scripts de pruebas de carga usando k6. Ver [k6-tests/README.md](k6-tests/README.md) para más información.
- `grafana/`: Configuración de dashboards y provisioning para Grafana.
- `doc/`: Documentación del proyecto.

## Pruebas de Carga

Para ejecutar pruebas de carga y crear usuarios y concesionarias de prueba, consulta la documentación en [k6-tests/README.md](k6-tests/README.md).

Ejemplo rápido:
```bash
cd k6-tests
k6 run load-test.js
```
