package com.perplexity.perplexity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponsePayload {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        public Message message;

        public Message getMessage() {
            return message;
        }
    }

    private List<Choice> choices;

    public List<Choice> getChoices() { return choices; }
}
