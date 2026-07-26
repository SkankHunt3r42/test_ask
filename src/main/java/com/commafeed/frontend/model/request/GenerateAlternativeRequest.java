package com.commafeed.frontend.model.request;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@SuppressWarnings("serial")
@Data
@Schema(description = "Generate alternative text request")
public class GenerateAlternativeRequest implements Serializable {

    @Schema(description = "target to rewrite (title or content)", required = true)
    @NotBlank
    private String target;

    @Schema(description = "prompt instruction for the LLM", required = true)
    @NotBlank
    private String prompt;
}
