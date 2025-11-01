# Academic System - Backend

<em>Main backend service for the Academic Management System, built with Spring Boot 3.5 and integrated with Oracle Database.</em>

---

## Table of Contents

- [Academic System - Backend](#academic-system---backend)
  - [Table of Contents](#table-of-contents)
  - [Overview](#overview)
  - [Architecture](#architecture)
  - [Features](#features)
  - [Technology Stack](#technology-stack)
  - [Database Model](#database-model)
  - [API Endpoints](#api-endpoints)
  - [Project Structure](#project-structure)
  - [Ecosystem](#ecosystem)
  - [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Database Setup](#database-setup)
    - [Usage](#usage)
  - [License](#license)
  - [Contact](#contact)

---

## Overview

**SistemaAcademico-Backend** is the core RESTful API of the **Academic Management System** ecosystem, providing comprehensive business logic and data services.  

It exposes over **70 endpoints** for managing:
- 🎓 **Carreras** (Academic Programs)
- 📚 **Cursos** (Courses)
- 📅 **Ciclos** (Academic Cycles)
- 👨‍🎓 **Alumnos** (Students)
- 👨‍🏫 **Profesores** (Professors)
- 📋 **Grupos** (Course Groups/Sections)
- ✍️ **Matrículas** (Enrollments)
- 👤 **Usuarios** (Users & Authentication)
- 🔗 **Carrera-Curso** (Career-Course relationships)

This backend communicates with:
- The Android mobile app: [gestion-academica-app](https://github.com/isaacmendezr/gestion-academica-app)  
- The web platform: [gestion-academica-web](https://github.com/isaacmendezr/gestion-academica-web)

---

## Architecture

- **Framework:** Spring Boot 3.5.0
- **Language:** Java 21
- **Database:** Oracle Database XE (via JDBC direct access)
- **Build Tool:** Maven
- **Design Pattern:** Layered architecture with MVC pattern
  - **Controllers:** RESTful endpoints (`@RestController`)
  - **Services:** Business logic layer (`@Service`)
  - **Database Access:** Direct JDBC with `CallableStatement` for stored procedures
- **Data Mapping:** POJOs with Lombok annotations
- **API Style:** RESTful with JSON request/response
- **Error Handling:** Centralized exception handling with `@RestControllerAdvice`
- **Logging:** SLF4J with detailed request/response logging

**Key Architectural Decisions:**
- Uses **stored procedures** in Oracle Database for complex business logic
- Direct JDBC access (no JPA/Hibernate) for maximum control
- `REF_CURSOR` returns for query results
- Proactive validation before destructive operations

---

## Features

| Category | Description |
| :-------- | :----------- |
| ⚙️ **Architecture** | Clean layered architecture: Controllers → Services → Oracle Stored Procedures |
| 🔌 **Integration** | 70+ REST endpoints with CORS enabled for web and mobile clients |
| 🗄️ **Database** | Comprehensive Oracle schema with 9 tables, 70+ stored procedures, and triggers |
| 🧩 **Compatibility** | Fully compatible with **gestion-academica-app (Android)** and **gestion-academica-web (Vue 3)** |
| 🛡️ **Validation** | Multi-layer validation: triggers in DB + service layer checks + controller validations |
| 📦 **Code Quality** | Uses Lombok for boilerplate reduction, consistent error handling, comprehensive logging |
| 💾 **Database Support** | Complete SQL script with schema, stored procedures, triggers, and test data |
| 🔍 **DTOs** | Specialized DTOs for complex queries with joins (CursoDto, GrupoDto, MatriculaAlumnoDto, etc.) |
| 🔄 **Transaction Management** | Atomic operations with rollback support for critical operations |
| 📊 **Connection Pooling** | HikariCP configuration for optimal database performance |

---

## Technology Stack

**Core Dependencies:**
- **Spring Boot Starter Web** - REST API endpoints
- **Spring Boot Starter Data JDBC** - Database connectivity
- **Spring Boot Starter Validation** - Request validation
- **Oracle JDBC Driver** (`ojdbc11`) - Oracle database connectivity
- **Lombok** - Code generation and boilerplate reduction
- **Spring Boot DevTools** - Development utilities
- **SLF4J** - Logging framework

**Configuration:**
- **Server Port:** 8080
- **Database:** `jdbc:oracle:thin:@localhost:1521:XE`
- **Connection Pool:** HikariCP (max: 10, min idle: 5)
- **CORS:** Enabled for `http://localhost:5173` and all origins
- **Logging:** DEBUG level for application, file and console output

---

## Database Model

**9 Core Tables:**

1. **Carrera** - Academic programs (id, codigo, nombre, titulo)
2. **Curso** - Courses (id, codigo, nombre, creditos, horas_semanales)
3. **Ciclo** - Academic cycles (id, anio, numero, fecha_inicio, fecha_fin, estado)
4. **Carrera_Curso** - Career-Course-Cycle relationship (M:N with cycle)
5. **Profesor** - Professors (id, cedula, nombre, telefono, email)
6. **Alumno** - Students (id, cedula, nombre, telefono, email, fecha_nacimiento, pk_carrera)
7. **Grupo** - Course sections (id, pk_carrera_curso, numero_grupo, horario, pk_profesor)
8. **Matricula** - Enrollments (id, pk_alumno, pk_grupo, nota)
9. **Usuario** - Users for authentication (id, cedula, clave, tipo)

**Key Relationships:**
- Alumno → Carrera (Many-to-One)
- Grupo → Carrera_Curso, Profesor (Many-to-One)
- Matricula → Alumno, Grupo (Many-to-One)
- Carrera_Curso → Carrera, Curso, Ciclo (Many-to-One)

**Business Rules Enforced:**
- Only one active cycle at a time
- Students can only enroll in courses from their career
- No duplicate enrollments in the same course
- Grades must be between 0-100
- Cascading validations before deletions

---

## API Endpoints

**Base URL:** `http://localhost:8080/api`

| Module | Endpoint Base | Count | Key Operations |
|--------|---------------|-------|----------------|
| 🎓 Carreras | `/api/carreras` | 10 | CRUD, search by code/name, manage courses |
| 📚 Cursos | `/api/cursos` | 9 | CRUD, search by code/name/career/cycle |
| 📅 Ciclos | `/api/ciclos` | 7 | CRUD, search by year/id, activate cycle |
| 🔗 Carrera-Curso | `/api/carrera-curso` | 5 | Link courses to careers, manage by cycle |
| 👨‍🏫 Profesores | `/api/profesores` | 8 | CRUD, search by cedula/name |
| 👨‍🎓 Alumnos | `/api/alumnos` | 10 | CRUD, search by cedula/name/career, filter by cycle |
| 📋 Grupos | `/api/grupos` | 10 | CRUD, search by career/course/professor/cycle |
| ✍️ Matrículas | `/api/matricular` | 9 | CRUD, list by student/group/cycle, change group |
| 👤 Usuarios | `/api/usuarios` | 6 | CRUD, login, search by cedula |

**Example Endpoints:**
```
POST   /api/usuarios/login
GET    /api/alumnos/listar
GET    /api/cursos/buscarCursosPorCarreraYCiclo/{idCarrera}/{idCiclo}
GET    /api/grupos/buscarGruposPorProfesorEnCicloActivo/{cedula}
GET    /api/matricular/listarMatriculasPorAlumnoYCiclo/{idAlumno}/{idCiclo}
POST   /api/ciclos/activarCiclo/{id}
DELETE /api/usuarios/eliminar/{id}  (cascades to Alumno/Profesor)
```

---

## Project Structure

```sh
SistemaAcademico-Backend/
├── pom.xml                          # Maven configuration
├── mvnw, mvnw.cmd                   # Maven wrapper
├── logs/                            # Application logs
└── src/
    └── main/
        ├── java/org/example/sistemaacademico/
        │   ├── SistemaAcademicoApplication.java  # Main Spring Boot app
        │   ├── config/
        │   │   └── WebConfig.java                # CORS configuration
        │   ├── controller/                       # 9 REST Controllers
        │   │   ├── AlumnoController.java
        │   │   ├── CarreraController.java
        │   │   ├── CarreraCursoController.java
        │   │   ├── CicloController.java
        │   │   ├── CursoController.java
        │   │   ├── GrupoController.java
        │   │   ├── MatriculaController.java
        │   │   ├── ProfesorController.java
        │   │   └── UsuarioController.java
        │   ├── data/                             # 9 Service classes
        │   │   ├── AlumnoService.java
        │   │   ├── CarreraService.java
        │   │   ├── CarreraCursoService.java
        │   │   ├── CicloService.java
        │   │   ├── CursoService.java
        │   │   ├── GrupoService.java
        │   │   ├── MatriculaService.java
        │   │   ├── ProfesorService.java
        │   │   └── UsuarioService.java
        │   ├── database/                         # Exception handling
        │   │   ├── GlobalException.java          # Business errors (400)
        │   │   ├── NoDataException.java          # Not found (404)
        │   │   └── GlobalExceptionHandler.java   # Centralized handler
        │   └── logic/                            # Domain models
        │       ├── Alumno.java
        │       ├── Carrera.java
        │       ├── CarreraCurso.java
        │       ├── Ciclo.java
        │       ├── Curso.java
        │       ├── Grupo.java
        │       ├── Matricula.java
        │       ├── Profesor.java
        │       ├── Usuario.java
        │       └── dto/                          # Data Transfer Objects
        │           ├── CursoDto.java
        │           ├── GrupoDto.java
        │           ├── GrupoProfesorDto.java
        │           └── MatriculaAlumnoDto.java
        └── resources/
            ├── application.properties            # App configuration
            ├── banner.txt                        # Custom Spring Boot banner
            └── SistemaAcademico.sql              # Complete DB script
```

---

## Ecosystem

The backend is part of an integrated application ecosystem:

| Component | Description | Repository |
|------------|-------------|-------------|
| 📱 **Academic Management App** | Android mobile app developed in Kotlin using MVVM architecture and Hilt dependency injection. | [github.com/isaacmendezr/gestion-academica-app](https://github.com/isaacmendezr/gestion-academica-app) |
| 💻 **Academic Management Web** | Web application developed with Vue 3, Vuetify, Pinia, and Axios. | [github.com/isaacmendezr/gestion-academica-web](https://github.com/isaacmendezr/gestion-academica-web) |

---

## Getting Started

### Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **Oracle Database XE** (Express Edition) or higher
- Network access to Oracle Database on `localhost:1521`

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/isaacmendezr/SistemaAcademico-Backend
   cd SistemaAcademico-Backend
   ```

2. Install dependencies:
   ```sh
   mvn clean install
   ```

3. Configure the database connection in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
   spring.datasource.username=system
   spring.datasource.password=root
   spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
   ```

### Database Setup

1. Connect to your Oracle Database with SQL*Plus or SQL Developer

2. Execute the complete database script:
   ```sh
   sqlplus system/root@localhost:1521/XE
   SQL> @src/main/resources/SistemaAcademico.sql
   ```

   This script will:
   - Drop existing tables and objects (if any)
   - Create 9 tables with relationships
   - Create 70+ stored procedures and functions
   - Set up triggers for data validation
   - Insert sample test data

3. Verify the installation:
   ```sql
   SELECT table_name FROM user_tables;
   -- Should show: CARRERA, CURSO, CICLO, CARRERA_CURSO, PROFESOR, ALUMNO, GRUPO, MATRICULA, USUARIO
   ```

### Usage

1. Run the backend locally:
   ```sh
   mvn spring-boot:run
   ```

2. The API will be available at:
   ```
   http://localhost:8080/api
   ```

3. Test the API with curl:
   ```sh
   # List all careers
   curl http://localhost:8080/api/carreras/listar

   # List all courses
   curl http://localhost:8080/api/cursos/listar

   # Login example
   curl -X POST "http://localhost:8080/api/usuarios/login?cedula=123456789&clave=password123"
   ```

4. View logs:
   ```sh
   tail -f logs/sistema-academico.log
   ```

**Development Mode:**
- Hot reload enabled with Spring Boot DevTools
- Debug logging for `org.example.sistemaacademico` package
- CORS enabled for frontend development on `http://localhost:5173`

**Production Considerations:**
- Update database credentials in `application.properties`
- Configure proper CORS origins in `WebConfig.java`
- Adjust logging levels for production
- Set up proper connection pooling parameters
- Enable HTTPS for secure communication

---

## License

This project is part of an academic management system ecosystem.

## Contact

**Developer:** Isaac Méndez  
**Repository:** [github.com/isaacmendezr/SistemaAcademico-Backend](https://github.com/isaacmendezr/SistemaAcademico-Backend)

---

**Note:** This backend is designed to work in conjunction with the [Android app](https://github.com/isaacmendezr/gestion-academica-app) and [Web platform](https://github.com/isaacmendezr/gestion-academica-web). Make sure to configure all three components for a complete system.
