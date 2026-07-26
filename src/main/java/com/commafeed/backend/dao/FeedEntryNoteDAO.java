package com.commafeed.backend.dao;

import com.commafeed.backend.model.FeedEntry;
import com.commafeed.backend.model.FeedEntryNote;
import com.commafeed.backend.model.QFeedEntryNote;
import com.commafeed.backend.model.User;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import java.util.List;

@Singleton
public class FeedEntryNoteDAO extends GenericDAO<FeedEntryNote> {

    private static final QFeedEntryNote NOTE = QFeedEntryNote.feedEntryNote;

    public FeedEntryNoteDAO(EntityManager entityManager) {
        super(entityManager, FeedEntryNote.class);
    }

    public List<FeedEntryNote> findByUser(User user) {
        return query().selectFrom(NOTE).where(NOTE.user.eq(user)).fetch();
    }

    public FeedEntryNote findByUserAndEntry(User user, FeedEntry entry) {
        return query().selectFrom(NOTE)
                .where(NOTE.user.eq(user), NOTE.entry.eq(entry))
                .fetchFirst();
    }
}
