# Stage 1: Build the commafeed-server
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# We are building with context: .. (from docker-compose) so we copy the parent pom to satisfy Maven
COPY pom.xml .
# Create a minimal dummy client pom.xml because the real one is blocked by ../.dockerignore
RUN mkdir commafeed-client && echo '<project><modelVersion>4.0.0</modelVersion><parent><groupId>com.commafeed</groupId><artifactId>commafeed</artifactId><version>7.2.0</version></parent><artifactId>commafeed-client</artifactId></project>' > commafeed-client/pom.xml
COPY commafeed-server/pom.xml commafeed-server/

# We also copy the server source code and dev tools (for checkstyle)
COPY commafeed-server/src commafeed-server/src
COPY commafeed-server/dev commafeed-server/dev

# We build ONLY the commafeed-server module
WORKDIR /app/commafeed-server
# Using -am (also make) to ensure it builds anything commafeed-server requires, skipping tests for speed
RUN mvn clean package -pl . -am -DskipTests

# Stage 2: Run the compiled application
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copy the built application from the build stage
COPY --from=build /app/commafeed-server/target/quarkus-app/ /app/

EXPOSE 8082

# Run the Quarkus fast-jar
CMD ["java", "-jar", "quarkus-run.jar"]
