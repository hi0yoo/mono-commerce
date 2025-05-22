# Step 1: Build stage using JDK
FROM eclipse-temurin:21-jdk AS builder

# Working directory
WORKDIR /app

# Gradle 설정 파일 복사
# Gradle Wrapper 복사
COPY gradlew .
# Gradle 디렉토리 복사
COPY gradle gradle
# 빌드 설정
COPY build.gradle.kts .
# Gradle settings
COPY settings.gradle.kts .

# 소스 코드 및 모듈 복사
# 멀티모듈 복사
COPY modules modules
# 필요 시 gitignore 복사
COPY .gitignore .gitignore

# 실행 권한 추가 및 빌드
RUN chmod +x ./gradlew                      # Wrapper 실행권한 추가
RUN ./gradlew clean build --no-daemon       # jar 파일 빌드

# Step 2: Runtime stage with JRE
FROM eclipse-temurin:21-jre AS runtime

# Working directory for the application
WORKDIR /app

# Build된 jar 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose application port
EXPOSE 8080

# Run application at container launch
ENTRYPOINT ["java", "-jar", "/app/app.jar"]