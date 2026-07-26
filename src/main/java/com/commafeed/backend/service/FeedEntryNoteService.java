package com.commafeed.backend.service;

import com.commafeed.backend.dao.FeedEntryDAO;
import com.commafeed.backend.dao.FeedEntryNoteDAO;
import com.commafeed.backend.model.FeedEntry;
import com.commafeed.backend.model.FeedEntryNote;
import com.commafeed.backend.model.User;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;

@Singleton
public class FeedEntryNoteService {

    private final FeedEntryNoteDAO feedEntryNoteDAO;
    private final FeedEntryDAO feedEntryDAO;

    @Inject
    public FeedEntryNoteService(FeedEntryNoteDAO feedEntryNoteDAO, FeedEntryDAO feedEntryDAO) {
        this.feedEntryNoteDAO = feedEntryNoteDAO;
        this.feedEntryDAO = feedEntryDAO;
    }

    public FeedEntryNote addNote(User user, Long entryId, String content, Integer rating) {
        FeedEntry entry = feedEntryDAO.findById(entryId);
        if (entry == null) {
            throw new IllegalArgumentException("Entry not found");
        }

        FeedEntryNote note = feedEntryNoteDAO.findByUserAndEntry(user, entry);
        if (note == null) {
            note = new FeedEntryNote();
            note.setUser(user);
            note.setEntry(entry);
        }
        note.setContent(content);
        note.setRating(rating);
        if (note.getId() == null) {
            feedEntryNoteDAO.persist(note);
        } else {
            feedEntryNoteDAO.merge(note);
        }
        return note;
    }

    public List<FeedEntryNote> getNotes(User user) {
        return feedEntryNoteDAO.findByUser(user);
    }
}
