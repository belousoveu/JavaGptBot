/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.bot;


import lombok.Data;

import java.util.Map;


@Data
public class UserDialog {
    private final long userId;
    private boolean blocked = false;
    private long chatId;
    private Map<String, String> questions;


    public UserDialog(long userId) {
        this.userId = userId;
    }

    public String getQuestionText(String questionKey) {
        return questions.get(questionKey);
    }
}
