FROM gradle:8.3-jdk20-alpine AS builder

WORKDIR /app

# Cache Gradle dependencies
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY src src

# Build application
RUN gradle build --no-daemon --stacktrace -x test

# Runtime stage
FROM openjdk:20-jdk-slim

ENV APP_HOME=/app

WORKDIR $APP_HOME

# Create non-root user
RUN useradd -m appuser && chown -R appuser /app
USER appuser

# Copy build JAR
COPY --from=builder --chown=appuser /app/build/libs/*-all.jar /app/app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s \
  CMD curl --fail http://localhost:8080/health || exit 1

CMD [ "java", "-jar", "app.jar" ]