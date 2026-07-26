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
