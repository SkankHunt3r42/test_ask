# Stage 1: Build the commafeed-server
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Copy the localized parent pom and the server pom
COPY parent/pom.xml parent/pom.xml
COPY pom.xml .

# Copy the server source code and dev tools (for checkstyle)
COPY src src
COPY dev dev

# We build the module standalone, using a cache mount to save Maven dependencies permanently
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

# Stage 2: Run the compiled application
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copy the built application from the build stage
COPY --from=build /app/target/quarkus-app/ /app/

EXPOSE 8082

# Run the Quarkus fast-jar
CMD ["java", "-jar", "quarkus-run.jar"]
