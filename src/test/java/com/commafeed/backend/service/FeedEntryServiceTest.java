package com.commafeed.backend.service;

import com.commafeed.backend.dao.FeedEntryDAO;
import com.commafeed.backend.dao.FeedEntryStatusDAO;
import com.commafeed.backend.dao.FeedSubscriptionDAO;
import com.commafeed.backend.model.Feed;
import com.commafeed.backend.model.FeedEntry;
import com.commafeed.backend.model.FeedEntryContent;
import com.commafeed.backend.model.FeedEntryStatus;
import com.commafeed.backend.model.FeedSubscription;
import com.commafeed.backend.model.User;
import com.commafeed.backend.service.FeedEntryService.GenerateAlternativeResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedEntryServiceTest {

    @Mock private FeedSubscriptionDAO feedSubscriptionDAO;
    @Mock private FeedEntryDAO feedEntryDAO;
    @Mock private FeedEntryStatusDAO feedEntryStatusDAO;
    @Mock private FeedEntryContentService feedEntryContentService;
    @Mock private FeedEntryFilteringService feedEntryFilteringService;
    @Mock private LlmService llmService;

    private FeedEntryService feedEntryService;
    private User user;
    private FeedEntry entry;
    private FeedEntryContent content;

    @BeforeEach
    void init() {
        feedEntryService =
                new FeedEntryService(
                        feedSubscriptionDAO,
                        feedEntryDAO,
                        feedEntryStatusDAO,
                        feedEntryContentService,
                        feedEntryFilteringService,
                        llmService);

        user = new User();
        user.setId(1L);

        content = new FeedEntryContent();
        content.setTitle("Original Title");
        content.setContent("Original Content");

        entry = new FeedEntry();
        entry.setId(100L);
        entry.setContent(content);
        entry.setFeed(new Feed());
    }

    @Test
    void generateAlternativeReturnsNullIfEntryNotFound() throws Exception {
        Mockito.when(feedEntryDAO.findById(100L)).thenReturn(null);
        GenerateAlternativeResult result =
                feedEntryService.generateAlternative(user, 100L, "title", "prompt");
        Assertions.assertNull(result);
    }

    @Test
    void generateAlternativeThrowsExceptionIfTargetInvalid() {
        Mockito.when(feedEntryDAO.findById(100L)).thenReturn(entry);
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> feedEntryService.generateAlternative(user, 100L, "author", "prompt"));
    }

    @Test
    void generateAlternativeThrowsExceptionIfTargetTextEmpty() {
        content.setTitle("");
        Mockito.when(feedEntryDAO.findById(100L)).thenReturn(entry);
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> feedEntryService.generateAlternative(user, 100L, "title", "prompt"));
    }

    @Test
    void generateAlternativeSuccessForTitle() throws Exception {
        Mockito.when(feedEntryDAO.findById(100L)).thenReturn(entry);
        Mockito.when(llmService.generateAlternative("Original Title", "prompt"))
                .thenReturn("Generated Title");

        FeedSubscription sub = new FeedSubscription();
        Mockito.when(feedSubscriptionDAO.findByFeed(user, entry.getFeed())).thenReturn(sub);

        FeedEntryStatus status = new FeedEntryStatus();
        Mockito.when(feedEntryStatusDAO.getStatus(user, sub, entry)).thenReturn(status);

        GenerateAlternativeResult result =
                feedEntryService.generateAlternative(user, 100L, "title", "prompt");

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Generated Title", result.generated());
        Assertions.assertEquals(status, result.status());
    }

    @Test
    void generateAlternativeSuccessForContent() throws Exception {
        Mockito.when(feedEntryDAO.findById(100L)).thenReturn(entry);
        Mockito.when(llmService.generateAlternative("Original Content", "prompt"))
                .thenReturn("Generated Content");

        FeedSubscription sub = new FeedSubscription();
        Mockito.when(feedSubscriptionDAO.findByFeed(user, entry.getFeed())).thenReturn(sub);

        FeedEntryStatus status = new FeedEntryStatus();
        Mockito.when(feedEntryStatusDAO.getStatus(user, sub, entry)).thenReturn(status);

        GenerateAlternativeResult result =
                feedEntryService.generateAlternative(user, 100L, "content", "prompt");

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Generated Content", result.generated());
        Assertions.assertEquals(status, result.status());
    }
}
