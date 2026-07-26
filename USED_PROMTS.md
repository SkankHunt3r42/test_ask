# This file will contain only the promts, that was used durign task execting.

## First task promt

[AI_RULES.md](file;file:///home/nikitos/IdeaProjects/commafeed/commafeed-server/AI_RULES.md) you should always use this file during code generation. It contains basic instructions on how to exactly u should behave and what is prohibited for you to touch or what is allowed to work. For now you should carefully review everyting that this file contains, and begin with TASK 1. Here some additional information for you so it can help to do this task:

TASK 1 — Add a 'saved entry notes' resource 

CommaFeed already models feeds and feed entries. Add a small new capability alongside them, following the existing REST → Service → DAO → entity layering:

A new JPA entity representing a user-saved note on a feed entry (e.g. a short comment plus a star rating the user attaches to an entry they read).

A DAO for it, extending the project's existing generic DAO the way the other DAOs do.

A Service that contains the logic.

Two REST endpoints on a new resource class:

POST to create/attach a note to an entry (validate the input),

GET to list the current user's notes.

Return proper status codes and reuse the project's existing DTO / exception-mapping style.

## Second task promt

Lets go futher with next task - task 2. All rules,restrictions, conditions are specefied in file [AI_RULES.md](file;file:///home/nikitos/IdeaProjects/commafeed/commafeed-server/AI_RULES.md). Any questions you have, should be resolved within your propoced plan, by my comments.

Here some additional information about TASK 2 that will help you proceed:

TASK  2 — LLM 'rewrite this entry' endpoint 

Add an endpoint that takes an existing feed entry by ID and uses a free LLM API to produce an alternative version of its title or content.

POST /entry/{id}/generate-alternative

Request body: target (title or content) and prompt (a free-text instruction, e.g. "rewrite this headline for a technical audience").


The endpoint loads the entry, sends the chosen field plus the prompt to the LLM, and returns the original entry, the target, the prompt, and the generated alternative.

Handle a missing entry and an LLM failure distinctly (correct status codes, no stack traces leaking to the client).

The LLM part such as api key configuration - will be handled by myself
