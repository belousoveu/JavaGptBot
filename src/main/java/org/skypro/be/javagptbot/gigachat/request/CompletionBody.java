/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.gigachat.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static org.skypro.be.javagptbot.gigachat.constants.GigaChatModel.GIGA_CHAT_MODEL;

@Slf4j
@Data
public class CompletionBody {
    private String model;
    private List<DialogMessage> messages;
    private float temperature;
    private boolean stream;
    private int max_tokens;
    private float repetition_penalty;
    private int update_interval;


    public CompletionBody(List<DialogMessage> messages) {
        this.model = GIGA_CHAT_MODEL;
        this.messages = messages;
        this.temperature = 0.25f;
        this.stream = false;
        this.max_tokens = 800;
        this.repetition_penalty = 1.2f;
        this.update_interval = 0;
    }

    public String toJsonString() {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return "";
        }
    }
}
