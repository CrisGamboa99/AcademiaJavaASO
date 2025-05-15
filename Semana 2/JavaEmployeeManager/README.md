# Sistema de Gestión de Empleados

Un sistema completo para la gestión de empleados desarrollado con Java Spring Boot y Vaadin Framework.

## Descripción

Esta aplicación proporciona una interfaz completa para gestionar información de empleados, con un backend RESTful desarrollado en Spring Boot y un frontend interactivo construido con Vaadin Framework.

## Características

### Backend (Spring Boot)

- API REST completa para la gestión de empleados
- Arquitectura en capas (Controller, Service, Repository)
- Validación de datos y manejo de excepciones
- Base de datos H2 en memoria (fácilmente configurable para bases de datos persistentes)
- Documentación API con Swagger/OpenAPI
- Pruebas unitarias y de integración con cobertura superior al 90%

### Frontend (Vaadin)

- Dashboard con estadísticas de empleados
- Visualización de distribución de empleados por departamento
- Gestión completa CRUD de empleados
- Filtrado y ordenación de datos
- Validación de formularios en tiempo real
- Diseño responsivo (compatible con móviles y escritorio)
- Tema personalizado con Lumo

## Capturas de Pantalla

### Dashboard
![Dashboard](screenshot-dashboard.png)

### Gestión de Empleados
![Empleados](screenshot-empleados.png)

## Requisitos

- Java 17 o superior
- Maven 3.6 o superior

## Instalación y Ejecución

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/tuusuario/empleados-app.git
   cd empleados-app
   ```

2. Compilar y ejecutar con Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. Acceder a la aplicación:
   - **Frontend Vaadin**: http://localhost:8000/
   - **Swagger API Docs**: http://localhost:8000/swagger-ui.html
   - **API REST**: http://localhost:8000/api/empleados

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── empleados/
│   │           └── api/
│   │               ├── config/                  # Configuración Spring y Swagger
│   │               ├── controller/              # Controladores REST
│   │               ├── dto/                     # Objetos de transferencia de datos
│   │               ├── exception/               # Excepciones personalizadas
│   │               ├── model/                   # Entidades JPA
│   │               ├── repository/              # Repositorios Spring Data
│   │               ├── service/                 # Servicios de negocio
│   │               └── ui/                      # Componentes Vaadin
│   │                   ├── config/              # Configuración del frontend
│   │                   ├── service/             # Servicios específicos para UI
│   │                   └── views/               # Vistas Vaadin
│   │                       ├── dashboard/       # Vista de dashboard
│   │                       ├── empleados/       # Vista de gestión de empleados
│   │                       └── layout/          # Layouts compartidos
│   └── resources/
│       ├── application.properties              # Configuración de la aplicación
│       └── META-INF/resources/
│           ├── frontend/                       # Recursos frontend
│           └── themes/                         # Temas personalizados
└── test/
    └── java/                                  # Pruebas unitarias e integración
```

## Tecnologías Utilizadas

- **Backend**: Spring Boot, Spring Data JPA, Spring Web, H2 Database, Lombok, Swagger/OpenAPI
- **Frontend**: Vaadin Flow, Lumo Theme
- **Testing**: JUnit, Mockito, Spring Test, JaCoCo

## Documentación

- [Documentación de la API (Swagger)](http://localhost:8000/swagger-ui.html)
- [Documentación de pruebas](TESTING.md)
- [Validación de pruebas](TEST_VALIDATION.md)

## Contribuir

1. Hacer un fork del repositorio
2. Crear una rama para tu característica: `git checkout -b feature/nueva-caracteristica`
3. Hacer commit de tus cambios: `git commit -m 'Añade nueva característica'`
4. Hacer push a la rama: `git push origin feature/nueva-caracteristica`
5. Enviar un pull request

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## Autor

Desarrollado como parte de un proyecto de demostración de arquitectura de aplicaciones Java modernas.