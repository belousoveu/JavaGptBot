/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DialogService {

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

}
