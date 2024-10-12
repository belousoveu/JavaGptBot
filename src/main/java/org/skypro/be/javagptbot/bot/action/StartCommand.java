package org.skypro.be.javagptbot.bot.action;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class StartCommand implements BotAction {

    private final long chatId;

    public StartCommand(long chatId) {
        this.chatId = chatId;
    }

    @Override
    public SendMessage getAnswer() {

        return SendMessage.builder()
                .chatId(chatId)
                .text(START_MESSAGE)
                .build();

    }


    private static final String START_MESSAGE = """
            Привет! Я бот, который может проанализировать проект на Java
            и оценить его на предмет соблюдения принципов ООП, Чистого кода,
            SOLID, KISS, DRY, YAGNI. Проверить правильность применения
            основных паттернов проектирования.
            А также предложить варианты улучшения проекта.
            """;
}
