# 🎓 InscripcionesApp — Sistema de Inscripción de Cursos

API REST construida con **Spring Boot 3** para gestionar la inscripción de estudiantes en cursos virtuales. Desplegada automáticamente en **AWS EC2** mediante **GitHub Actions** con imagen publicada en **Docker Hub**.

---

## 📋 Tabla de Contenidos
- [Arquitectura](#arquitectura)
- [Endpoints](#endpoints)
- [Funcionalidades adicionales](#funcionalidades-adicionales)
- [Configuración Oracle Cloud](#configuración-oracle-cloud)
- [Ejecución local](#ejecución-local)
- [Pipeline CI/CD](#pipeline-cicd)
- [Secrets requeridos](#secrets-requeridos)

---

## 🏗️ Arquitectura

```
inscripciones-app/
├── controller/         # REST Controllers (CourseController, EnrollmentController, StudentController)
├── service/            # Business logic (CourseService, EnrollmentService, StudentService)
├── repository/         # JPA Repositories (Oracle Cloud)
├── model/              # JPA Entities (Course, Student, Enrollment)
├── dto/                # Request/Response DTOs
├── exception/          # Custom exceptions + GlobalExceptionHandler
└── config/             # OpenAPI, DataInitializer
```

**Stack:** Java 17 · Spring Boot 3.2 · Spring Data JPA · Oracle Cloud (OJDBC11) · Lombok · Springdoc OpenAPI

---

## 🔌 Endpoints

### Cursos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v1/courses` | **Listar cursos disponibles** (nombre, instructor, duración, costo) |
| `GET` | `/api/v1/courses/{id}` | Obtener curso por ID |
| `POST` | `/api/v1/courses` | **Agregar nuevo curso** |
| `PUT` | `/api/v1/courses/{id}` | Actualizar curso |
| `PATCH` | `/api/v1/courses/{id}/toggle-availability` | Activar/desactivar curso |
| `GET` | `/api/v1/courses/category/{category}` | Filtrar por categoría |
| `GET` | `/api/v1/courses/search?keyword=...` | Buscar por palabra clave |
| `GET` | `/api/v1/courses/filter/price?min=...&max=...` | Filtrar por rango de precio |
| `GET` | `/api/v1/courses/categories` | Listar todas las categorías |

### Inscripciones
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/v1/enrollments` | **Inscribir estudiante** en uno o más cursos |
| `GET` | `/api/v1/enrollments/{id}` | Obtener detalle de inscripción |
| `GET` | `/api/v1/enrollments/student/{studentId}` | Historial de inscripciones del estudiante |
| `PATCH` | `/api/v1/enrollments/{id}/cancel` | Cancelar inscripción |

### Estudiantes
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/v1/students` | Registrar estudiante |
| `GET` | `/api/v1/students/{id}` | Obtener estudiante por ID |

### Documentación
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs JSON: `http://localhost:8080/api-docs`
- Health Check: `http://localhost:8080/actuator/health`

---

## ✨ Funcionalidades Adicionales

1. **Descuentos automáticos por volumen**  
   - 2 cursos → 5% de descuento  
   - 3 o más cursos → 10% de descuento

2. **Resumen de inscripción detallado**  
   Incluye subtotal, porcentaje de descuento, monto descontado y total final a pagar.

3. **Validación de capacidad máxima**  
   Si un curso tiene `maxStudents`, se valida que no esté lleno antes de inscribir.

4. **Múltiples métodos de pago**  
   `CREDIT_CARD`, `DEBIT_CARD`, `BANK_TRANSFER`, `PAYPAL`, `CASH`

5. **Búsqueda y filtrado avanzado de cursos**  
   Por categoría, palabra clave y rango de precio.

6. **Datos demo pre-cargados**  
   6 cursos de ejemplo se insertan automáticamente si la base de datos está vacía.

---

## 🗄️ Configuración Oracle Cloud

```properties
spring.datasource.url=jdbc:oracle:thin:@(description=...(host=adb.sa-saopaulo-1.oraclecloud.com)...)
spring.datasource.username=ADMIN
spring.datasource.password=YourPassword123!
```

Las tablas se crean automáticamente con `spring.jpa.hibernate.ddl-auto=update`.

---

## 🚀 Ejecución Local

### Con Maven
```bash
# Configurar variables de entorno
export DB_URL="jdbc:oracle:thin:@..."
export DB_USERNAME="ADMIN"
export DB_PASSWORD="YourPassword123!"

mvn spring-boot:run
```

### Con Docker
```bash
docker build -t inscripciones-app .
docker run -p 8080:8080 \
  -e DB_URL="..." \
  -e DB_USERNAME="ADMIN" \
  -e DB_PASSWORD="..." \
  inscripciones-app
```

### Con Docker Compose
```bash
# Crear archivo .env con DB_URL, DB_USERNAME, DB_PASSWORD
docker-compose up -d
```

---

## ⚙️ Pipeline CI/CD

```
Push a main
    │
    ▼
[Job 1] Build & Test (Maven + H2 in-memory)
    │
    ▼
[Job 2] Build Docker image → Push a Docker Hub
    │         yourdockerhub/inscripciones-app:latest
    │         yourdockerhub/inscripciones-app:<sha>
    ▼
[Job 3] Deploy en EC2 vía SSH
    │  docker pull → docker stop → docker run
    ▼
Health Check: GET /actuator/health
```

---

## 🔐 Secrets requeridos en GitHub

| Secret | Descripción |
|--------|-------------|
| `DOCKERHUB_USERNAME` | Tu usuario de Docker Hub |
| `DOCKERHUB_TOKEN` | Access token de Docker Hub |
| `EC2_HOST` | IP pública de la instancia EC2 |
| `EC2_USER` | Usuario SSH (ej: `ec2-user` o `ubuntu`) |
| `EC2_SSH_KEY` | Contenido del archivo `.pem` |
| `DB_URL` | JDBC URL de Oracle Cloud |
| `DB_USERNAME` | Usuario de Oracle (ej: `ADMIN`) |
| `DB_PASSWORD` | Contraseña de Oracle |

---

## 📦 Ejemplo: Inscribir en múltiples cursos

**Request:**
```json
POST /api/v1/enrollments
{
  "studentId": 1,
  "courseIds": [1, 2, 3],
  "paymentMethod": "CREDIT_CARD"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Enrollment completed successfully",
  "data": {
    "enrollmentId": 1,
    "studentName": "Juan Pérez",
    "courses": [
      { "courseName": "Java Spring Boot", "cost": 49.99 },
      { "courseName": "React.js Avanzado", "cost": 59.99 },
      { "courseName": "Python para Data Science", "cost": 69.99 }
    ],
    "subtotal": 179.97,
    "discountPercentage": 10.00,
    "discountAmount": 18.00,
    "totalAmount": 161.97,
    "paymentMethod": "CREDIT_CARD",
    "status": "CONFIRMED"
  }
}
```
