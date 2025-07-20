package com.perplexity.perplexity.model;

import java.util.List;

public class RequestPayload {
    private String model;
    private List<Message> messages;

    public RequestPayload(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    public String getModel() { return model; }
    public List<Message> getMessages() { return messages; }
}
