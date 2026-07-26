package com.commafeed.backend.service;

import com.commafeed.backend.dao.FeedEntryDAO;
import com.commafeed.backend.dao.FeedEntryNoteDAO;
import com.commafeed.backend.model.FeedEntry;
import com.commafeed.backend.model.FeedEntryNote;
import com.commafeed.backend.model.User;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedEntryNoteServiceTest {

    @Mock private FeedEntryNoteDAO feedEntryNoteDAO;
    @Mock private FeedEntryDAO feedEntryDAO;

    private FeedEntryNoteService feedEntryNoteService;

    private User user;
    private FeedEntry entry;

    @BeforeEach
    void init() {
        feedEntryNoteService = new FeedEntryNoteService(feedEntryNoteDAO, feedEntryDAO);

        user = new User();
        user.setId(1L);

        entry = new FeedEntry();
        entry.setId(100L);
    }

    @Test
    void addNoteShouldThrowExceptionIfEntryNotFound() {
        Mockito.when(feedEntryDAO.findById(100L)).thenReturn(null);
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> feedEntryNoteService.addNote(user, 100L, "test note", 5));
    }

    @Test
    void addNoteShouldCreateNewNoteIfNoneExists() {
        Mockito.when(feedEntryDAO.findById(100L)).thenReturn(entry);
        Mockito.when(feedEntryNoteDAO.findByUserAndEntry(user, entry)).thenReturn(null);

        FeedEntryNote createdNote = feedEntryNoteService.addNote(user, 100L, "New Note", 4);

        Assertions.assertNotNull(createdNote);
        Assertions.assertEquals("New Note", createdNote.getContent());
        Assertions.assertEquals(4, createdNote.getRating());
        Assertions.assertEquals(user, createdNote.getUser());
        Assertions.assertEquals(entry, createdNote.getEntry());

        Mockito.verify(feedEntryNoteDAO).persist(ArgumentMatchers.any(FeedEntryNote.class));
    }

    @Test
    void addNoteShouldUpdateExistingNote() {
        FeedEntryNote existingNote = new FeedEntryNote();
        existingNote.setId(50L);
        existingNote.setUser(user);
        existingNote.setEntry(entry);
        existingNote.setContent("Old Note");
        existingNote.setRating(3);

        Mockito.when(feedEntryDAO.findById(100L)).thenReturn(entry);
        Mockito.when(feedEntryNoteDAO.findByUserAndEntry(user, entry)).thenReturn(existingNote);

        FeedEntryNote updatedNote = feedEntryNoteService.addNote(user, 100L, "Updated Note", 5);

        Assertions.assertEquals("Updated Note", updatedNote.getContent());
        Assertions.assertEquals(5, updatedNote.getRating());
        Assertions.assertEquals(50L, updatedNote.getId());

        Mockito.verify(feedEntryNoteDAO).merge(existingNote);
    }

    @Test
    void getNotesShouldReturnNotesForUser() {
        FeedEntryNote note = new FeedEntryNote();
        Mockito.when(feedEntryNoteDAO.findByUser(user)).thenReturn(List.of(note));

        List<FeedEntryNote> notes = feedEntryNoteService.getNotes(user);

        Assertions.assertEquals(1, notes.size());
        Assertions.assertEquals(note, notes.get(0));
    }
}
