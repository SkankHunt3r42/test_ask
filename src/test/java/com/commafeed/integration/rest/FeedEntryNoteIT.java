package com.commafeed.integration.rest;

import com.commafeed.TestConstants;
import com.commafeed.frontend.model.EntryNote;
import com.commafeed.frontend.model.request.AddEntryNoteRequest;
import com.commafeed.integration.BaseIT;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class FeedEntryNoteIT extends BaseIT {

    @BeforeEach
    void setup() {
        initialSetup(TestConstants.ADMIN_USERNAME, TestConstants.ADMIN_PASSWORD);
        RestAssured.authentication =
                RestAssured.preemptive()
                        .basic(TestConstants.ADMIN_USERNAME, TestConstants.ADMIN_PASSWORD);
    }

    @AfterEach
    void cleanup() {
        RestAssured.reset();
    }

    @Test
    void testAddAndGetNotes() {
        // Subscribe to a feed to get some entries
        Long subscriptionId = subscribeAndWaitForEntries(getFeedUrl());

        // Fetch feed entries
        String entryId = getFeedEntries(subscriptionId).getEntries().get(0).getId();

        Assertions.assertNotNull(entryId, "Entry ID should not be null");

        // Add a note
        AddEntryNoteRequest addReq = new AddEntryNoteRequest();
        addReq.setEntryId(Long.valueOf(entryId));
        addReq.setContent("This is a test note.");
        addReq.setRating(5);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(addReq)
                .post("rest/feed-entry-notes")
                .then()
                .statusCode(200);

        // Get notes and verify
        EntryNote[] notes =
                RestAssured.given()
                        .get("rest/feed-entry-notes")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(EntryNote[].class);

        List<EntryNote> notesList = Arrays.asList(notes);
        Assertions.assertEquals(1, notesList.size());

        EntryNote fetchedNote = notesList.get(0);
        Assertions.assertEquals(Long.valueOf(entryId), fetchedNote.getEntryId());
        Assertions.assertEquals("This is a test note.", fetchedNote.getContent());
        Assertions.assertEquals(5, fetchedNote.getRating());
    }
}
