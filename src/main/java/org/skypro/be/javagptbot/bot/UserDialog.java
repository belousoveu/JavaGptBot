/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.bot;


import lombok.Data;
import org.skypro.be.javagptbot.gigachat.request.DialogMessage;
import org.skypro.be.javagptbot.gigachat.constants.GigaChatRole;
import org.skypro.be.javagptbot.utils.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Data
public class UserDialog {
    private final long userId;
    private boolean blocked = false;
    private long chatId;
    private Map<String, String> questions;
    private List<DialogMessage> messages;


    public UserDialog(long userId) {
        this.userId = userId;
    }

    public String getQuestionText(String questionKey) {
        return questions.get(questionKey);
    }

    public void addMessage(DialogMessage message) {
        messages.add(message);
    }

    public void clearMessages() {
        this.messages = new ArrayList<>();
        addMessage(new DialogMessage(GigaChatRole.SYSTEM_ROLE, TextUtils.getPrompt("tutor")));
    }

    public List<DialogMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }
}
