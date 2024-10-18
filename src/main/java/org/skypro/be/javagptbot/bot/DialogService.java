/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.bot;

import org.skypro.be.javagptbot.utils.TextUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component
public class DialogService {

    private static final int MAX_MESSAGE_LENGTH = 4096;

    public long getChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        }
        throw new IllegalArgumentException("No chat id"); //TODO: класс ошибки
    }

    public long getUserId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getId();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        }
        throw new IllegalArgumentException("No user id"); //TODO: класс ошибки
    }

    public List<SendMessage> getMessages(SendMessage message) {
        String text = message.getText();
        text = TextUtils.correctMarkdownFormat(text);
        System.out.println("text = " + text);
        List<SendMessage> messages = new ArrayList<>();
        long chatId = Long.parseLong(message.getChatId());
        while (text.length()>=MAX_MESSAGE_LENGTH) {
            String part = TextUtils.getPart(text, MAX_MESSAGE_LENGTH);
            text = text.substring(part.length()-1);
            messages.add(SendMessage.builder().text(part).chatId(chatId).parseMode(message.getParseMode()).build());
        }
        message.setText(text);
        messages.add(message);
        return messages;
    }
}
