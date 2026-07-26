package com.commafeed.frontend.model;

import com.commafeed.backend.model.FeedEntryNote;
import java.io.Serializable;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@SuppressWarnings("serial")
@Data
@Schema(description = "Entry Note")
public class EntryNote implements Serializable {

    private Long id;
    private Long entryId;
    private String content;
    private Integer rating;

    public static EntryNote build(FeedEntryNote note) {
        EntryNote en = new EntryNote();
        en.setId(note.getId());
        en.setEntryId(note.getEntry().getId());
        en.setContent(note.getContent());
        en.setRating(note.getRating());
        return en;
    }
}
