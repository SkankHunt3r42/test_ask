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
./mvnw quarkus:dev
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
# AI WORKFLOW

For this task, I used Antigravity AI, because its agentic capabilities allow for direct codebase navigation and terminal execution without context-switching. Since CommaFeed is too large to paste into a single prompt, I managed the context dynamically. Instead of dumping entire directories, I created a strict AI_RULES.md file that acted as a persistent system prompt. This file enforced architectural boundaries (e.g., JAX-RS over Spring Boot, extending GenericDAO). To keep token usage strictly under control and prevent "hallucinations" of massive incorrect code blocks, I enforced a "Plan-first" rule. I directed the AI to analyze specific existing files (like EntryREST.java or FeedEntry.java) as templates, and required it to outline a step-by-step proposal. The AI was strictly forbidden from generating any implementation code until I manually reviewed and approved its plan.