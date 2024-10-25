/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.bot.action;

import org.skypro.be.javagptbot.bot.UserDialog;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageSent implements BotAction {


    @Override
    public SendMessage getAnswer(UserDialog dialog) {
        return SendMessage.builder()
                .chatId(dialog.getChatId())
                .text(OTHER_MESSAGE)
                .build();
    }

    @Override
    public boolean canHandle(Update update) {
        return true;
    }

    @Override
    public String getName() {
        return "MessageSent";
    }

    @Override
    public boolean isLongOperation() {
        return false;
    }

    @Override
    public String getLongOperationMessage() {
        return "";
    }

    private static final String OTHER_MESSAGE = """
            Сорян, но тут я ничем не зацеплю. Залетите еще раз в помощь - /help!
            """;
}
