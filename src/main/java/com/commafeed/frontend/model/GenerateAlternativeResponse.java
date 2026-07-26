package com.commafeed.frontend.model;

import java.io.Serializable;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@SuppressWarnings("serial")
@Data
@Schema(description = "Generate alternative text response")
public class GenerateAlternativeResponse implements Serializable {

    @Schema(description = "original entry")
    private Entry originalEntry;

    @Schema(description = "target that was rewritten")
    private String target;

    @Schema(description = "prompt instruction for the LLM")
    private String prompt;

    @Schema(description = "generated alternative text")
    private String generatedAlternative;
}
