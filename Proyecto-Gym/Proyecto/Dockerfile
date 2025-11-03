FROM maven:3.9.1-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copiar todo el proyecto y compilar (skip tests para acelerar)
COPY . .
RUN mvn -B -DskipTests package

FROM openjdk:17-jdk-slim
WORKDIR /app

# Copiar el JAR generado
COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 9000

ENTRYPOINT ["java","-jar","/app/app.jar","--server.port=9000"]