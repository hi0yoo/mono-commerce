# Step 1: Build stage using JDK
FROM eclipse-temurin:21-jdk AS builder

# Working directory
WORKDIR /app

COPY build/libs/*.jar app.jar

# Expose application port
EXPOSE 8080

# Run application at container launch
ENTRYPOINT ["java", "-jar", "/app/app.jar"]