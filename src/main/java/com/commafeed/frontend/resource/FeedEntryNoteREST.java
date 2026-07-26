package com.commafeed.frontend.resource;

import com.commafeed.backend.model.FeedEntryNote;
import com.commafeed.backend.model.User;
import com.commafeed.backend.service.FeedEntryNoteService;
import com.commafeed.frontend.model.EntryNote;
import com.commafeed.frontend.model.request.AddEntryNoteRequest;
import com.commafeed.security.AuthenticationContext;
import com.commafeed.security.Roles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/rest/feed-entry-notes")
@RolesAllowed(Roles.USER)
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
@Singleton
@Tag(name = "Entry Notes")
public class FeedEntryNoteREST {

    private final FeedEntryNoteService feedEntryNoteService;
    private final AuthenticationContext authenticationContext;

    @POST
    @Transactional
    @Operation(
            summary = "Add or update a note for a feed entry",
            description = "Add a note to a feed entry")
    public Response addNote(
            @Valid @Parameter(description = "Add Note request", required = true)
                    AddEntryNoteRequest req) {
        User user = authenticationContext.getCurrentUser();
        try {
            feedEntryNoteService.addNote(user, req.getEntryId(), req.getContent(), req.getRating());
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Transactional
    @Operation(
            summary = "Get user's entry notes",
            description = "Get all entry notes for the current user")
    @APIResponse(
            responseCode = "200",
            content = {
                @Content(
                        mediaType = "application/json",
                        schema =
                                @Schema(
                                        implementation = EntryNote.class,
                                        type =
                                                org.eclipse.microprofile.openapi.annotations.enums
                                                        .SchemaType.ARRAY))
            })
    public Response getNotes() {
        User user = authenticationContext.getCurrentUser();
        List<FeedEntryNote> notes = feedEntryNoteService.getNotes(user);
        List<EntryNote> entryNotes = notes.stream().map(EntryNote::build).toList();
        return Response.ok(entryNotes).build();
    }
}
