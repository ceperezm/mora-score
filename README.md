# MoraScore

MoraScore es una plataforma para evaluar el riesgo de morosidad en el sistema financiero.
Utiliza datos del sistema financiero peruano como base para el entrenamiento del modelo
de Machine Learning y la clasificacion de clientes.

Integra un backend empresarial (Spring Boot) con un microservicio de Machine Learning (FastAPI)
para predecir la probabilidad de mora de un cliente a partir de sus datos financieros.

## Estructura del Repositorio

- `backend/` : API REST desarrollada en Spring Boot 3 + Java 21. Gestiona la logica de negocio,
  persistencia (PostgreSQL) y seguridad (JWT).
- `ml-service/` : Microservicio en Python (FastAPI) que ejecuta el modelo de Machine Learning
  entrenado con datos del sistema financiero peruano para predecir la probabilidad de mora.
- `db/` : Scripts de inicializacion y configuracion de la base de datos PostgreSQL.
- `frontend/` : Interfaz de usuario (en desarrollo).

## Contexto del Modelo ML

El modelo fue entrenado con variables propias del sistema financiero peruano:

- `zona` : Departamento del Peru (Lima, Arequipa, La Libertad, Piura, Cusco, etc.)
- `clasif_sbs` : Clasificacion crediticia segun la SBS (1 = Normal, 2 = CPP, 3 = Deficiente, etc.)
- `vivienda` : Tipo de vivienda (PROPIA, FAMILIAR)
- `nivel_educ` : Nivel educativo (PRIMARIA, SECUNDARIA, TECNICA, UNIVERSITARIA, POSTGRADO)
- `exp_sf` : Anos de experiencia en el sistema financiero
- `linea_sf` / `deuda_sf` : Linea de credito y deuda en el sistema financiero

## Requisitos

- Docker y Docker Compose
- Java 21 (para desarrollo local del backend)
- Python 3.10+ (para desarrollo local del ml-service)

## Instalacion y Despliegue con Docker

1. Clonar el repositorio.
2. Copiar `.env.example` a `.env` y configurar las variables de entorno (incluyendo la clave JWT).
3. Levantar los servicios:

   ```bash
   docker-compose up --build -d
   ```

4. La API REST estara disponible en: http://localhost:8080
5. El servicio de ML estara disponible en: http://localhost:8000

## Pruebas y Uso (Swagger)

Una vez levantado el backend, accede a la documentacion interactiva en:

**http://localhost:8080/swagger-ui.html**

Desde ahi puedes probar todos los endpoints: autenticacion, gestion de clientes,
datos financieros, evaluaciones y decisiones.

## Tecnologias

| Capa | Tecnologia |
|---|---|
| Backend | Spring Boot 3, Java 21, Spring Security, JWT |
| Machine Learning | FastAPI, scikit-learn, pandas, joblib |
| Base de datos | PostgreSQL 16 |
| Contenerizacion | Docker, Docker Compose |
| Documentacion API | SpringDoc OpenAPI (Swagger UI) |
