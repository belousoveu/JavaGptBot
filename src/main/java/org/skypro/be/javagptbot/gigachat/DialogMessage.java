/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.gigachat;

import lombok.Data;

@Data
public class DialogMessage {
    private String role;
    private String content;

    public DialogMessage(String userRole, String question) {
        this.role = userRole;
        this.content = question;
    }
}
