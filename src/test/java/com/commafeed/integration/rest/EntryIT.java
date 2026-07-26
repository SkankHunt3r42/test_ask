package com.commafeed.integration.rest;

import com.commafeed.TestConstants;
import com.commafeed.backend.service.LlmService;
import com.commafeed.frontend.model.GenerateAlternativeResponse;
import com.commafeed.frontend.model.request.GenerateAlternativeRequest;
import com.commafeed.integration.BaseIT;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
class EntryIT extends BaseIT {

    @InjectMock LlmService llmService;

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

    private String getEntryId() {
        Long subscriptionId = subscribeAndWaitForEntries(getFeedUrl());
        return getFeedEntries(subscriptionId).getEntries().get(0).getId();
    }

    @Test
    void testGenerateAlternativeSuccess() throws Exception {
        String entryId = getEntryId();
        Assertions.assertNotNull(entryId, "Entry ID should not be null");

        // Mock LLM service
        Mockito.when(llmService.generateAlternative(Mockito.anyString(), Mockito.anyString()))
                .thenReturn("This is an LLM generated alternative.");

        GenerateAlternativeRequest req = new GenerateAlternativeRequest();
        req.setTarget("title");
        req.setPrompt("Make it funny");

        GenerateAlternativeResponse res =
                RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(req)
                        .post("rest/entry/{id}/generate-alternative", entryId)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(GenerateAlternativeResponse.class);

        Assertions.assertNotNull(res.getGeneratedAlternative());
        Assertions.assertEquals(
                "This is an LLM generated alternative.", res.getGeneratedAlternative());
        Assertions.assertNotNull(res.getOriginalEntry());
        Assertions.assertEquals(entryId, res.getOriginalEntry().getId());
    }

    @Test
    void testGenerateAlternativeBadTarget() {
        String entryId = getEntryId();

        GenerateAlternativeRequest req = new GenerateAlternativeRequest();
        req.setTarget("author"); // Invalid target
        req.setPrompt("Make it funny");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(req)
                .post("rest/entry/{id}/generate-alternative", entryId)
                .then()
                .statusCode(400);
    }

    @Test
    void testGenerateAlternativeLlmError() throws Exception {
        String entryId = getEntryId();

        // Mock LLM service to throw exception
        Mockito.when(llmService.generateAlternative(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new IOException("LLM API failed"));

        GenerateAlternativeRequest req = new GenerateAlternativeRequest();
        req.setTarget("title");
        req.setPrompt("Make it funny");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(req)
                .post("rest/entry/{id}/generate-alternative", entryId)
                .then()
                .statusCode(502); // 502 Bad Gateway
    }
}
