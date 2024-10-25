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
public class StartCommand implements BotAction {

    @Override
    public SendMessage getAnswer(UserDialog dialog) {

        return SendMessage.builder()
                .chatId(dialog.getChatId())
                .text(START_MESSAGE)
                .build();

    }

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage() && update.getMessage().getText().startsWith("/start");
    }

    @Override
    public String getName() {
        return "StartCommand";
    }

    @Override
    public boolean isLongOperation() {
        return false;
    }

    @Override
    public String getLongOperationMessage() {
        return "";
    }


    private static final String START_MESSAGE = """
            Эй, народ! Я бот, который может заценить ваш Java проект. 
            Проверю, насколько он в теме с ООП, чистым кодом, SOLID, KISS, DRY и YAGNI. 
            Также гляну, как используются паттерны проектирования. 
            И, конечно, подкину идеи для прокачки проекта. Готов? Погнали!
            """;
}
