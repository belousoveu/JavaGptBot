package org.skypro.be.javagptbot.bot.action;

import lombok.Data;
import org.skypro.be.javagptbot.bot.UserDialog;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class StartCommand implements BotAction {

    private long userId;

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
            Привет! Я бот, который может проанализировать проект на Java
            и оценить его на предмет соблюдения принципов ООП, Чистого кода,
            SOLID, KISS, DRY, YAGNI. Проверить правильность применения
            основных паттернов проектирования.
            А также предложить варианты улучшения проекта.
            """;
}
