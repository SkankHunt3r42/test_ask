package com.commafeed.frontend.model.request;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@SuppressWarnings("serial")
@Data
@Schema(description = "Add Note Request")
public class AddEntryNoteRequest implements Serializable {

    @Schema(description = "entry id", required = true)
    @NotNull
    private Long entryId;

    @Schema(description = "note content")
    private String content;

    @Schema(description = "star rating (0-5, for example)")
    private Integer rating;
}
