# -------- BUILD STAGE --------
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiamos solo lo necesario para cachear dependencias
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw -B -q dependency:go-offline

# Copiamos el código y compilamos
COPY src src
RUN ./mvnw -B -q clean package -DskipTests


# -------- RUNTIME STAGE --------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos el jar final
COPY --from=build /app/target/*.jar app.jar

# Puerto estándar Spring Boot
EXPOSE 8080

# Variables de entorno se inyectan desde Render/Railway
ENTRYPOINT ["java", "-jar", "app.jar"]
