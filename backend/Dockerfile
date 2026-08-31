# Etapa 1: Compilacion
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia solo el pom.xml y descarga todas las dependencias
# Docker cachea esta capa: si pom.xml no cambia, no vuelve a descargar
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia el codigo fuente y compila
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecucion
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
