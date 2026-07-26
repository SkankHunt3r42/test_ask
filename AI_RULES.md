# AI Assistant Rules & Context for CommaFeed Project

## 1. Context & Tech Stack
You are an expert Java developer helping to add new features to `CommaFeed`, an open-source RSS reader.
- **Backend Framework:** Quarkus.
- **REST API:** JAX-RS (Strictly use `@Path`, `@GET`, `@POST`, `@Produces`, `@Consumes`).
- **Database / ORM:** Hibernate (JPA), embedded H2 database.
- **Boilerplate:** Lombok is used extensively (use `@Getter`, `@Setter`, `@RequiredArgsConstructor`, etc.).

## 2. Hard Restrictions (CRITICAL)
- **NO SPRING BOOT:** Do NOT use `@RestController`, `@Autowired`, `@RequestMapping`, or any Spring-specific annotations. Use CDI (`@Inject`, `@ApplicationScoped`) and JAX-RS.
- **Scope:** Modify ONLY the `commafeed-server` module. Completely ignore the `commafeed-client` module.
- **Secrets:** NEVER hardcode API keys or secrets in the code. Read them from configuration or environment variables.
- **Error Handling:** Do not leak stack traces to the client. Use existing ExceptionMappers and standard HTTP status codes (e.g., 404 Not Found, 502 Bad Gateway for external API failures).

## 3. Architectural Conventions
You must strictly follow the existing project structure: `REST Resources -> Service -> DAO -> JPA Entities`.
- **Entities:** Put new JPA entities in the `model` package.
- **DAOs:** Put database access logic in the `dao` package. ANY new DAO MUST extend the existing generic DAO class of the project (find and analyze how other DAOs are structured before creating a new one).
- **Services:** Put business logic in the `service` package.
- **REST:** Put API endpoints in the `rest` package. Reuse existing DTO patterns for requests/responses.

## 4. Current Tasks Overview
- **Level 1:** Add a 'saved entry notes' resource (JPA entity, DAO, Service, and REST endpoints to POST a note and GET current user's notes).
- **Level 2:** Add an LLM 'rewrite this entry' endpoint (`POST /entry/{id}/generate-alternative`) using a free LLM API, handling failures gracefully.

Before generating any code, ALWAY outline a brief plan of which files you intend to create or modify. Wait for my approval. 
