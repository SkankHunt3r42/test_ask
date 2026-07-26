# CommaFeed Server

This is the backend module for CommaFeed, built with Quarkus.

## Configuration

**IMPORTANT:** Before launching the project, you must provide your own LLM API key for the AI text generation features to work. 

Open `src/main/resources/application.properties` and replace the placeholder with your actual API key:
```properties
app.llm.api.key=YOUR_API_KEY_HERE
```

## How to launch locally

There are two primary ways to launch the backend server locally: using the Maven Wrapper or using Docker.

### 1. Using the Maven Wrapper (Development Mode)

You can launch the application directly from the source code using the provided Maven wrapper. This is the recommended approach if you want live-reloading while developing.

**Prerequisites:** Java 25 installed locally.

From the `commafeed-server` directory, run:
```bash
../mvnw quarkus:dev
```
*Note: The server will start up and listen on `http://localhost:8083` for API requests.*

### 2. Using Docker (Production Build)

If you do not have Java or Maven installed locally, or if you simply want to run the compiled application in a clean, isolated environment, you can use Docker. 

**Prerequisites:** Docker and Docker Compose installed.

From the `commafeed-server` directory, run a single command:
```bash
docker compose up --build -d
```

- Docker will download a Java 25 Maven environment, compile the code cleanly inside the container, and start the application in the background.
- The backend API will be running on `http://localhost:8082`.
- Any database files will be safely persisted in a local Docker volume (`commafeed-data`).

To view the logs of the running container:
```bash
docker compose logs -f
```

To shut the server down:
```bash
docker compose down
```
