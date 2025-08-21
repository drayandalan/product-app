# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom & download dependency first (layer cache)
COPY pom.xml ./
RUN mvn -q -DskipTests dependency:go-offline

# Build the application
COPY src ./src
RUN mvn -q -DskipTests clean package

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# For compose health check (curl /actuator/health)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

ENV JAVA_OPTS=""
ENV PORT=8080
EXPOSE 8080

# Copy the bootable jar
COPY --from=build /app/target/app.jar /app/app.jar

# Run
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
