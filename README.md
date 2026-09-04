# MoraScore

MoraScore es una plataforma para evaluar el riesgo de morosidad en el sistema financiero.
Utiliza datos del sistema financiero peruano como base para el entrenamiento del modelo
de Machine Learning y la clasificación de clientes.

Integra un backend empresarial (Spring Boot) con un microservicio de Machine Learning (FastAPI)
para predecir la probabilidad de mora de un cliente a partir de sus datos financieros. El versionamiento y registro de los modelos ahora se gestiona de forma profesional a través de **MLflow**.

## Estructura del Repositorio

- `backend/` : API REST desarrollada en Spring Boot 3 + Java 21. Gestiona la lógica de negocio,
  persistencia (PostgreSQL) y seguridad (JWT).
- `ml-service/` : Microservicio en Python (FastAPI) que ejecuta el modelo de Machine Learning.
  Se integra con un Tracking Server de MLflow local para cargar dinámicamente el modelo en producción.
- `db/` : Scripts de inicialización y configuración de la base de datos PostgreSQL.
- `frontend/` : Interfaz de usuario (en desarrollo).

## Contexto del Modelo ML

El modelo entrena descargando los datos de forma automática desde Kaggle (`luishcaldernb/morosidad`). Algunas de las variables del sistema financiero peruano incluyen:

- `zona` : Departamento del Perú (Lima, Arequipa, La Libertad, Piura, Cusco, etc.)
- `clasif_sbs` : Clasificación crediticia según la SBS (1 = Normal, 2 = CPP, 3 = Deficiente, etc.)
- `vivienda` : Tipo de vivienda (PROPIA, FAMILIAR, ALQUILADA)
- `nivel_educ` : Nivel educativo (PRIMARIA, SECUNDARIA, TECNICA, UNIVERSITARIA, POSTGRADO)
- `exp_sf` : Años de experiencia en el sistema financiero
- `linea_sf` / `deuda_sf` : Línea de crédito y deuda en el sistema financiero

## Requisitos

- Docker y Docker Compose
- Java 21 (para desarrollo local del backend)
- Python 3.10+ (para desarrollo local del ml-service y MLflow)

## Instalación y Despliegue (MLflow + Docker)

El flujo ha sido completamente automatizado. Al ejecutar Docker Compose, se levantarán secuencialmente: la base de datos, el servidor de MLflow, un contenedor temporal que entrena el modelo y lo promueve, el servicio predictivo (FastAPI) y finalmente el backend en Spring Boot.

1. **Clonar el repositorio.**

2. **Configurar entorno:**
   Copiar el archivo `.env.example` a `.env` y configurar las variables de entorno (incluyendo la clave JWT).

3. **Levantar todos los servicios automáticamente:**
   ```bash
   docker-compose up --build -d
   ```
   > **Nota:** La primera vez que levantes el entorno, el contenedor `morascore-trainer` descargará los datos de Kaggle y entrenará el modelo. El servicio `ml-service` esperará automáticamente a que el modelo esté listo en MLflow antes de arrancar.

4. La API REST del backend estará disponible en: **http://localhost:8080**
5. El servicio de ML estará disponible en: **[http://localhost:8000](http://localhost:8000/predict)**
6. La interfaz de MLflow estará disponible en: **http://localhost:5000**

## Pruebas y Uso (Swagger)

Una vez levantado el backend, accede a la documentación interactiva en:

**http://localhost:8080/swagger-ui.html**

Desde ahí puedes probar todos los endpoints de integración.

## Tecnologías

| Capa | Tecnología |
|---|---|
| Backend | Spring Boot 3, Java 21, Spring Security, JWT |
| Machine Learning | FastAPI, MLflow, scikit-learn, pandas |
| Base de datos | PostgreSQL 16 |
| Contenedores | Docker, Docker Compose |
| Documentación API | SpringDoc OpenAPI (Swagger UI) |
