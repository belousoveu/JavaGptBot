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
public class HelpCommand implements BotAction {


    @Override
    public SendMessage getAnswer(UserDialog dialog) {
        return SendMessage.builder()
                .chatId(dialog.getChatId())
                .text(HELP_MESSAGE)
                .parseMode("Markdown")
                .build();
    }

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage() && update.getMessage().getText().startsWith("/help");
    }

    @Override
    public String getName() {
        return "HelpCommand";
    }

    @Override
    public boolean isLongOperation() {
        return false;
    }

    @Override
    public String getLongOperationMessage() {
        return "";
    }

    private static final String HELP_MESSAGE = """
             Чтобы заюзать мою помощь, кинь ссылку на пулл-реквест в репозитории.
             Формат должен быть примерно такой:
             ```https://github.com/{username}/{project-name}/pull/1```
             Готово? Отправляй!
            """;
}
