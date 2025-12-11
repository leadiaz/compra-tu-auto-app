# compra-tu-auto-app
Practicas de Desarrollo de Software | Universidad Nacional de Quilmes 2025

Aplicación web para la compra y venta de autos, desarrollada como proyecto académico. El sistema permite a los usuarios comprar autos, a las concesionarias publicar ofertas y a los administradores gestionar el sistema completo.

## Estructura del Proyecto

Este proyecto contiene la configuración de Docker Compose para orquestar los servicios de la aplicación Compra Tu Auto.

### Directorios

- `k6-tests/`: Scripts de pruebas de carga usando k6. Ver [k6-tests/README.md](k6-tests/README.md) para más información.
- `grafana/`: Configuración de dashboards y provisioning para Grafana.
- `doc/`: Documentación del proyecto.

## Proyectos

### Backend

**Repositorio**: [compra-tu-auto-app-backend](https://github.com/leadiaz/compra-tu-auto-app-backend)

API REST desarrollada con Spring Boot que proporciona los endpoints para la gestión de usuarios, autos, ofertas, compras, reseñas y reportes.

#### Tecnologías

- **Java 17**: Lenguaje de programación
- **Spring Boot 3.5.5**: Framework principal
- **Spring Security**: Autenticación y autorización
- **Spring Data JPA**: Persistencia de datos
- **PostgreSQL**: Base de datos principal (producción)
- **H2**: Base de datos en memoria (desarrollo y testing)
- **Flyway**: Migraciones de base de datos

#### Librerías Principales

- **JWT (jjwt 0.12.3)**: Autenticación mediante tokens JWT
- **MapStruct 1.5.5**: Mapeo entre entidades y DTOs
- **Lombok**: Reducción de boilerplate code
- **Springdoc OpenAPI 2.7.0**: Documentación automática de la API (Swagger)
- **Spring Boot Actuator**: Métricas y monitoreo
- **Micrometer Prometheus**: Exportación de métricas para Prometheus
- **Spring Boot Validation**: Validación de datos de entrada
- **Spring Security Test**: Testing de seguridad

#### Características

- Autenticación JWT con roles (COMPRADOR, CONCESIONARIA, ADMIN)
- API REST documentada con Swagger/OpenAPI
- Migraciones de base de datos versionadas con Flyway
- Métricas exportadas a Prometheus
- Tests unitarios e integración con JUnit y Mockito

### Frontend

**Repositorio**: [compra-tu-auto-app-frontend](https://github.com/leadiaz/compra-tu-auto-app-frontend)

Aplicación web desarrollada con Angular que proporciona la interfaz de usuario para compradores, concesionarias y administradores.

#### Tecnologías

- **Angular 20.2.0**: Framework principal
- **TypeScript 5.9.2**: Lenguaje de programación
- **RxJS 7.8.0**: Programación reactiva
- **Express 5.1.0**: Servidor para Server-Side Rendering (SSR)

#### Librerías Principales

- **Angular Router**: Navegación y routing
- **Angular Forms**: Manejo de formularios reactivos
- **Angular SSR**: Server-Side Rendering para mejor SEO
- **Cypress 13.13.0**: Testing end-to-end
- **Karma & Jasmine**: Testing unitario

#### Características

- Interfaz responsive con componentes reutilizables
- Autenticación con JWT almacenado en localStorage
- Guards para protección de rutas según roles
- Servicios para comunicación con la API
- Testing E2E con Cypress
- Server-Side Rendering (SSR) habilitado

## Pruebas de Carga

Para ejecutar pruebas de carga y crear usuarios y concesionarias de prueba, consulta la documentación en [k6-tests/README.md](k6-tests/README.md).

Ejemplo rápido:
```bash
cd k6-tests
k6 run load-test.js
```
