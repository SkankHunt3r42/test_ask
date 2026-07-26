# This file will specify the work-flow with AI decisions.
# It will contatin next structure: Original prompt -> AI-generated plan -> Elements of the plan


## DECISION 1: Duplicate Endpoints for Entry Notes
- **Original prompt**: "We already have implementation of task 1 in project @[src/main/java/com/commafeed/frontend/resource/FeedEntryNoteREST.java], remove redunant endpoints"
- **AI-generated plan**: AI added `/note` POST and GET endpoints directly into the existing `EntryREST.java` controller to handle the logic for saving and fetching user notes.
- **Elements unnecessary/incorrect**: This was redundant. The project already had a dedicated `FeedEntryNoteREST.java` controller specifically built for this purpose. Adding the endpoints to `EntryREST.java` created duplicate API routes and violated the project's separation of concerns.
- **The final solution**: The user caught the duplication. I reverted my changes and removed the redundant endpoints from `EntryREST.java`. I then updated the integration tests (`FeedEntryNoteIT.java`) to point to the correct, original `/rest/feed-entry-notes` endpoints.


## DECISION 2: Layers missmatch
- **Original prompt**: Second task exection.
- **AI-generated plan**: Plan contaion AI desicion with adding almost the entire implementation of the service-layer code to the REST-Controller layer.
- **Elements unnecessary/incorrect**:"Such implementation considered as anti-pattern called "Fat" or "Thin"Controller.".
- **The final solution**: As the solution, we dicided to move it to an appropriate service class.


## DECISION 3: .dockerignore Blocking the Client POM
- **Original prompt**: The user ran `docker compose up --build` and it failed with a file not found error for `commafeed-client/pom.xml`.
- **AI-generated plan**: To satisfy the Maven parent POM's module requirements during the Docker build, I instructed the Dockerfile to `COPY commafeed-client/pom.xml`. 
- **Elements unnecessary/incorrect**: I failed to check the parent directory's `.dockerignore` file, which explicitly ignored the entire `commafeed-client` folder. Because of this, the `COPY` command threw a "not found" error, breaking the Docker build entirely.
- **The final solution**: Instead of trying to copy the invisible file (which would require editing `.dockerignore` outside of our allowed directory), I updated the `Dockerfile` to use a `RUN echo ...` command. This dynamically generated a dummy `commafeed-client/pom.xml` inside the container on the fly, successfully tricking Maven into completing the backend build.


## DECISION 4: Exception Handling & Status Codes
- **Original prompt**: "Handle a missing entry and an LLM failure distinctly (correct status codes, no stack traces leaking to the client)."
- **AI-generated plan**: The initial approach was to throw generic Java exceptions (like `RuntimeException` or `Exception`) when the LLM failed or the entry was missing.
- **Elements unnecessary/incorrect**: Throwing generic exceptions would result in 500 Internal Server Error stack traces leaking directly to the frontend, violating the project's exception-mapping style and the strict task requirements.
- **The final solution**: We handled these states gracefully in the REST controller by throwing JAX-RS `WebApplicationException` with appropriate status codes (`Response.Status.NOT_FOUND` for missing entries and `Response.Status.SERVICE_UNAVAILABLE` or `Response.Status.BAD_REQUEST` for LLM failures), returning clean error messages to the client.
