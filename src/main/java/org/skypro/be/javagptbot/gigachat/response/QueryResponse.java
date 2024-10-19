/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.gigachat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class QueryResponse {

    private List<Choice> choices;
    private long created;
    private String model;
    private Usage usage;
    private String object;

    @Data
    public static class Choice {
        private GptMessage message;
        private int index;
        private String finish_reason;
    }


    @Data
    public static class GptMessage {
        private String role;
        private String content;

        @JsonProperty("data_for_context")
        private List<Object> dataForContext;
    }

    @Data
    public static class Usage {
        private int prompt_tokens;
        private int completion_tokens;
        private int total_tokens;
    }
}
