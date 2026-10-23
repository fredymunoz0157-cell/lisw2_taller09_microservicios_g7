# Taller 09: Comunicación Síncrona y Asíncrona de Microservicios (Banco de Preguntas Saber PRO)
**Universidad del Cauca — Laboratorio de Ingeniería de Software II**

Este monorepo contiene la solución completa en **Java 17 + Spring Boot 3 + RabbitMQ** lista para abrir y ejecutar en **Visual Studio Code**, integrando tanto los lineamientos de los documentos del taller (`lisw2_guia_paso_a_paso_taller09-.pdf` y `lisw2_taller09_ComunicacionesSincronasAsincronas.pdf`) como **todas las clases, enumeraciones, relaciones y métodos del diagrama UML (`plantuml-diagram (2).svg`) y de las Figuras 1 y 2**.

## Arquitectura de Microservicios y Puertos

| Microservicio | Puerto | Responsabilidad Principal | Clases del UML / Figura 1 incluidas |
| :--- | :--- | :--- | :--- |
| **user-service** | `:8081` | Autenticación síncrona (`POST /auth/login`), verificación de roles y auditoría de eventos. | `Usuario`, `Rol`, `RegistroAuditoria`, `LoginRequestDTO`, `LoginResponseDTO`, `UserDTO` |
| **validation-service** | `:8082` | Validación síncrona (`POST /validate`) de estructura, opciones, clave única y expresiones prohibidas. | `ValidadorEstructural`, `ValidationRule`, `Pregunta`, `OpcionRespuesta`, `ValidationRequestDTO`, `ValidationResultDTO` |
| **question-service** | `:8083` | Creación síncrona (`POST /questions`), corrección (`PUT /questions/{id}`) y envío asíncrono a revisión (`POST /questions/{id}/submit` -> `202 Accepted`). | `Pregunta`, `OpcionRespuesta`, `HistorialModificacion`, `EstadoPregunta`, `NivelDificultad`, `SubmitQuestionCommand`, `ValidationClient`, `CommandPublisher` |
| **lifecycle-service** | `:8084` | Autoridad única sobre estados y transiciones (`@RabbitListener` + `GET /lifecycle/{id}` + `POST /lifecycle/transition`). | `QuestionState`, `OperationResult`, `QuestionStatus`, `OperationStatus`, `HistorialModificacion`, `CommandListener` |
| **review-service** | `:8085` | Asignación de revisores (`POST /reviews/assign`), evaluación de pares (`POST /reviews/evaluate`) y publicación del evento `ReviewerAssigned`. | `Revision`, `FormatoEvaluacion`, `ReviewAssignment`, `Review`, `ReviewResult`, `AssignmentStatus`, `EventPublisher` |
| **notification-service** | `:8086` | Consumo asíncrono (`@RabbitListener`) del evento `ReviewerAssigned` y envío simulado de correo sin bloquear el flujo principal. | `Notification`, `NotificationType`, `NotificationStatus`, `NotificationListener`, `EmailService` |
| **simulacro-service** | `:8087` | Implementa el 4.° cluster del diagrama UML (*Simulacros y Seguimiento Académico*) y el cliente `DesktopClient`. | `Simulacro`, `IntentoSimulacro`, `RespuestaEstudiante`, `ReporteAcademico`, `DesktopClient` |

## Ejecución Rápida en VS Code

1. **Levantar RabbitMQ con Docker Compose**:
   ```bash
   docker compose up -d
   ```
   - Puerto AMQP: `5672`
   - Consola de administración web: `http://localhost:15672` (`guest` / `guest`)

2. **Abrir en Visual Studio Code**:
   - Abra la carpeta raíz `lisw2_taller09_microservicios_gX` o el archivo `saberpro.code-workspace` en VS Code.
   - Asegúrese de tener instalada la extensión **Extension Pack for Java** y **Spring Boot Extension Pack**.
   - Vaya al panel **Run and Debug (`Ctrl+Shift+D`)** y seleccione **"Iniciar Todos los Microservicios SaberPRO"** (o inicie cada microservicio desde el panel **Spring Boot Dashboard**).

3. **Ejecutar Pruebas en Postman**:
   - Importe el archivo `postman/SaberPro_Microservices.postman_collection.json` en Postman o en la extensión de Postman para VS Code.
